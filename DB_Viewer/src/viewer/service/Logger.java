package viewer.service;

import java.util.logging.Level;

public class Logger
{
    private static boolean enabled = true;
    
    public static void Log(String s, Object...o) { Log(String.format(s, o)); }
    public static void Log(String s)
    {
        if(enabled)
        {
            java.util.logging.Logger.getAnonymousLogger().log(Level.INFO, s);
        }
    }
    
    public static void setEnabled(boolean f) { enabled = f; }
}
