
package org.kesy.djob.dex.datax.plugins.reader.streamreader;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.kesy.djob.dex.datax.common.exception.DataExchangeException;
import org.kesy.djob.dex.datax.common.exception.ExceptionTracker;
import org.kesy.djob.dex.datax.common.plugin.LineSender;
import org.kesy.djob.dex.datax.common.plugin.PluginStatus;
import org.kesy.djob.dex.datax.common.plugin.Reader;
import org.kesy.djob.dex.line.Line;
import org.kesy.djob.dex.line.StringLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author bazhen.csy
 *
 */
public class StreamReader extends Reader {	
	private char FIELD_SPLIT = '|';
	
	private String ENCODING = "UTF-8";
	
	private String nullString = "";
	private String filePath = "";
	private boolean hasHeader = false;
	
	private static Logger logger = LoggerFactory.getLogger(StreamReader.class.getCanonicalName());
	
	@Override
	public int init() {
		this.FIELD_SPLIT = param.getCharValue(ParamKey.fieldSplit, '|');
		this.ENCODING = param.getValue(ParamKey.encoding, "UTF-8");
		this.nullString = param.getValue(ParamKey.nullString, "");
		this.filePath = param.getValue(ParamKey.filepath,"");
		this.hasHeader = param.getBoolValue(ParamKey.hasHeader, false);
		return PluginStatus.SUCCESS.value();
	}


	@Override
	public int connect() {
		return 0;
	}

	@Override
	public int startRead(LineSender lineSender){
		
		int ret = PluginStatus.SUCCESS.value();
		
		File file = new File(filePath);
		if (!file.exists()) {
			return ret;
		}
		
		int previous;
		String fetch;
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file),this.ENCODING));
			
			boolean isFirstRow = true;
			while ((fetch = reader.readLine()) != null) {
				if (hasHeader && isFirstRow) {
					isFirstRow = false;
					continue;
				}
				
				previous = 0;
				Line line = lineSender.createLine();
				if (line instanceof StringLine) {
					line.addField(fetch);
				}
				else {
					for (int i = 0; i < fetch.length(); i++) {
						if (fetch.charAt(i) == this.FIELD_SPLIT) {
							line.addField(changeNull(fetch.substring(previous, i)));
							previous = i + 1;
						}
					}
					line.addField(fetch.substring(previous));
				}
				
				lineSender.sendToWriter(line);
			}
			lineSender.flush();
		}  catch (Exception e) {
			logger.error(ExceptionTracker.trace(e));
			throw new DataExchangeException(e.getCause());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				logger.error(ExceptionTracker.trace(e));
				throw new DataExchangeException(e.getCause());
			}
		}
		
		return ret;
	}


	@Override
	public int finish(){
		return PluginStatus.SUCCESS.value();
	}

	private String changeNull(final String item) {
		if (nullString != null && nullString.equals(item)) {
			return null;
		}
		return item;
	}
}
