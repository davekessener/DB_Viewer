package viewer.materials;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import viewer.exception.ConnectionFailureException;
import viewer.literals.Relation;
import viewer.literals.URL;

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
            throw new ConnectionFailureException(Failure.TIMEOUT);
        }
    }
    
    public synchronized void disconnect()
    {
        try
        {
            if(connected())
            {
                java.sql.Connection c = con_;

                con_ = null;
                c.close();
            }
        }
        catch(ConnectionFailureException | SQLException e) { }
    }
    
    public synchronized Relation query(String query) throws ConnectionFailureException
    {
        assert connected() : "Precondition violated: connected()";
        
        try
        {
            Statement s = con_.createStatement();
            
            s.setQueryTimeout(5);
            
            return GenerateRelation(s.executeQuery(query));
        }
        catch(SQLException e)
        {
            throw new ConnectionFailureException(Failure.TIMEOUT);
        }
    }
    
    private static Relation GenerateRelation(ResultSet r) throws SQLException
    {
        Relation.Factory f = Relation.GetFactory();
        
        ResultSetMetaData m = r.getMetaData();
        int l = m.getColumnCount();
        
        for(int i = 0 ; i < l ; ++i)
        {
            f.addColumn(m.getColumnName(i + 1));
        }
        
        while(r.next())
        {
            String[] row = new String[l];
            
            for(int i = 0 ; i < l ; ++i)
            {
                row[i] = r.getString(i + 1);
            }
            
            f.addRow(row);
        }

        r.close();
        
        return f.finish();
    }
    
    private static final Map<Integer, Failure> ORACLE_ERRORCODES = new HashMap<>();
    
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
}
