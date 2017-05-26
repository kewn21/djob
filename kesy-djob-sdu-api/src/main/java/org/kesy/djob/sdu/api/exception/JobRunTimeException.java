/**
 * This file created at Jan 7, 2014.
 *
 */
package org.kesy.djob.sdu.api.exception;

/**
 * 类<code>{@link JobRunTimeException}</code>  创建于 Jan 7, 2014<br/>
 * 类描述:<p>
 * <li></li>
 * @author kewn
 */
public class JobRunTimeException extends RuntimeException {

	private static final long serialVersionUID = -7068323858345464845L;

	public JobRunTimeException() {
		super();
	}

	public JobRunTimeException(String msg) {
		super(msg);
	}

	public JobRunTimeException(Throwable cause) {
		super(cause);
	}

	public JobRunTimeException(String msg, Throwable cause) {
		super(msg, cause);
	}

	@Override
	public String toString() {
		Throwable cause = super.getCause();
		if (null == cause || this == cause) {
			return super.toString();
		} else {
			return super.toString() + " [See nested exception: " + cause + "]";
		}
	}
}
