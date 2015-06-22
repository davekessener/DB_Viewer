package viewer.service.connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javafx.application.Platform;
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
    
    public Future<Boolean> testConnection(URL url, String u, String p)
    {
        return request(() -> doTestConnection(url, u, p));
    }
    
    public boolean doTestConnection(URL url, String u, String p) throws ConnectionFailureException
    {
        try(Connection c = new Connection(url, u, p))
        {
            return c.connected();
        }
    }
    
    public Future<String> establishConnection(String name, URL url, String u, String p) throws ConnectionFailureException
    {
        return request(() -> doEstablishConnection(name, url, u, p));
    }
    
    @SuppressWarnings("resource")
    public String doEstablishConnection(String name, URL url, String u, String p) throws ConnectionFailureException
    {
        Connection c = new Connection(url, u, p);
        
        if(!c.connected()) throw new ConnectionFailureException(Connection.Failure.TIMEOUT);
        
        cons_.put(name = nextName(name), c);
        
        return name;
    }
    
    public Future<Void> closeConnection(String name)
    {
        return request(() -> doCloseConnection(name));
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

    public Future<Void> request(String id, VoidConnectionTask task)
    {
        return request(id, (Connection c) -> { task.execute(c); return Void.Return(); });
    }
    
    public <T> Future<T> request(String id, ConnectionTask<T> task)
    {
        assert knowsConnection(id) : "Precondition violated: knowsConnection(name)";
        
        Connection c = cons_.get(id);
        
        return request(() -> task.execute(c));
    }
    
    public Future<Void> request(VoidTask task)
    {
        return request(() -> { task.execute(); return Void.Return(); });
    }
    
    public <T> Future<T> request(Task<T> task)
    {
        Promise p = new Promise(task);
        
        thread_.register(p);
        
        return new Future<T>(p);
    }
    
    public void consume(Runnable r) { Platform.runLater(r); }
    
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
