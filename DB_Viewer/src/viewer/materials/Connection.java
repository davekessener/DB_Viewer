package viewer.materials;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import viewer.exception.ConnectionFailureException;
import viewer.literals.URL;
import viewer.materials.sql.Date;
import viewer.materials.sql.SQLObject;
import viewer.materials.sql.Timestamp;
import viewer.materials.sql.Varchar;
import viewer.materials.sql.Number;
import viewer.service.Logger;

public class Connection
{
    private static int ConnectionID = 0;
    
    private final int id_;
    private java.sql.Connection con_;
    
    public Connection(URL url, String user, String password) throws ConnectionFailureException
    {
        id_ = ConnectionID++;
        
        try
        {
//            OracleDataSource ods = new OracleDataSource();
//            ods.setURL(url.toString());
//            con_ = ods.getConnection(user, password);

//            Logger.Log("Connected to '" + url.toString() + "' @" + user + ":" + password + ".");
            DriverManager.setLoginTimeout(5);
            con_ = DriverManager.getConnection(url.toString(), user, password);
        }
        catch(SQLException e)
        {
            throw new ConnectionFailureException(GetFailure(e.getErrorCode()));
        }
        
        Logger.Log("Connection %d established (%s@%s).", id_, user, url.toString());
    }
    
    public int getID() { return id_; }
    
    public synchronized boolean connected() throws ConnectionFailureException
    { 
        try
        {
            java.sql.Connection c = con_;
            con_ = null;
            
            if(c != null && c.isValid(3))
            {
                con_ = c;
                return true;
            }
            
            return false;
        }
        catch(SQLException e)
        {
            throw new ConnectionFailureException(GetFailure(e.getErrorCode()));
        }
    }
    
    public synchronized void disconnect()
    {
        try
        {
            if(connected())
            {
                Logger.Log("Disconnected line %d.", id_);
                
                java.sql.Connection c = con_;

                con_ = null;
                c.close();
            }
        }
        catch(ConnectionFailureException | SQLException e) { }
    }
    
    public synchronized void execute(String query) throws ConnectionFailureException
    {
        String id = query.split("[ \t]+")[0];
        
        if(QUERIES.contains(id)) query(query);
        else if(UPDATES.contains(id)) modify(query);
        else throw new Error("Unknown sql query: \"" + query +"\"!");
    }
    
    public synchronized void executeAll(List<String> queries) throws ConnectionFailureException
    {
        try
        {
            Statement s = con_.createStatement();
            
            for(String q : queries)
            {
                assert UPDATES.contains(q.split("[ \t]+")[0]) : "Precondition violated: query is UPDATE";
                
                s.addBatch(q);
            }
            
            s.executeBatch();
        }
        catch(SQLException e)
        {
            throw new ConnectionFailureException(GetFailure(e.getErrorCode()));
        }
    }

    public synchronized Relation query(String query) throws ConnectionFailureException
    {
        assert connected() : "Precondition violated: connected()";
        assert QUERIES.contains(query.split("[ \t]+")[0]) : "Precondition violated: QUERIES.contains(query)";
        
        Logger.Log("Querying connection %d for \"%s\"", id_, query);
        
        try
        {
            Statement s = con_.createStatement();
            
            s.setQueryTimeout(5);
            
            return GenerateRelation(s.executeQuery(query));
        }
        catch(SQLException e)
        {
            throw new ConnectionFailureException(GetFailure(e.getErrorCode()));
        }
    }
    
    public synchronized int modify(String query) throws ConnectionFailureException
    {
        assert connected() : "Precondition violated: connected()";
        assert UPDATES.contains(query.split("[ \t]+")[0]) : "Precondition violated: UPDATES.contains(query)";

        Logger.Log("Querying connection %d for \"%s\"", id_, query);
        
        try
        {
            Statement s = con_.createStatement();
            
            s.setQueryTimeout(5);
            
            return s.executeUpdate(query);
        }
        catch(SQLException e)
        {
            throw new ConnectionFailureException(GetFailure(e.getErrorCode()));
        }
    }
    
    private static Relation GenerateRelation(ResultSet r) throws SQLException
    {
        Relation.Factory f = Relation.GetFactory();
        
        ResultSetMetaData m = r.getMetaData();
        int l = m.getColumnCount();
        
        for(int i = 0 ; i < l ; ++i)
        {
            Class<?> c;
            
            try
            {
                c = Class.forName(m.getColumnClassName(i + 1));
            }
            catch(ClassNotFoundException e)
            {
                java.util.logging.Logger.getGlobal().log(Level.SEVERE, 
                        "SQL class '" + m.getColumnClassName(i) + "' unknown!", e);
                throw new Error("Unknown class " + m.getColumnClassName(i + 1));
            }
            
            f.addColumn(m.getColumnName(i + 1), WRAPPERS.get(c));
        }
        
        while(r.next())
        {
            SQLObject[] row = new SQLObject[l];
            
            for(int i = 0 ; i < l ; ++i)
            {
                row[i] = SQLObject.Generate(r.getObject(i + 1));
            }
            
            f.addRow(row);
        }

        r.close();
        
        return f.produce();
    }
    
    private static final Map<Integer, Failure> ORACLE_ERRORCODES = new HashMap<>();
    private static final Map<Class<?>, Class<? extends SQLObject>> WRAPPERS = new HashMap<>();
    
    public static class Failure
    {
        public static final Failure NETWORK = new Failure(17002);
        public static final Failure CREDENTIALS = new Failure(1017);
        public static final Failure SID = new Failure(12505);
        public static final Failure TIMEOUT = new Failure(0);
        
        public final int code;
        
        private Failure(int ec)
        {
            ORACLE_ERRORCODES.put(code = ec, this);
        }
        
        public static Failure UNKNOWN(int ec) { return new Failure(ec); }
    }
    
    private static Failure GetFailure(int ec)
    {
        return ORACLE_ERRORCODES.containsKey(ec) ? 
                ORACLE_ERRORCODES.get(ec) : Failure.UNKNOWN(ec);
    }
    
    private static final List<String> QUERIES = Arrays.asList(new String[] {"SELECT"});
    private static final List<String> UPDATES = Arrays.asList(new String[] {"UPDATE", "INSERT", "DELETE", "DROP", "CREATE"});
    
    static
    {
        WRAPPERS.put(BigDecimal.class, Number.class);
        WRAPPERS.put(String.class, Varchar.class);
        WRAPPERS.put(java.sql.Timestamp.class, Date.class);
        WRAPPERS.put(oracle.sql.TIMESTAMP.class, Timestamp.class);
    }
}
