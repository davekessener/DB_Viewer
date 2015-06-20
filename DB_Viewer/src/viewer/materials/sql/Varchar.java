package viewer.materials.sql;

import viewer.exception.SQLInstantiationException;

public class Varchar extends SQLObject
{
    private String raw_;
    
    protected Varchar() { raw_ = " "; }
    protected Varchar(String s) { raw_ = s; }
    
    @Override
    public String toString()
    {
        return raw_;
    }
    
    @Override
    public String toQueryString()
    {
        return "'" + raw_ + "'";
    }
    
    @Override
    public boolean equals(Object o)
    {
        return raw_.equals(o) || (raw_.isEmpty() && o == null);
    }
    
    @Override
    public int hashCode()
    {
        return raw_.hashCode();
    }
    
    public static Varchar Instantiate()
    {
        return new Varchar();
    }
    
    public static Varchar ValueOf(String s) throws SQLInstantiationException
    {
        assert s != null : "Vorbedingung verletzt: s != null";
        
        if(s.isEmpty()) throw new SQLInstantiationException(Varchar.class, s);
        
        return new Varchar(s);
    }
}
