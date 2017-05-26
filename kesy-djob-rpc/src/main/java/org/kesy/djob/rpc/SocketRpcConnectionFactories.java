package org.kesy.djob.rpc;

import java.net.InetAddress;

import org.kesy.djob.rpc.RpcConnectionFactory.Connection;

public class SocketRpcConnectionFactories {

	  private SocketRpcConnectionFactories() {
	  }

	  /**
	   * Create a client-side {@link RpcConnectionFactory}. This uses delimited
	   * communication mode which allows multiple protocol buffers to be
	   * sent/received over a single {@link Connection}.
	   */
	  public static RpcConnectionFactory createRpcConnectionFactory(String host,
	      int port) {
	    return new SocketRpcConnectionFactory(host, port, true /* delimited */);
	  }

	  /**
	   * Create an undelimited mode client-side {@link RpcConnectionFactory}. Only
	   * one RPC (request/response pair) can be performed over a single
	   * {@link Connection}.
	   *
	   * @deprecated Uses old delimited communication mode. Change client/server to
	   *             use delimited mode.
	   */
	  @Deprecated
	  public static RpcConnectionFactory createUndelimitedRpcConnectionFactory(
	      String host, int port) {
	    return new SocketRpcConnectionFactory(host, port, false /* delimited */);
	  }

	  /**
	   * Create a server-side {@link ServerRpcConnectionFactory} at the given port.
	   * This uses delimited communication mode which allows multiple protocol
	   * buffers to be sent/received over a single {@link Connection}.
	   */
	  public static ServerRpcConnectionFactory createServerRpcConnectionFactory(
	      int port) {
	    return new SocketServerRpcConnectionFactory(port, true /* delimited */);
	  }

	  /**
	   * Create a server-side {@link ServerRpcConnectionFactory} at the given port.
	   * This uses delimited communication mode which allows multiple protocol
	   * buffers to be sent/received over a single {@link Connection}.
	   *
	   * @param port Port that this server socket will be started on.
	   * @param backlog the maximum length of the queue. A value <=0 uses default
	   *        backlog.
	   * @param bindAddr the local InetAddress the server socket will bind to. A
	   *        null value binds to any/all local IP addresses.
	   */
	  public static ServerRpcConnectionFactory createServerRpcConnectionFactory(
	      int port, int backlog, InetAddress bindAddr) {
	    return new SocketServerRpcConnectionFactory(port, backlog, bindAddr,
	        true /* delimited */);
	  }

	  /**
	   * Create an undelimited server-side {@link ServerRpcConnectionFactory} at the
	   * given port. This uses delimited communication mode. Only one RPC
	   * (request/response pair) can be performed over a single {@link Connection}.
	   *
	   * @param port Port that this server socket will be started on.
	   * @param backlog the maximum length of the queue. A value <=0 uses default
	   *        backlog.
	   * @param bindAddr the local InetAddress the server socket will bind to. A
	   *        null value binds to any/all local IP addresses.
	   *
	   * @deprecated Uses old delimited communication mode. Change client/server to
	   *             use delimited mode.
	   */
	  @Deprecated
	  public static ServerRpcConnectionFactory
	      createUndelimitedServerRpcConnectionFactory(
	          int port, int backlog, InetAddress bindAddr) {
	    return new SocketServerRpcConnectionFactory(port, backlog, bindAddr,
	        false /* delimited */);
	  }
	}
