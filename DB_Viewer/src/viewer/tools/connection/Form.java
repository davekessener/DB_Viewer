package viewer.tools.connection;

public class Form
{
    public final String NAME, SERVER, SID, USER, PASSWORD;
    public final int PORT;
    
    public Form(String name, String server, int port, String sid, String user, String password)
    {
        NAME = name;
        SERVER = server;
        PORT = port;
        SID = sid;
        USER = user;
        PASSWORD = password;
    }
}
