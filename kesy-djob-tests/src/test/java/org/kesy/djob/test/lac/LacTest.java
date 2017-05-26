package org.kesy.djob.test.lac;

import org.junit.Test;
import org.kesy.djob.lac.Startup;

public class LacTest {
	
	@Test
	public void test01() {
		Startup startup = new Startup();
		startup.start();
	}

}
