/**
 * 2014年8月22日
 */
package org.kesy.djob.test.lac;

import org.junit.Test;
import org.kesy.djob.lac.Startup;

/**
 * @author kewn
 *
 */
public class ServerTest {
	
	@Test
	public void start() {
		Startup startup = new Startup();
		startup.start();
	}

}
