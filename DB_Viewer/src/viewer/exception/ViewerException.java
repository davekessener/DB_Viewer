package viewer.exception;

import viewer.literals.language.Literals;

public abstract class ViewerException extends Exception
{
    private static final long serialVersionUID = 0L;

    public ViewerException(String s)
    {
        super(s);
    }
    
    @Override
    public String getLocalizedMessage()
    {
        return Literals.Get(super.getLocalizedMessage());
    }
}
