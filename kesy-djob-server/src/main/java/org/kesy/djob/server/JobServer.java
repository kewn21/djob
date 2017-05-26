/**
 * 
 */ 
package org.kesy.djob.server; 

/**
 * @author kewn
 *
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.Executors;

import org.kesy.djob.rpc.RpcServer;
import org.kesy.djob.rpc.ServerRpcConnectionFactory;
import org.kesy.djob.rpc.SocketRpcConnectionFactories;
import org.kesy.djob.sdu.api.cluster.ClusterInfo;
import org.kesy.djob.sdu.api.model.JobManagerServiceProto.JobManagerService;
import org.kesy.djob.sdu.api.model.JobMonitorServiceProto.JobMonitorService;
import org.kesy.djob.server.invoker.JobManagerInvoker;
import org.kesy.djob.server.invoker.JobMonitorInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class JobServer {
	
	private static final String	DEFAULT_CONFIG_PATH	= "/job/job-server.properties";
	private static final Logger logger = LoggerFactory.getLogger(JobServer.class);
	
    private int port;
    private int maxSize;
    private static RpcServer server;

	private static final String	PORT_STR			= "port";
	private static final String	SIZE_STR			= "maxSize";
    
    public JobServer() {
    	InputStream stream = null;
		try {
			stream = JobServer.class.getResource(DEFAULT_CONFIG_PATH)
					.openStream();
			Properties prop = new Properties();
			prop.load(stream);
			if (prop.isEmpty()) {
				logger.debug("no found a properties file to init Server");
			} else {
				port = Integer.parseInt(prop.getProperty(PORT_STR));
				maxSize = Integer.parseInt(prop.getProperty(SIZE_STR));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
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
 
    public JobServer(int port, int maxSize) {
        this.port = port;
        this.maxSize = maxSize;
    }
 
    public void createServer(){
    	try {
			ClusterInfo.ip = InetAddress.getLocalHost().getHostAddress();
			ClusterInfo.port = port;
		} catch (UnknownHostException e) {
			logger.error("Get cluster ip error", e);
		}
    	
    	// Start server
        ServerRpcConnectionFactory rpcConnectionFactory = SocketRpcConnectionFactories
                .createServerRpcConnectionFactory(port);
        server = new RpcServer(rpcConnectionFactory,
                Executors.newFixedThreadPool(maxSize), true);
    }
    
    //启动
    public void run() {
    	if(server == null){
    		createServer();
    	}

        server.registerBlockingService(JobManagerService
                .newReflectiveBlockingService(new JobManagerInvoker()));
        server.registerBlockingService(JobMonitorService
                .newReflectiveBlockingService(new JobMonitorInvoker()));
        
        server.run();
    }
    
    
    public String choice() throws IOException{	
        BufferedReader cmd = new BufferedReader(new InputStreamReader(System.in));
        return cmd.readLine();
    }
    
    public static void main(String[] args) throws IOException{
    	int port = 8100;
        int size = 20;
        System.out.println("是否要启动服务？  Y/N");
        JobServer jobServer = new JobServer(port, size);
		String cmd = jobServer.choice();
		if(cmd.equalsIgnoreCase("y")){	
			jobServer.run();
	    } else{
	        System.out.println("不启动服务!!!");	
	    }
    }
}
 