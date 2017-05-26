package org.kesy.djob.rpc;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import org.kesy.djob.rpc.RpcConnectionFactory.Connection;
import org.kesy.djob.rpc.RpcForwarder.RpcException;
import org.kesy.djob.rpc.SocketRpcProtos.ErrorReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.BlockingService;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.Service;

public class RpcServer {

	  private static final Logger LOG = LoggerFactory.getLogger(RpcServer.class.getName());

	  private final RpcForwarder rpcForwarder;
	  private final ServerRpcConnectionFactory rpcConnectionFactory;
	  private final ExecutorService executor;
	  private final ServerThread serverThread;
	  private final boolean waitForCallback;

	  /**
	   * @param rpcConnectionFactory Factory to use to receive connections from
	   *        clients. The implementation should be compatible with the factory
	   *        being used by the client.
	   * @param executorService Executor service be used for handling requests.
	   * @param closeConnectionAfterInvokingService If set to false, when calling a
	   *        nonblocking RPC, the incoming connection is left open until the
	   *        callback is invoked by the service. This behavior is needed for
	   *        truly async RPCs. If set to true, the incoming connection is closed
	   *        immediately after invoking the service. This means that the service
	   *        must invoke the callback passed to it in the same thread. Blocking
	   *        services are not affected by this parameter.
	   */
	  public RpcServer(ServerRpcConnectionFactory rpcConnectionFactory,
	      ExecutorService executorService,
	      boolean closeConnectionAfterInvokingService) {
		  this.rpcForwarder = new RpcForwarder();
		  this.rpcConnectionFactory = rpcConnectionFactory;
			this.executor = executorService;
			this.serverThread = new ServerThread();
			serverThread.setDaemon(true);
			this.waitForCallback = !closeConnectionAfterInvokingService;
		}

	  /**
	   * Register an RPC service implementation on this server.
	   */
	  public void registerService(Service service) {
	    rpcForwarder.registerService(service);
	  }

	  /**
	   * Register an RPC blocking service implementation on this server.
	   */
	  public void registerBlockingService(BlockingService service) {
	    rpcForwarder.registerBlockingService(service);
	  }

	  /**
	   * Start the server to listen for requests. The calling thread is blocked
	   * permanently.
	   */
	  public void run() {
	    serverThread.run();
	  }

	  /**
	   * Start the server to listen for requests in a separate. The calling thread
	   * is not blocked. The server runs in a daemon thread so the JVM will exit
	   * when no other thread is running. To manually shut down the server, call
	   * {@link #shutDown()}.
	   */
	  public void startServer() {
	    serverThread.start();
	  }

	  /**
	   * Returns a {@link Runnable} which runs the server. This is useful if you
	   * want to run the server using your own {@link ExecutorService}. Note that
	   * {@link #shutDown()} can still be used to stop the server.
	   */
	  public Runnable getServerRunnable() {
	    return serverThread;
	  }

	  /**
	   * @return Whether the server is running.
	   */
	  public boolean isRunning() {
	    return serverThread.isRunning();
	  }

	  /**
	   * Stops this server. Any requests that are in progress are immediately shut
	   * down. Also the {@link ExecutorService} provided while constructing the
	   * server is also shut down.
	   */
	  public void shutDown() {
	    serverThread.stopServer();
	  }

	  /**
	   * Thread that runs the server.
	   */
	  private class ServerThread extends Thread {

	    // Whether the server is running
	    private volatile boolean running = false;

	    @Override
	    public void run() {
	      LOG.info("Starting RPC server");
	      try {
	        running = true;
	        while (running) {
	          // Thread blocks here waiting for requests
	          Connection connection = rpcConnectionFactory.createConnection();
	          if (running && !executor.isShutdown()) {
	            if (connection.isClosed()) {
	              // Connection was closed, don't execute
	              try {
	                Thread.sleep(100);
	              } catch (InterruptedException e) {
	                throw new RuntimeException(e);
	              }
	            } else {
	              executor.execute(new ConnectionHandler(connection));
	            }
	          }
	        }
	      } catch (IOException ex) {
	        stopServer();
	      } finally {
	        running = false;
	      }
	    }

	    private boolean isRunning() {
	      return running;
	    }

	    private void stopServer() {
	      if (isRunning()) {
	        LOG.info("Shutting down RPC server");
	        running = false;
	        if (!executor.isShutdown()) {
	          executor.shutdownNow();
	        }
	        try {
	          rpcConnectionFactory.close();
	        } catch (IOException e) {
	          LOG.warn("Error while shutting down server", e);
	        }
	      }
	    }
	  }

	  /**
	   * Handles socket requests.
	   */
	  class ConnectionHandler implements Runnable {

	    private final Connection connection;

	    ConnectionHandler(Connection connection) {
	      this.connection = connection;
	    }

	    @Override
	    public void run() {
	      try {
	        // Parse request
	        SocketRpcProtos.Request.Builder builder = SocketRpcProtos.Request
	            .newBuilder();
	        connection.receiveProtoMessage(builder);
	        if (!builder.isInitialized()) {
	          sendResponse(handleError("Invalid request from client",
	              ErrorReason.BAD_REQUEST_DATA, null));
	          return;
	        }

	        SocketRpcProtos.Request rpcRequest = builder.build();
	        if (waitForCallback) {
	          forwardRpc(rpcRequest);
	        } else {
	          forwardBlockingRpc(rpcRequest);
	        }
	      } catch (IOException e) {
	        sendResponse(handleError("Bad request data from client",
	            ErrorReason.BAD_REQUEST_DATA, e));
	      }
	    }

	    private void forwardRpc(SocketRpcProtos.Request rpcRequest) {
	      // Create callback to pass to the forwarder
	      RpcCallback<SocketRpcProtos.Response> rpcCallback =
	          new RpcCallback<SocketRpcProtos.Response>() {
	        @Override
	        public void run(SocketRpcProtos.Response rpcResponse) {
	          sendResponse(rpcResponse);
	        }
	      };

	      // Forward request
	      try {
	        rpcForwarder.doRpc(rpcRequest, rpcCallback);
	      } catch (RpcException e) {
	        sendResponse(handleError(e.msg, e.errorReason, e.getCause()));
	      }
	    }

	    private void forwardBlockingRpc(SocketRpcProtos.Request rpcRequest) {
	      // Forward request
	      try {
	        SocketRpcProtos.Response rpcResponse =
	            rpcForwarder.doBlockingRpc(rpcRequest);
	        sendResponse(rpcResponse);
	      } catch (RpcException e) {
	        sendResponse(handleError(e.msg, e.errorReason, e.getCause()));
	      }
	    }

	    private void sendResponse(SocketRpcProtos.Response rpcResponse) {
	      try {
	        if (connection.isClosed()) {
	          // Connection was closed for some reason
	        	LOG.warn("Connection closed");
	          return;
	        }
	        connection.sendProtoMessage(rpcResponse);
	      } catch (IOException e) {
	        LOG.warn("Error while writing", e);
	      } finally {
	        try {
	          connection.close();
	        } catch (IOException e) {
	          // It's ok
	        	LOG.warn("Error while closing I/O", e);
	        }
	      }
	    }

	    private SocketRpcProtos.Response handleError(String msg,
	        ErrorReason reason, Throwable throwable) {
	    	LOG.warn(reason + ": " + msg, throwable);
	      return SocketRpcProtos.Response
	          .newBuilder()
	          .setError(msg)
	          .setErrorReason(reason)
	          .build();
	    }
	  }
	}
