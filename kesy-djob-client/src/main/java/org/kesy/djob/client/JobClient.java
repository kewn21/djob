/**
 * 
 */ 
package org.kesy.djob.client; 

/**
 * @author kewn
 *
 */

import org.kesy.djob.rpc.RpcChannels;
import org.kesy.djob.rpc.RpcConnectionFactory;
import org.kesy.djob.rpc.SocketRpcConnectionFactories;

import com.google.protobuf.BlockingRpcChannel;
import com.google.protobuf.ServiceException;
 
public class JobClient {
	
    private int port;
    private String host;
    private int maxSize;
    
    public BlockingRpcChannel createChannel(){
    	RpcConnectionFactory connectionFactory = SocketRpcConnectionFactories
    			.createRpcConnectionFactory(host, port);
    	
    	return RpcChannels.newBlockingRpcChannel(connectionFactory);
    }
    
    public JobClient(int port, String host, int maxSize) {
        this.port = port;
        this.host = host;
        this.maxSize = maxSize;
    }
    
    public static void main(String[] args) throws ServiceException {
    }
}
 