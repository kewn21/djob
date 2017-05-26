package org.kesy.djob.rpc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.kesy.djob.rpc.RpcConnectionFactory.Connection;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLite.Builder;

class SocketConnection implements Connection {

	  private final Socket socket;
	  private final OutputStream out;
	  private final InputStream in;
	  private final boolean delimited;

	  SocketConnection(Socket socket, boolean delimited) throws IOException {
	    this.socket = socket;
	    this.delimited = delimited;

	    // Create input/output streams
	    try {
	      out = new BufferedOutputStream(socket.getOutputStream());
	      in = new BufferedInputStream(socket.getInputStream());
	    } catch (IOException e) {
	      // Cleanup and rethrow
	      try {
	        socket.close();
	      } catch (IOException ioe) {
	        // It's ok
	      }
	      throw e;
	    }
	  }

	  @Override
	  public void sendProtoMessage(MessageLite message) throws IOException {
	    // Write message
	    if (delimited) {
	      try {
	        message.writeDelimitedTo(out);
	        out.flush();
	      } catch (IOException e) {
	        // Cannot write anymore, just close socket
	        socket.close();
	        throw e;
	      }
	    } else {
	      message.writeTo(out);
	      out.flush();
	      socket.shutdownOutput();
	    }
	  }

	  @Override
	  public void receiveProtoMessage(Builder messageBuilder) throws IOException {
	    // Read message
	    if (delimited) {
	      messageBuilder.mergeDelimitedFrom(in);
	    } else {
	      messageBuilder.mergeFrom(in);
	    }
	  }

	  @Override
	  public void close() throws IOException {
	    if (!socket.isClosed()) {
	      socket.close();
	    }
	  }

	  @Override
	  public boolean isClosed() {
	    return socket.isClosed();
	  }
	}
