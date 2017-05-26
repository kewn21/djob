package org.kesy.djob.rpc;

import org.kesy.djob.rpc.SocketRpcProtos.ErrorReason;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

public class SocketRpcController implements RpcController {

	  private boolean failed = false;
	  private String error = null;
	  private ErrorReason reason = null;

	  @Override
	  public void reset() {
	    failed = false;
	    error = null;
	    reason = null;
	  }

	  @Override
	  public boolean failed() {
	    return failed;
	  }

	  /**
	   * @return Reason for rpc error, to be used client side. Note that the reason
	   *         might have been specified by the server or client.
	   */
	  public ErrorReason errorReason() {
	    return reason;
	  }

	  @Override
	  public String errorText() {
	    return error;
	  }

	  @Override
	  public void startCancel() {
	    // Not yet supported
	    throw new UnsupportedOperationException(
	        "Cannot cancel request in Socket RPC");
	  }

	  @Override
	  public void setFailed(String reason) {
	    failed = true;
	    error = reason;
	  }

	  void setFailed(String error, ErrorReason errorReason) {
	    setFailed(error);
	    reason = errorReason;
	  }

	  @Override
	  public boolean isCanceled() {
	    // Not yet supported
	    throw new UnsupportedOperationException(
	        "Cannot cancel request in Socket RPC");
	  }

	  @Override
	  public void notifyOnCancel(RpcCallback<Object> callback) {
	    // Not yet supported
	    throw new UnsupportedOperationException(
	        "Cannot cancel request in Socket RPC");
	  }

	  @Override
	  public String toString() {
	    return new StringBuffer("SocketRpcController:")
	        .append("\nFailed: " + failed)
	        .append("\nError: " + error)
	        .append("\nReason: " + reason).toString();
	  }
	}
