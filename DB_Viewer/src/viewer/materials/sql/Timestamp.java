package viewer.materials.sql;

import java.time.Instant;

import oracle.sql.TIMESTAMP;
import viewer.exception.SQLInstantiationException;

public class Timestamp extends SQLObject
{
    private TIMESTAMP raw_;
    
    protected Timestamp() { raw_ = new TIMESTAMP(TIMESTAMP.toBytes(java.sql.Timestamp.from(Instant.now()))); }
    protected Timestamp(TIMESTAMP t) { raw_ = t; }
    
    @Override
    public String toString()
    {
        return raw_.toString();
    }
    
    @Override
    public String toQueryString()
    {
        return "to_timestamp('" + toString() + "', 'YYYY-MM-DD HH24:MI:SS.FF')";
    }
    
    @Override
    public boolean equals(Object o)
    {
        return raw_.equals(o);
    }
    
    @Override
    public int hashCode()
    {
        return raw_.hashCode();
    }
    
    public static Timestamp Instantiate()
    {
        return new Timestamp();
    }
    
    public static Timestamp ValueOf(String s) throws SQLInstantiationException
    {
        try
        {
            return new Timestamp(new TIMESTAMP(TIMESTAMP.toBytes(java.sql.Timestamp.valueOf(s))));
        }
        catch(IllegalArgumentException e)
        {
            throw new SQLInstantiationException(Timestamp.class, s);
        }
    }
}
