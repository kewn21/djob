package org.kesy.djob.rpc;

import java.io.Closeable;
import java.io.IOException;
import java.net.UnknownHostException;

public class PersistentRpcConnectionFactory implements
ServerRpcConnectionFactory, Closeable {

private final RpcConnectionFactory factory;
private volatile PersistentConnection connection = null;
private final boolean server;

/**
* Create a client-side persistent {@link RpcConnectionFactory} which uses the
* given factory to create the persistent {@link Connection}.
* <p>
* Since only a single {@link Connection} is used for all RPCs, the
* {@link #close()} method must explicitly be called when the client has
* finished using the factory.
*/
public static PersistentRpcConnectionFactory createInstance(
  RpcConnectionFactory factory) {
return new PersistentRpcConnectionFactory(factory, false /* server */);
}

/**
* Create a server-side persistent {@link ServerRpcConnectionFactory} which
* uses the given factory to create the persistent {@link Connection}.
* <p>
* Note: {@link #close()} shuts down both the {@link Connection} and the
* factory.
*/
public static ServerRpcConnectionFactory createServerInstance(
  ServerRpcConnectionFactory factory) {
return new PersistentRpcConnectionFactory(factory, true /* server */);
}

private PersistentRpcConnectionFactory(
  RpcConnectionFactory rpcConnectionFactory, boolean server) {
this.factory = rpcConnectionFactory;
this.server = server;
}

@Override
public Connection createConnection() throws UnknownHostException,
  IOException {
// Use Java 1.5+ double checked locking to lazy init
PersistentConnection local = connection;
if (local == null) {
  local = initConnecton();
}

if (server) {
  // Server thread blocks here for readLock so that multiple handler threads
  // aren't created.
  local.acquireReadLock();
}

return local;
}

private synchronized PersistentConnection initConnecton()
  throws UnknownHostException, IOException {
PersistentConnection local = connection;
if (local == null) {
  connection = local = new PersistentConnection(factory.createConnection(),
      !server);
}
return local;
}

/**
* Closes the connection used by this factory and in case of server-side,
* closes the factory as well.
*
* @see Connection#close()
* @see ServerRpcConnectionFactory#close()
*/
@Override
public void close() throws IOException {
PersistentConnection local = connection;
if (local != null) {
  local.inner.close();
}

// Close server if needed.
if (factory instanceof ServerRpcConnectionFactory) {
  ((ServerRpcConnectionFactory) factory).close();
}
}
}