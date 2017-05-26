package org.kesy.djob.rpc;

import java.io.IOException;
import java.net.Socket;

import javax.net.SocketFactory;

class SocketRpcConnectionFactory implements RpcConnectionFactory {

	  private final String host;
	  private final int port;
	  private final SocketFactory socketFactory;
	  private final boolean delimited;

	  /**
	   * Constructor to create sockets the given host/port.
	   */
	  public SocketRpcConnectionFactory(String host, int port, boolean delimited) {
	    this(host, port, SocketFactory.getDefault(), delimited);
	  }

	  // Used for testing
	  SocketRpcConnectionFactory(String host, int port,
	      SocketFactory socketFactory, boolean delimited) {
	    this.host = host;
	    this.port = port;
	    this.socketFactory = socketFactory;
	    this.delimited = delimited;
	  }

	  @Override
	  public Connection createConnection() throws IOException {
	    // Open socket
	    Socket socket = socketFactory.createSocket(host, port);
	    return new SocketConnection(socket, delimited);
	  }
	}
