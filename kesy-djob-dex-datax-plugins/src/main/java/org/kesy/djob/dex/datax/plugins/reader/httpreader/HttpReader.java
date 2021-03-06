
package org.kesy.djob.dex.datax.plugins.reader.httpreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.kesy.djob.dex.datax.common.exception.DataExchangeException;
import org.kesy.djob.dex.datax.common.exception.ExceptionTracker;
import org.kesy.djob.dex.datax.common.plugin.LineSender;
import org.kesy.djob.dex.datax.common.plugin.PluginParam;
import org.kesy.djob.dex.datax.common.plugin.PluginStatus;
import org.kesy.djob.dex.datax.common.plugin.Reader;
import org.kesy.djob.dex.line.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpReader extends Reader {

	private static Logger logger = LoggerFactory.getLogger(HttpReader.class.getCanonicalName());

	private String FIELD_SPLIT = "\t";
	private String ENCODING = "UTF-8";
	private String nullString = "";
	private String httpURL = "";

	@Override
	public int init(){
		logger.info("begin init httpreader");

		this.FIELD_SPLIT = param.getValue(ParamKey.fieldSplit, "\t");
		this.ENCODING = param.getValue(ParamKey.encoding, "UTF-8");
		this.nullString = param.getValue(ParamKey.nullString, "");
		this.httpURL = param.getValue(ParamKey.httpURLs);
		
		logger.info("end init httpreader");
		return 0;
	}

	@Override
	public int connect() {
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public int prepare(PluginParam param) {
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public List<PluginParam> split(PluginParam param) {
		HttpURLSplitter spliter = new HttpURLSplitter();
		spliter.setParam(param);
		spliter.init();
		return spliter.split();
	}

	@Override
	public int startRead(LineSender sender) {
		logger.info("begin startLoad httpreader");
		
		int ret = PluginStatus.SUCCESS.value();

		try {
			URL url = new URL(this.httpURL);
			URLConnection urlc = null;
			try {
				urlc = url.openConnection();
				HttpURLConnection httpUrlConnection = (HttpURLConnection) urlc;
				BufferedReader in = new BufferedReader(new InputStreamReader(
						httpUrlConnection.getInputStream(), this.ENCODING));
				String readline = in.readLine();
				while (readline != null) {
					Line line = sender.createLine();
					String[] strs = readline.split(this.FIELD_SPLIT);
					for (String str : strs) {
						if (!str.equals(this.nullString)) {
							line.addField(str);
						} else {
							line.addField(null);
						}
					}
					sender.sendToWriter(line);
					readline = in.readLine();
				}
				sender.flush();
			} catch (IOException e) {
				logger.error(ExceptionTracker.trace(e));
				String message = String.format("Httpreader failed: %s,%s",
						e.getMessage(), e.getCause());
				throw new DataExchangeException(message);
			}
		} catch (MalformedURLException e) {
			logger.error(ExceptionTracker.trace(e));
			String message = String.format("Httpreader failed: %s,%s",
					e.getMessage(), e.getCause());
			throw new DataExchangeException(message);
		}

		logger.info("HttpReader read work ends .");
		return ret;
	}

	@Override
	public int finish() {
		return PluginStatus.SUCCESS.value();
	}

}
