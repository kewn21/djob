package org.kesy.djob.rpc;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import org.kesy.djob.rpc.RpcConnectionFactory.Connection;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLite.Builder;

class PersistentConnection implements Connection {

	  private final boolean client;
	  final Connection inner;

	  // Using fair locks so threads don't wait for too long
	  private final Semaphore readLock = new Semaphore(1, true /* fair */);
	  private final ReentrantLock writeLock = new ReentrantLock(true /* fair */);

	  PersistentConnection(Connection connection, boolean client) {
	    this.inner = connection;
	    this.client = client;
	  }

	  @Override
	  public void sendProtoMessage(MessageLite message)
	      throws IOException {
	    try {
	      writeLock.lockInterruptibly();
	    } catch (InterruptedException e) {
	      throw new IOException("Interrupted while waiting for write lock", e);
	    }
	    try {
	      inner.sendProtoMessage(message);
	    } finally {
	      writeLock.unlock();
	    }
	  }

	  /**
	   * Try to acquire read lock on this connection and block until it is
	   * available.
	   *
	   * @throws IOException If acquiring read lock was interrupted.
	   */
	  void acquireReadLock() throws IOException {
	    try {
	      readLock.acquire();
	    } catch (InterruptedException e) {
	      throw new IOException("Interrupted while waiting for read lock", e);
	    }
	  }

	  @Override
	  public void receiveProtoMessage(Builder messageBuilder) throws IOException {
	    // Get lock if client, for server it should already have lock
	    if (client) {
	      acquireReadLock();
	    } else {
	      readLock.drainPermits();
	    }
	    try {
	      inner.receiveProtoMessage(messageBuilder);
	    } finally {
	      readLock.release();
	    }
	  }

	  @Override
	  public void close() {
	    // No-op since we want to reuse it.
	  }

	  @Override
	  public boolean isClosed() {
	    return inner.isClosed();
	  }
	}