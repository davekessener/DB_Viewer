package viewer.service;

public class Logger
{
    public static void Log(String s, Object...o) { Log(String.format(s, o)); }
    public static void Log(String s)
    {
        System.out.println(s);
    }
}
