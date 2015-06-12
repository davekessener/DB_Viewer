package viewer.service.connection;

import viewer.exception.ConnectionFailureException;
import viewer.materials.Connection;

interface VoidConnectionTask
{
    void execute(Connection c) throws ConnectionFailureException;
}
