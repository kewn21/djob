package org.kesy.djob.rpc;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;

@Deprecated
public class SocketRpcServer extends RpcServer {

  /**
   * @param port Port that this server will be started on.
   * @param executorService To be used for handling requests.
   */
  public SocketRpcServer(int port, ExecutorService executorService) {
    this(port, 0, null, executorService);
  }

  /**
   * Constructor with customization to pass into java.net.ServerSocket(int port,
   * int backlog, InetAddress bindAddr)
   *
   * @param port
   *          Port that this server will be started on.
   * @param backlog
   *          the maximum length of the queue. A value <=0 uses default backlog.
   * @param bindAddr
   *          the local InetAddress the server will bind to. A null value binds
   *          to any/all local ip addresses.
   * @param executorService
   *          executorService To be used for handling requests.
   */
	public SocketRpcServer(int port, int backlog, InetAddress bindAddr,
			ExecutorService executorService) {
    super(new SocketServerRpcConnectionFactory(port, backlog, bindAddr,
        false /* delimited */), executorService,
        true /* closeConnectionAfterInvokingService */);
	}
}
