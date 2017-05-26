
package org.kesy.djob.core.json;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

/**
 * TODO : document me
 *
 * @author kewn
 */
public final class Xml {
	
	private static final String PATH_CHARSET = "utf-8";
	
	public static <T> T toObject(Class<T> requiredType, String filePath) throws IOException{
		filePath = URLDecoder.decode(filePath, PATH_CHARSET);
		FileInputStream input = new FileInputStream(filePath);
		XMLSerializer xmlSerializer = new XMLSerializer();
		JSON json =  xmlSerializer.readFromStream(input);
		String jsonString = json.toString();
		return Json.toObject(jsonString, requiredType);
	}

	public static <T> List<T> toList(Class<T> requiredType, String filePath) throws IOException{
		filePath = URLDecoder.decode(filePath, PATH_CHARSET);
		FileInputStream input = new FileInputStream(filePath);
		XMLSerializer xmlSerializer = new XMLSerializer();
		JSON json =  xmlSerializer.readFromStream(input);
		String jsonString = json.toString();
		return Json.toListObject(jsonString, requiredType);
	}
	
	public static <T> List<T> toList4jar(Class<T> requiredType, String filePath) throws IOException{
		InputStream s =  Xml.class.getResourceAsStream(filePath);
		XMLSerializer xmlSerializer = new XMLSerializer();
		JSON json =  xmlSerializer.readFromStream(s);
		String jsonString = json.toString();
		return Json.toListObject(jsonString, requiredType);
	}
}
