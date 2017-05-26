package org.kesy.djob.rpc;

import javax.net.SocketFactory;



@Deprecated
public class SocketRpcChannel extends RpcChannelImpl {

  /**
   * Create a channel for TCP/IP RPCs.
   */
  public SocketRpcChannel(String host, int port) {
    this(host, port, SocketFactory.getDefault());
  }

  // Used for testing
  SocketRpcChannel(String host, int port, SocketFactory socketFactory) {
    super(new SocketRpcConnectionFactory(host, port, socketFactory,
        false /* delimited */), RpcChannels.SAME_THREAD_EXECUTOR);
  }

  /**
   * Create new rpc controller to be used to control one request.
   */
  public SocketRpcController newRpcController() {
    return new SocketRpcController();
  }
}
