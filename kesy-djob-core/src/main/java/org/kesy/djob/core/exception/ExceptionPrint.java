/**
 * 2014年8月25日
 */
package org.kesy.djob.core.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author kewn
 *
 */
public final class ExceptionPrint {
	
	public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

}
