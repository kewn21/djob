
package org.kesy.djob.dex.datax.plugins.writer.streamwriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.kesy.djob.dex.datax.common.exception.DataExchangeException;
import org.kesy.djob.dex.datax.common.exception.ExceptionTracker;
import org.kesy.djob.dex.datax.common.plugin.LineReceiver;
import org.kesy.djob.dex.datax.common.plugin.PluginStatus;
import org.kesy.djob.dex.datax.common.plugin.Writer;
import org.kesy.djob.dex.line.Line;
import org.kesy.djob.dex.line.StringLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamWriter extends Writer {
	private char FIELD_SPLIT = '|';

	private String ENCODING = "UTF-8";

	private String PREFIX = "";

	private boolean printable = true;

	private String nullString = "";

	private String filePath = "/";

	private static Logger logger = LoggerFactory.getLogger(StreamWriter.class.getCanonicalName());

	@Override
	public int init() {
		this.FIELD_SPLIT = param.getCharValue(ParamKey.fieldSplit, '|');
		this.ENCODING = param.getValue(ParamKey.encoding, "UTF-8");
		this.PREFIX = param.getValue(ParamKey.prefix, "");
		this.nullString = param.getValue(ParamKey.nullChar, this.nullString);
		this.printable = param.getBoolValue(ParamKey.print, this.printable);

		this.filePath = param.getValue(ParamKey.tofilepath, this.filePath);
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int connect() {
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int startWrite(LineReceiver linereceiver) {
		Line line;
		BufferedWriter writer = null;
		try {
			logger.info("StreamWriter begin write to:"+filePath);
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(filePath)), this.ENCODING));
			while ((line = linereceiver.getFromReader()) != null) {
				if (this.printable) {
					writer.write(makeVisual(line));
				} else {
				}
			}
			writer.flush();
			return PluginStatus.SUCCESS.value();
		} catch (Exception e) {
			logger.error(ExceptionTracker.trace(e));
			throw new DataExchangeException(e.getCause());
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				logger.error(ExceptionTracker.trace(e));
				throw new DataExchangeException(e.getCause());
			}
		}
	}

	@Override
	public int commit() {
		return 0;
	}

	@Override
	public int finish() {
		return 0;
	}

	private String makeVisual(Line line) {
		if (line == null) {
			return this.PREFIX + "\n";
		}
		
		if (line instanceof StringLine) {
			return line.toString((char)31) + "\n";
		}
		
		if (line.getFieldNum() == 0) {
			return this.PREFIX + "\n";
		}

		int i = 0;
		String item = null;
		int num = line.getFieldNum();
		StringBuilder sb = new StringBuilder();

		sb.append(this.PREFIX);
		for (i = 0; i < num; i++) {
			item = line.getField(i);
			if (null == item) {
				sb.append(nullString);
			} else {
				sb.append(item);
			}

			if (i != num - 1) {
				sb.append(FIELD_SPLIT);
			} else {
				sb.append('\n');
			}
		}

		return sb.toString();
	}

}
