/**
 * This file created at Jan 7, 2014.
 *
 */
package org.kesy.djob.sdu.api.exception;

/**
 * 类<code>{@link JobException}</code> 创建于 Jan 7, 2014<br/>
 * 类描述:
 * <p>
 * <li></li>
 * 
 * @author kewn
 */
public class JobException extends Exception {
	private static final long serialVersionUID = 4227588101753548333L;

	public JobException() {
		super();
	}

	public JobException(String msg) {
		super(msg);
	}

	public JobException(Throwable cause) {
		super(cause);
	}

	public JobException(String msg, Throwable cause) {
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
