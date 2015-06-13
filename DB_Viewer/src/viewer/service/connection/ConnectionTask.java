package viewer.service.connection;

import viewer.exception.ConnectionFailureException;
import viewer.materials.Connection;

public interface ConnectionTask<T>
{
    T execute(Connection c) throws ConnectionFailureException;
}
