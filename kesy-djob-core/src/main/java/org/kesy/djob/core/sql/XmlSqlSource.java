/**
 * This file created at 2010-10-21.
 *
 */
package org.kesy.djob.core.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * <code>{@link XmlSqlSource}</code>
 *
 * 通过xml提供SQL语句的实现类
 *
 * @author kewn
 * @since 1.0.0
 */
public class XmlSqlSource {
	private static Logger		logger			= LoggerFactory.getLogger(XmlSqlSource.class);
	private String				sqlLocation		= "classpath*:/sqls/**/*.xml";															// sql配置文件所在位置
	private Map<String, String>	sqlMap			= new ConcurrentHashMap<String, String>();
	private String				sqlFolder		= null;

	/**
	 * 修正在内网无法访问xml属性文件dtd问题
	 */
	private static final String	PROPS_DTD_URI	= "http://java.sun.com/dtd/properties.dtd";
	private static final String	PROPS_DTD		= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<!-- DTD for properties -->" + "<!ELEMENT properties ( comment?, entry* ) >"
														+ "<!ATTLIST properties" + " version CDATA #FIXED \"1.0\">" + "<!ELEMENT comment (#PCDATA) >"
														+ "<!ELEMENT entry (#PCDATA) >" + "<!ATTLIST entry " + " key CDATA #REQUIRED>";

	/**
	 * @return the sqlLocation
	 */
	public String getSqlLocation() {
		return sqlLocation;
	}

	/**
	 * @param sqlLocation the sqlLocation to set
	 */
	public void setSqlLocation(String sqlLocation) {
		this.sqlLocation = sqlLocation;
	}

	public XmlSqlSource() {
	}

	public String getSql(String sqlId) {
		return sqlMap.get(sqlId.toLowerCase());
	}

	/**
	 * 从制定路径读取sql文件
	 */
	public void loadSqls() {
		if (this.sqlLocation == null || sqlLocation.trim().equals("")) {
			throw new RuntimeException("sql files location must set.");
		}
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = null;
		try {
			resources = resourcePatternResolver.getResources(this.sqlLocation);
			InputStream inputStream = null;
			for (Resource resource : resources) {
				try {
					URL url = resource.getURL();

					if (logger.isDebugEnabled()) {
						logger.debug("found {} resource '{}'", url.getProtocol(), url.toExternalForm());
					}
					// 如果sql是来自于文件系统
					if (ResourceUtils.URL_PROTOCOL_FILE.equalsIgnoreCase(url.getProtocol())) {
						if (sqlFolder == null) {
							findMonitorFolder(resource.getFile());
						}
						inputStream = new FileInputStream(resource.getFile());
					} else {
						inputStream = resource.getInputStream();
					}
					if (resource.getFilename().endsWith(".xml")) {
						loadFromXml(inputStream, resource.getFilename(), false);
					}
				} finally {
					IOUtils.closeQuietly(inputStream);
				}
			}
		} catch (Exception e) {
			logger.error("读取资源文件时出现错误,返回null ...", e);
		}
	}

	/**
	 * 根据资源文件定位受监控的sql根目录
	 */
	private void findMonitorFolder(File file) {
		while (file.getParentFile() != null) {
			File sqlFolderFile = file.getParentFile();
			if (sqlLocation.contains(sqlFolderFile.getName())) {
				sqlFolder = sqlFolderFile.getAbsolutePath();
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("xml sql folder %s", sqlFolder));
				}
				break;
			} else {
				file = sqlFolderFile;
			}
		}
	}

	/**
	 * 从文件中读取sql语句到缓存中
	 */
	private void loadFromXml(InputStream inputStream, String fileName, boolean enabledOverride) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		builder.setEntityResolver(new EntityResolver() {
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				if (systemId.equals(PROPS_DTD_URI)) {
					InputSource inputStream = new InputSource(new StringReader(PROPS_DTD));
					inputStream.setSystemId(PROPS_DTD_URI);
					return inputStream;
				}
				throw new SAXException("Invalid system identifier: " + systemId);
			}
		});
		Document document = builder.parse(inputStream);

		NodeList sqls = document.getElementsByTagName("entry");
		for (int i = 0; i < sqls.getLength(); i++) {
			Node element = sqls.item(i);
			String key = element.getAttributes().getNamedItem("key").getNodeValue();
			String value = element.getTextContent();
			// 处理编码
			//value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
			if (sqlMap.containsKey(key) && !enabledOverride) {
				throw new RuntimeException(String.format("duplicate key '%s' found in '%s'", key, fileName));
			}
			sqlMap.put(key.toLowerCase(), value);
		}
	}
}