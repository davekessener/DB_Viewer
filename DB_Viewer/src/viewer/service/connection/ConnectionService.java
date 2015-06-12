package viewer.service.connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import viewer.exception.ConnectionFailureException;
import viewer.literals.URL;
import viewer.materials.Connection;

public class ConnectionService implements AutoCloseable
{
    private ConnectionThread thread_;
    private Map<String, Connection> cons_;
    
    public ConnectionService()
    {
        thread_ = new ConnectionThread();
        cons_ = new ConcurrentHashMap<String, Connection>();
        
        thread_.start();
    }
    
    public FailableFuture testConnection(URL url, String u, String p)
    {
        return new FailableFuture(register(() -> doTestConnection(url, u, p)));
    }
    
    public boolean doTestConnection(URL url, String u, String p) throws ConnectionFailureException
    {
        Connection c = new Connection(url, u, p);
        
        if(c.connected())
        {
            c.disconnect();
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public Future<String> establishConnection(String name, URL url, String u, String p) throws ConnectionFailureException
    {
        return register(() -> doEstablishConnection(name, url, u, p));
    }
    
    public String doEstablishConnection(String name, URL url, String u, String p) throws ConnectionFailureException
    {
        Connection c = new Connection(url, u, p);
        
        if(!c.connected()) throw new ConnectionFailureException(Connection.Failure.TIMEOUT);
        
        cons_.put(name = nextName(name), c);
        
        return name;
    }
    
    public Future<Void> closeConnection(String name)
    {
        return register(() -> doCloseConnection(name));
    }
    
    public void doCloseConnection(String name)
    {
        if(cons_.containsKey(name))
        {
            Connection c = cons_.get(name);
            cons_.remove(name);
            c.disconnect();
        }
    }
    
    private String nextName(String s)
    {
        String r = s;
        int v = 1;
        
        while(cons_.containsKey(r)) r = s + " (" + (++v) + ")";
        
        return r;
    }
    
    public boolean knowsConnection(String name)
    {
        return cons_.containsKey(name);
    }
    
    public <T> Future<T> register(String name, ConnectionTask<T> task)
    {
            assert knowsConnection(name) : "Precondition violated: knowsConnection(name)";
            
            Connection c = cons_.get(name);
            
            return register(() -> task.execute(c));
    }
    
    public Future<Void> register(VoidTask task)
    {
        return register(() -> { task.execute(); return Void.Return(); });
    }
    
    public <T> Future<T> register(Task<T> task)
    {
        Promise p = new Promise(task);
        
        thread_.register(p);
        
        return new Future<T>(p);
    }
    
    public static interface ConnectionTask<T> { T execute(Connection c) throws ConnectionFailureException; }

    @Override
    public void close() throws Exception
    {
        thread_.stop();
        
        for(Connection c : cons_.values())
        {
            c.disconnect();
        }
    }
}
