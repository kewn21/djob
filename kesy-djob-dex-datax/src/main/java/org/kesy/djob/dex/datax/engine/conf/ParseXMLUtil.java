
package org.kesy.djob.dex.datax.engine.conf;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.kesy.djob.dex.datax.common.consts.ConfigConsts;
import org.kesy.djob.dex.datax.common.exception.DataExchangeException;
import org.kesy.djob.dex.datax.common.utils.StrUtils;

/**
 * A utility tool to parse configure XML file.
 * 
 */
public class ParseXMLUtil {
	private static final Log LOG = LogFactory.getLog(ParseXMLUtil.class);

	/**
	 * Parse plugins configuration file, get all plugin configurations銆� *
	 * 
	 * @return a map mapping plugin name to plugin configurations銆� *
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, PluginConf> loadPluginConfig() {
		
		InputStream is = ParseXMLUtil.class.getResourceAsStream(ConfigConsts.CONF_ROOT_PATH + "/datax/plugins.xml");

		Map<String, PluginConf> plugins = new HashMap<String, PluginConf>();
		//File file = new File(PLUGINSXML);
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(is);
		} catch (DocumentException e) {
			LOG.error("DataX Can not find " + ConfigConsts.CONF_ROOT_PATH + "/datax/plugins.xml");
			throw new DataExchangeException(e.getCause());
		}
		String xpath = "/plugins/plugin";
		List<Node> pluginnode = (List<Node>) document.selectNodes(xpath);
		for (Node node : pluginnode) {
			PluginConf plugin = new PluginConf();
			plugin.setVersion(node.selectSingleNode("./version")
					.getStringValue());
			plugin.setName(node.selectSingleNode("./name").getStringValue());
			plugin
					.setTarget(node.selectSingleNode("./target")
							.getStringValue());
			plugin.setType(node.selectSingleNode("./type").getStringValue());
			plugin.setClassName(node.selectSingleNode("./class")
					.getStringValue());
			plugin.setMaxthreadnum(Integer.parseInt(node.selectSingleNode(
					"./maxthreadnum").getStringValue()));
			plugins.put(plugin.getName(), plugin);
		}
		return plugins;
	}

	/**
	 * Parse engine configuration file銆� *
	 * 
	 * @return {@link EngineConf}.
	 * 
	 * */
	public static EngineConf loadEngineConfig() {

		/*
		 * ENGINEXML =
		 * URLDecoder.decode(ParseXMLUtil.class.getResource(ENGINEXML)
		 * .getPath());
		 */

		InputStream is = ParseXMLUtil.class.getResourceAsStream(ConfigConsts.CONF_ROOT_PATH + "/datax/engine.xml");
		/*
		 * URLDecoder.decode(""); ParseXMLUtil.class.getResource(name)( );
		 */
		EngineConf engineConf = EngineConf.getInstance();
		// File file =is;
		SAXReader saxReader = new SAXReader();
		Document document = null;
		// System.out.println(file.toString());

		try {
			document = saxReader.read(is);
		} catch (DocumentException e) {
			LOG.error("DataX Can not find " + ConfigConsts.CONF_ROOT_PATH + "/datax/engine.xml" + " , program exits .");
			throw new DataExchangeException(e);
		}

		engineConf.setVersion(Integer.parseInt(document.selectSingleNode(
				"/engine/version").getStringValue()));

		engineConf.setStorageClassName(document.selectSingleNode(
				"/engine/storage/class").getStringValue());
		engineConf.setStorageLineLimit(StrUtils.getIntParam(
				document.selectSingleNode("/engine/storage/linelimit")
						.getStringValue(), 10000, 100, 1000000));
		engineConf.setStorageByteLimit(StrUtils.getIntParam(
				document.selectSingleNode("/engine/storage/bytelimit")
						.getStringValue(), 1000000, 10000, 100000000));
		engineConf.setStorageBufferSize(StrUtils.getIntParam(document
				.selectSingleNode("/engine/storage/buffersize")
				.getStringValue(), 10, 1, 500));

		return engineConf;
	}

}
