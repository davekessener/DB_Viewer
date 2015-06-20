package viewer.materials.sql;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import oracle.sql.TIMESTAMP;
import viewer.exception.SQLInstantiationException;

public abstract class SQLObject
{
    public abstract String toQueryString();
    
    public static SQLObject Instantiate(Class<? extends SQLObject> c)
    {
        try
        {
            return c.newInstance();
        }
        catch(InstantiationException | IllegalAccessException e)
        {
            throw new Error(e);
        }
    }
    
    public static SQLObject ValueOf(Class<? extends SQLObject> c, String s) throws SQLInstantiationException
    {
        try
        {
            return (SQLObject) c.getMethod("ValueOf", String.class).invoke(null, s);
        }
        catch(InvocationTargetException e)
        {
            throw (SQLInstantiationException) e.getCause();
        }
        catch(IllegalAccessException
                | IllegalArgumentException
                | NoSuchMethodException
                | SecurityException e)
        {
            throw new Error(e);
        }
    }
    
    public static SQLObject Generate(Object o)
    {
        return GENERATORS.get(o.getClass()).generate(o);
    }
    
    private static interface Generator { SQLObject generate(Object o); }
    private static Map<Class<?>, Generator> GENERATORS = new HashMap<>();
    
    static
    {
        GENERATORS.put(BigDecimal.class, o -> new Number((BigDecimal) o));
        GENERATORS.put(String.class, o -> new Varchar((String) o));
        GENERATORS.put(java.sql.Timestamp.class, o -> new Date((java.sql.Timestamp) o));
        GENERATORS.put(TIMESTAMP.class, o -> new Timestamp((TIMESTAMP) o));
    }
}
