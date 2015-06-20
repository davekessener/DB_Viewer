package viewer.materials.sql;

import java.sql.Timestamp;
import java.time.Instant;

import viewer.exception.SQLInstantiationException;

public class Date extends SQLObject
{
    private Timestamp raw_;
    
    protected Date() { raw_ = Timestamp.from(Instant.now()); }
    protected Date(Timestamp t) { raw_ = t; }
    
    @Override
    public String toString()
    {
        return raw_.toString().split(" ")[0];
    }
    
    @Override
    public String toQueryString()
    {
        return "to_date('" + toString() + "', 'YYYY-MM-DD')";
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(o == this) return true;
        else if(o instanceof Date)
        {
            return toString().equals(o.toString());
        }
        
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return toString().hashCode();
    }
    
    public static Date Instantiate()
    {
        return new Date();
    }
    
    public static Date ValueOf(String s) throws SQLInstantiationException
    {
        assert s != null : "Vorbedingung verletzt: s != null";
        
        try
        {
            return new Date(Timestamp.valueOf(s + " 00:00:00"));
        }
        catch(IllegalArgumentException e)
        {
            throw new SQLInstantiationException(Date.class, s);
        }
    }
}
