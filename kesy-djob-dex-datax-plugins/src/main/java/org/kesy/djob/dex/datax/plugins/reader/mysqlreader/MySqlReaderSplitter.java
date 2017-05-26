
package org.kesy.djob.dex.datax.plugins.reader.mysqlreader;

import static java.text.MessageFormat.format;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.kesy.djob.dex.datax.common.plugin.PluginParam;
import org.kesy.djob.dex.datax.common.plugin.PluginStatus;
import org.kesy.djob.dex.datax.common.plugin.Splitter;
import org.kesy.djob.dex.datax.common.utils.SplitUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MySqlReaderSplitter extends Splitter {
	
	private List<String> tableNames;

	private static final String SQL_WITHOUT_WHERE_PATTERN = "select {0} from {1}";

	private static final String SQL_WITH_WHERE_PATTERN = "select {0} from {1} where {2}";

	private static Logger logger = LoggerFactory.getLogger(MySqlReaderSplitter.class);
	
	public MySqlReaderSplitter(PluginParam iParam){
		param = iParam;
	}
	                                           
	
	@Override
	public int init() {
		String tables = param.getValue(ParamKey.tables);
		tableNames = SplitUtils.splitTables(tables);
		logger.info("MySqlReaderSpliter initialize successfully.");
		return PluginStatus.SUCCESS.value();
	}

	@Override
	public List<PluginParam> split() {
		return this.splitParamsList(param);
	}

	private List<PluginParam> splitParamsList(PluginParam iParams) {
		ArrayList<PluginParam> listParms = new ArrayList<PluginParam>();
		
		String where = iParams.getValue(ParamKey.where, null);
		String columns = iParams.getValue(ParamKey.columns,
				"*");
		for (String table : tableNames) {
			PluginParam oParams = SplitUtils.copyParam(iParams);
			String sql;
			if (StringUtils.isBlank(where)) {
				sql = format(SQL_WITHOUT_WHERE_PATTERN, columns, table);
			} else {
				sql = format(SQL_WITH_WHERE_PATTERN, columns, table, where);
			}
			oParams.putValue(ParamKey.sql, sql);
			listParms.add(oParams);
		}
		logger.info(" MySqlReaderSpliter splits successfully.");
		return listParms;
	}
}
