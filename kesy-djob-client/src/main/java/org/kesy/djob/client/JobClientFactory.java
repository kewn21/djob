/**
 * This file created at 2014-1-13.
 *
 */
package org.kesy.djob.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.BlockingRpcChannel;


/**
 * <code>{@link JobClientFactory}</code>
 *
 * TODO : document me
 *
 * @author kewn
 */
public class JobClientFactory {
	
	private static List<BlockingRpcChannel> channels = new ArrayList<BlockingRpcChannel>();
	
	private static Logger logger = LoggerFactory.getLogger(JobClientFactory.class);
	
	private static final String				DEFAULT_CONFIG_PATH		= "/job/job-client.properties";
	
	private static String					host;
	private static int						port;
	private static int						maxSize;

	private static final String				HOST_STR				= "host";
	private static final String				PORT_STR				= "port";
	private static final String				SIZE_STR				= "maxSize";

	private static volatile int				currentCount			= 0;

	private static final String				CURRENT_CHANNEL_SIZE	= "current channel size : %d";
	private static final String				GET_CHANNEL				= "get %d'th channel from channel pool";
	private static final String				CREATE_CHANNEL			= "create %d'th channel";
	
	
	//使用默认配置
	static {
		InputStream stream = null;
		try{
			stream = JobClientFactory.class.getResource(DEFAULT_CONFIG_PATH).openStream();
			Properties prop = new Properties();
			prop.load(stream);
			if(prop.isEmpty()){
				logger.debug("Haven't found a properties file to init Client");
			}else{
				host =prop.getProperty(HOST_STR);
				port =Integer.parseInt(prop.getProperty(PORT_STR));
				maxSize =Integer.parseInt(prop.getProperty(SIZE_STR));
			}
		}catch (IOException e) {
			logger.error(e.getMessage());
		}finally{
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
				logger.error("inputStream close exception");
				logger.error(e.getMessage());
			}
		}
	}
	
	private JobClientFactory(){
	}
	
	public static BlockingRpcChannel getChannel() {
		logger.info(String.format(CURRENT_CHANNEL_SIZE, currentCount));
		
		BlockingRpcChannel channel = null;
		
		synchronized (channels) {
			if(currentCount++ >= maxSize){
				int channelNum = (currentCount - 1) % maxSize;
				logger.info(String.format(GET_CHANNEL, channelNum));
				channel = channels.get(channelNum);
			}
			else {
				logger.info(String.format(CREATE_CHANNEL, currentCount));
				channel = new JobClient(port, host, maxSize).createChannel(); 
				channels.add(channel);
			}
		}
		
		return channel;
	}
}
