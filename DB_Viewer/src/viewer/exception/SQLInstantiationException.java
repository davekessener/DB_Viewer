package viewer.exception;

import viewer.literals.language.Strings;
import viewer.materials.sql.SQLObject;

public class SQLInstantiationException extends ViewerException
{
    private static final long serialVersionUID = 8100173792459051259L;
    
    private Class<? extends SQLObject> type_;
    private String value_;

    public SQLInstantiationException(Class<? extends SQLObject> t, String s)
    {
        super(Strings.ERROR_INSTANTIATION);
        type_ = t;
        value_ = s;
    }
    
    @Override
    public String getLocalizedMessage()
    {
        return String.format(super.getLocalizedMessage(), value_, type_.getName());
    }
}
