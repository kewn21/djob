package org.kesy.djob.rpc;

import java.io.Closeable;
import java.io.IOException;
import java.net.UnknownHostException;

import com.google.protobuf.MessageLite;

public interface RpcConnectionFactory {

	  /**
	   * Create a connection over which an RPC can be performed. Note that for some
	   * implementations, only one RPC should be performed over the connection
	   * returned. i.e. {@link Connection#sendProtoMessage(MessageLite)} and
	   * {@link Connection#receiveProtoMessage(MessageLite.Builder)} can be called
	   * just once.
	   * <p>
	   * In the case of a server side connection, this method should block the
	   * calling thread until a client makes a request.
	   */
	  Connection createConnection() throws UnknownHostException, IOException;

	  /**
	   * Client/Server connection for performing an RPC.
	   */
	  public interface Connection extends Closeable {

	    /**
	     * Send the given protocol buffer message over the connection.
	     */
	    void sendProtoMessage(MessageLite message) throws IOException;

	    /**
	     * Receive a protocol buffer message over the connection and parse it into
	     * the given builder. Note that this method will block until the message is
	     * received fully or some error occurs.
	     */
	    void receiveProtoMessage(MessageLite.Builder messageBuilder)
	        throws IOException;

	    /**
	     * Close the connection and do any cleanup if needed. The connection cannot
	     * be used after this method has been called.
	     */
	    @Override
	    void close() throws IOException;

	    /**
	     * Whether the connection is closed. This should be as accurate as possible
	     * so that clients/servers don't try to send/receive messages over this
	     * connection if it will fail.
	     */
	    boolean isClosed();
	  }
	}
