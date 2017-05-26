
package org.kesy.djob.dex.datax.plugins.common;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kesy.djob.dex.line.Line;
import org.kesy.djob.dex.datax.common.plugin.LineSender;
import org.kesy.djob.dex.datax.common.plugin.PluginMonitor;
import org.kesy.djob.dex.datax.common.plugin.Reader;
import org.kesy.djob.dex.datax.common.plugin.Writer;


/**
 * A proxy which provides to database {@link Reader} plugin. <br/>
 * Usually this proxy is held by a Reader plugin. The proxy wraps ResultSet to line, then send line to {@link Writer}.
 * 
 * @see Reader
 * @see DBResultSetReceiver
 * 
 */
public class DBResultSetSender {
	private LineSender sender;

	protected PluginMonitor monitor;

	private int columnCount;

	private Map<String, SimpleDateFormat> dateFormatMap = new HashMap<String, SimpleDateFormat>();

	private SimpleDateFormat[] timeMap = null;

	private static final Log logger = LogFactory.getLog(DBResultSetSender.class);
	
	/**
	 * A static factory method which provides {@link DBResultSetSender}.
	 * 
	 * @param	sender
	 * 			a LineSender.
	 * 
	 * @return
	 * 			a DBResultSetSender instance.
	 * 
	 */
	public static DBResultSetSender newSender(LineSender sender) {
		return new DBResultSetSender(sender);
	}

	/**
	 * A normal constructor of {@link DBResultSetSender}.
	 * 
	 * @param lineSender
	 * 			a LineSender.
	 * 
	 * @see DBResultSetSender#newSender(LineSender)
	 * 
	 */
	public DBResultSetSender(LineSender lineSender) {
		this.sender = lineSender;
	}

	/**
	 * Set Monitor.
	 * 
	 * @param	iMonitor
	 * 			a PluginMonitor.
	 * 
	 */
	public void setMonitor(PluginMonitor iMonitor) {
		this.monitor = iMonitor;
	}
	
	/**
	 * Set date format when needs to deal with kinds of time format.
	 * 
	 * @param	dateFormatMap
	 * 			a map which key is String and value is {@link SimpleDateFormat}.
	 * 
	 */
	public void setDateFormatMap(Map<String, SimpleDateFormat> dateFormatMap) {
		this.dateFormatMap = dateFormatMap;
	}

	/**
	 * Send data in type of a {@link ResultSet} to {@link Writer}.
	 * 
	 * @param resultSet
	 * 			a {@link ResultSet}.
	 * 
	 * @throws SQLException
	 * 			if occurs SQLException.
	 * 
	 */
	public void sendToWriter(ResultSet resultSet) throws SQLException {
		String item = null;
		Timestamp ts = null;
		setColumnCount(resultSet.getMetaData().getColumnCount());
		setColumnTypes(resultSet);
		while (resultSet.next()) {
			Line line = sender.createLine();
			try {
				/* TODO: date format need to handle by transfomer plugin */
				for (int i = 1; i <= columnCount; i++) {
					if (null != timeMap[i]) {
						ts = resultSet.getTimestamp(i);
						if (null != ts) {
							item = timeMap[i].format(ts);
						} else {
							item = null;
						}
					} else {
						item = resultSet.getString(i);
					}
					line.addField(item);
				}
				boolean b = sender.sendToWriter(line);
				if (null != monitor) {
					if (b) {
						monitor.lineSuccess();
					} else {
						monitor.lineFail("Send one line failed!");
					}
				}
			} catch (SQLException e) {
				logger.error(e.getMessage() + "| One dirty line : " + line.toString('\t'));
			}
		}
		
	}

	/**
	 * Flush data in buffer (if exists) to Storage.
	 * 
	 * */
	public void flush() {
		if (sender != null) {
			sender.flush();
		}
	}
	
	private void setColumnTypes(ResultSet resultSet) throws SQLException {
		timeMap = new SimpleDateFormat[columnCount + 1];

		ResultSetMetaData rsmd = resultSet.getMetaData();
		
		for (int i = 1; i <= columnCount; i++) {
			String type = rsmd.getColumnTypeName(i).toLowerCase().trim();
			if (this.dateFormatMap.containsKey(type)) {
				timeMap[i] = this.dateFormatMap.get(type);
			}
		}
	}
	
	private void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}
}
