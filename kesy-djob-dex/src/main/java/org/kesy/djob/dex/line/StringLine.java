
package org.kesy.djob.dex.line;

/**
 * <code>{@link StringLine}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public class StringLine implements Line {
	
	private String line;

	/* (non-Javadoc)
	 * @see org.kesy.djob.core.data.Line#addField(java.lang.String)
	 */
	@Override
	public boolean addField(String field) {
		this.line = field;
		return true;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.core.data.Line#addField(java.lang.String, int)
	 */
	@Override
	public boolean addField(String field, int index) {
		// TODO implement Line.addField
		return false;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.core.data.Line#getField(int)
	 */
	@Override
	public String getField(int idx) {
		// TODO implement Line.getField
		return null;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.core.data.Line#checkAndGetField(int)
	 */
	@Override
	public String checkAndGetField(int idx) {
		// TODO implement Line.checkAndGetField
		return null;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.core.data.Line#getFieldNum()
	 */
	@Override
	public int getFieldNum() {
		// TODO implement Line.getFieldNum
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.core.data.Line#toStringBuffer(char)
	 */
	@Override
	public StringBuffer toStringBuffer(char separator) {
		// TODO implement Line.toStringBuffer
		return null;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.core.data.Line#toString(char)
	 */
	@Override
	public String toString(char separator) {
		return this.line;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.core.data.Line#fromString(java.lang.String, char)
	 */
	@Override
	public Line fromString(String lineStr, char separator) {
		// TODO implement Line.fromString
		return null;
	}

	/* (non-Javadoc)
	 * @see org.kesy.djob.core.data.Line#length()
	 */
	@Override
	public int length() {
		// TODO implement Line.length
		return 0;
	}

}
