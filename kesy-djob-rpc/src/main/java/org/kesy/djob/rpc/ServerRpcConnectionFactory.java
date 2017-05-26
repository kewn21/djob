package org.kesy.djob.rpc;

import java.io.Closeable;
import java.io.IOException;

public interface ServerRpcConnectionFactory extends RpcConnectionFactory,
Closeable {

/**
* Stop accepting any more requests, should be called when shutting down the
* server.
*/
@Override
public void close() throws IOException;
}
