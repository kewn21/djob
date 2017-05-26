/**
 * This file created at 2014-1-18.
 *

 */
package org.kesy.djob.lac;

import org.kesy.djob.core.spring.SpringFactory;
import org.kesy.djob.sdu.api.JobManagerFactory;
import org.kesy.djob.server.JobServerFactory;

/**
 * <code>{@link Startup}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public class Startup {

	public static void main(String[] args) {
		Startup startup = new Startup();
		startup.start();
	}

	public void start() {
		
		SpringFactory.load();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				JobManagerFactory.get().startAll();
			}
		}).start();
		
		JobServerFactory.createServer().run();

		/*new Thread(new Runnable() {

			@Override
			public void run() {
				JobServer jobServer = new JobServer();
				jobServer.run();
			}
		}).start();

		BufferedReader cmd = new BufferedReader(
				new InputStreamReader(System.in));
		try {
			String cmdKey;
			while (!(cmdKey = cmd.readLine()).equalsIgnoreCase("exit")) {
				System.out.println("your input : " + cmdKey);
			}
		} catch (IOException e) {
		} finally {
			System.exit(0);
		}*/
	}
}
