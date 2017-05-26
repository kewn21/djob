package org.kesy.djob.rpc;

import java.util.concurrent.Executor;

import com.google.protobuf.BlockingRpcChannel;
import com.google.protobuf.RpcChannel;

public final class RpcChannels {

	  static final Executor SAME_THREAD_EXECUTOR = new Executor() {
	    @Override
	    public void execute(Runnable command) {
	      command.run();
	    }
	  };

	  private RpcChannels() {
	  }

	  /**
	   * Create a {@link RpcChannel} that uses the given
	   * {@link RpcConnectionFactory} to connect to the RPC server and the given
	   * {@link Executor} to listen for the RPC response after sending the request.
	   * RPCs made using this {@link RpcChannel} will not block the thread calling
	   * the RPC method. Use {@link #newBlockingRpcChannel(RpcConnectionFactory)} if
	   * you want the RPC method to block.
	   * <p>
	   * This channel doesn't call the callback if the server-side implementation
	   * did not call the callback. If any error occurs, it will call the callback
	   * with null and update the controller with the error.
	   */
	  public static RpcChannel newRpcChannel(
	      RpcConnectionFactory connectionFactory, Executor executor) {
	    return new RpcChannelImpl(connectionFactory, executor);
	  }

	  /**
	   * Create a {@link BlockingRpcChannel} that uses the given
	   * {@link RpcConnectionFactory} to connect to the RPC server.
	   */
	  public static BlockingRpcChannel newBlockingRpcChannel(
	      RpcConnectionFactory connectionFactory) {
	    return new RpcChannelImpl(connectionFactory, SAME_THREAD_EXECUTOR);
	  }
	}