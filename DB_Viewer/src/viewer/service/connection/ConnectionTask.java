package viewer.service.connection;

import viewer.exception.ConnectionFailureException;
import viewer.materials.Connection;

interface ConnectionTask<T>
{
    T execute(Connection c) throws ConnectionFailureException;
}
