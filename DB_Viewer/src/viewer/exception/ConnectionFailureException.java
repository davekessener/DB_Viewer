package viewer.exception;

import java.util.HashMap;
import java.util.Map;

import viewer.literals.language.Literals;
import viewer.literals.language.Strings;
import viewer.materials.Connection;
import viewer.materials.Connection.Failure;

public class ConnectionFailureException extends ViewerException
{
    private static final long serialVersionUID = -5965219366905289836L;

    public ConnectionFailureException(Connection.Failure f)
    {
        super(GetString(f));
    }
    
    private static String GetString(Connection.Failure f)
    {
        if(FAILURES.containsKey(f))
        {
            return FAILURES.get(f);
        }
        else
        {
            return String.format("%s (%d)", Literals.Get(Strings.E_ESTABLISH_FAILURE_UNKNOWN), f.code);
        }
    }
    
    private static final Map<Connection.Failure, String> FAILURES;
    
    static
    {
        FAILURES = new HashMap<>();

        FAILURES.put(Failure.NETWORK, Strings.E_ESTABLISH_FAILURE_NETWORK);
        FAILURES.put(Failure.CREDENTIALS, Strings.E_ESTABLISH_FAILURE_CREDENTIALS);
        FAILURES.put(Failure.TIMEOUT, Strings.E_ESTABLISH_FAILURE_TIMEOUT);
        FAILURES.put(Failure.SID, Strings.E_ESTABLISH_FAILURE_SID);
    }
}
