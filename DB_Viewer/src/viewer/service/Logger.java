package viewer.service;

import java.util.logging.Level;

public class Logger
{
    public static void Log(String s, Object...o) { Log(String.format(s, o)); }
    public static void Log(String s)
    {
        java.util.logging.Logger.getAnonymousLogger().log(Level.INFO, s);
    }
}
