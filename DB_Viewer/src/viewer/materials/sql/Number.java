package viewer.materials.sql;

import java.math.BigDecimal;

import viewer.exception.SQLInstantiationException;

public class Number extends SQLObject
{
    private BigDecimal raw_;
    
    protected Number() { raw_ = BigDecimal.ZERO; }
    protected Number(BigDecimal v) { raw_ = v; }
    
    @Override
    public String toString()
    {
        return raw_.toString();
    }
    
    @Override
    public String toQueryString()
    {
        return raw_.toPlainString();
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
    
    public static Number Instantiate()
    {
        return new Number();
    }
    
    public static Number ValueOf(String s) throws SQLInstantiationException
    {
        assert s != null : "Vorbedingung verletzt: s != null";
        
        try
        {
            return new Number(new BigDecimal(s));
        }
        catch(NumberFormatException e)
        {
            throw new SQLInstantiationException(Number.class, s);
        }
    }
}
