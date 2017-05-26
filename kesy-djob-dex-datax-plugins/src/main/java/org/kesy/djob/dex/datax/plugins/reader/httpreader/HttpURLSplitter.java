
package org.kesy.djob.dex.datax.plugins.reader.httpreader;

import java.util.ArrayList;
import java.util.List;

import org.kesy.djob.dex.datax.common.plugin.PluginParam;
import org.kesy.djob.dex.datax.common.plugin.PluginStatus;
import org.kesy.djob.dex.datax.common.plugin.Splitter;
import org.kesy.djob.dex.datax.common.utils.SplitUtils;


public class HttpURLSplitter extends Splitter {

	private String URLDelimiter = ";";
	private String httpURLs = "";

	@Override
	public int init(){
		this.URLDelimiter = param.getValue(
				ParamKey.URLDelimiter, ";");
		this.httpURLs = param.getValue(ParamKey.httpURLs);
		
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public List<PluginParam> split(){
		List<PluginParam> v = new ArrayList<PluginParam>();
		String[] urls = httpURLs.split(this.URLDelimiter);
		for (String url : urls) {
			if ("" != url) {
				PluginParam oParams = SplitUtils.copyParam(this
						.getParam());
				oParams.putValue(ParamKey.httpURLs, url.trim());
				v.add(oParams);
			}
		}
		return v;
	}

}
