
package org.kesy.djob.dex.line;

import java.util.Map;

/**
 * <code>{@link FieldOrderLine}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public class FieldOrderLine implements Line {
	
	private String[] fieldList;
	
	private final Map<Integer, Integer> addOrderMap;
	
	private int length = 0;

	private int fieldNum = 0;
	
	public FieldOrderLine(int fieldSize, Map<Integer, Integer> addOrderMap) {
		this.fieldList = new String[fieldSize];
		this.addOrderMap = addOrderMap;
	}

	/**
	 * Clear 
	 */
	public void clear() {
		length = 0;
		fieldNum = 0;
	}

	/**
	 * Get length of all fields, exclude separate.
	 * 
	 * @return
	 *			length of all fields.
	 *
	 * */
	@Override
	public int length() {
		return length;
	}

	/**
	 * Add a field into the {@link Line}.
	 * 
	 * @param	field	
	 * 			Field added into {@link Line}.
	 * 
	 * @return 
	 * 			true for OK, false for failure.
	 * 
	 * */
	@Override
	public boolean addField(String field) {
		if (addOrderMap.containsKey(fieldNum)) {
			fieldList[addOrderMap.get(fieldNum)] = field;
		}
		
		fieldNum++;
		if (field != null)
			length += field.length();
		return true;
	}

	/**
	 * 	
	 * Add a field into the {@link Line}.
	 * 
	 * @param	field	
	 * 			field added into {@link Line}.
	 * 
	 * @param 	index	
	 * 			given position of field in the {@link Line}.
	 * 
	 * @return 
	 *			true for OK, false for failure.
	 *
	 * */
	@Override
	public boolean addField(String field, int index) {
		fieldList[index] = field;
		if (fieldNum < index + 1)
			fieldNum = index + 1;
		if (field != null)
			length += field.length();
		return true;
	}

	/**
	 * Get number of total fields in the {@link Line}.
	 * 
	 * @return
	 *			number of total fields in {@link Line}.
	 *
	 * */
	@Override
	public int getFieldNum() {
		return fieldNum;
	}

	/**
	 * Get one field of the {@link Line} indexed by the param.
	 * 
	 * @param	idx
	 * 			given position of the {@link Line}.
	 * 
	 * @return
	 *			field indexed by the param.
	 *
	 * */
	@Override
	public String getField(int idx) {
		return fieldList[idx];
	}
	
	/**
	 * Get one field of the {@link Line} indexed by the param.
	 * if idx specified by user beyond field number of {@link Line}
	 * null will be returned
	 * 
	 * @param	 idx
	 * 			given position of the {@link Line}.
	 * 
	 * @return
	 *			field indexed by the param.
	 *
	 * */
	public String checkAndGetField(int idx) {
		if (idx < 0 ||
				idx >= fieldNum) {
			return null;
		}
		return fieldList[idx];
	}

	/**
	 * Use param as separator of field, format the {@link Line} into {@link StringBuffer}.
	 * 
	 * @param	separator	
	 * 			field separate.
	 * 
	 * @return
	 * 			{@link Line} in {@link StringBuffer} style.
	 * 
	 * */
	@Override
	public StringBuffer toStringBuffer(char separator) {
		StringBuffer tmp = new StringBuffer();
		tmp.append(fieldNum);
		tmp.append(":\t");
		for (int i = 0; i < fieldNum; i++) {
			tmp.append(fieldList[i]).append(separator);
		}
		return tmp;
	}
	
	/**
	 * Use param as separator of field, translate the {@link Line} into {@link String}.
	 * 
	 * @param 	separator
	 * 			field separate.
	 * 
	 * @return
	 * 			{@link Line} in {@link String}.
	 * 
	 * */
	@Override
	public String toString(char separator) {
		return this.toStringBuffer(separator).toString();
	}

	/**
	 *  [empty implement]<br>
	 *  Use param(separator) as separator of field, split param(linestr) and construct {@link Line}.
	 *  
	 *  @param	lineStr
	 *			String will be translated into {@link Line}.
	 *  
	 *  @param 	separator
	 *			field separate.
	 *  
	 *  @return	
	 *  		{@link Line}
	 *  
	 * */
	@Override
	public Line fromString(String lineStr, char separator) {
		return null;
	}
}
