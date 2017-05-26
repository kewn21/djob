/**
 * This file created at 2014年5月16日.
 */
package org.kesy.djob.web;

import java.util.Map;

/**
 * <code>{@link DataResponse}</code>
 *
 * TODO : document me
 *
 * @author dengqb
 */
public class DataResponse {
	
	private String[] columnNames;
	private String[][] rows;
	private int resultCode = ResultCode.success.value;
	private Object resultValue;
	private String exception;
	private Map<String, Object> callbackParams;
	
	/**
	 * @return the columnNames
	 */
	public String[] getColumnNames() {
		return columnNames;
	}
	/**
	 * @param columnNames the columnNames to set
	 */
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	/**
	 * @return the rows
	 */
	public String[][] getRows() {
		return rows;
	}
	/**
	 * @param rows the rows to set
	 */
	public void setRows(String[][] rows) {
		this.rows = rows;
	}
	/**
	 * @return the resultCode
	 */
	public int getResultCode() {
		return resultCode;
	}
	/**
	 * @param resultCode the resultCode to set
	 */
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public Object getResultValue() {
		return resultValue;
	}
	public void setResultValue(Object resultValue) {
		this.resultValue = resultValue;
	}
	/**
	 * @return the exception
	 */
	public String getException() {
		return exception;
	}
	/**
	 * @param exception the exception to set
	 */
	public void setException(String exception) {
		this.exception = exception;
	}
	/**
	 * @return the callbackParams
	 */
	public Map<String, Object> getCallbackParams() {
		return callbackParams;
	}
	/**
	 * @param callbackParams the callbackParams to set
	 */
	public void setCallbackParams(Map<String, Object> callbackParams) {
		this.callbackParams = callbackParams;
	}
	

	public static enum ResultCode {
		success(200),
		error(500),
		timeout(408);
		
		public final int value;

		ResultCode(int value){
	        this.value = value;
	    }
		
		@Override
		public String toString() {
			return String.valueOf(this.value);
		}
	}
	
	public static DataResponseBuilder newBuilder() {
		return new DataResponseBuilder();
	}
	
	
	public static final class DataResponseBuilder {
		private String[] columnNames;
		private String[][] rows;
		private int resultCode = ResultCode.success.value;
		private Object resultValue;
		private String exception;
		private Map<String, Object> callbackParams;
		
		public DataResponseBuilder setColumnNames(String[] columnNames) {
			this.columnNames = columnNames;
			return this;
		}
		public DataResponseBuilder setRows(String[][] rows) {
			this.rows = rows;
			return this;
		}
		public DataResponseBuilder setResultCode(int resultCode) {
			this.resultCode = resultCode;
			return this;
		}
		public DataResponseBuilder setResultValue(Object resultValue) {
			this.resultValue = resultValue;
			return this;
		}
		public DataResponseBuilder setException(String exception) {
			this.resultCode = ResultCode.error.value;
			this.exception = exception;
			return this;
		}
		public DataResponseBuilder setCallbackParams(Map<String, Object> callbackParams) {
			this.callbackParams = callbackParams;
			return this;
		}
		
		public DataResponse build() {
			DataResponse response = new DataResponse();
			response.setColumnNames(columnNames);
			response.setRows(rows);
			response.setResultCode(resultCode);
			response.setResultValue(resultValue);
			response.setException(exception);
			response.setCallbackParams(callbackParams);
			return response;
		}
	}
}
