package viewer.literals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import viewer.exception.URLException;
import viewer.literals.language.Strings;

public class URL
{
    private final String url_;
    private final String server_;
    private final int port_;
    private final String sid_;
    
    private URL(String server, int port, String sid)
    {
        this.server_ = server;
        this.port_ = port;
        this.sid_ = sid;
        this.url_ = String.format(FORMAT, server, port, sid);
    }
    
    public String getServer() { return server_; }
    public int getPort() { return port_; }
    public String getsid() { return sid_; }
    
    @Override
    public String toString()
    {
        return url_;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if(o == null) return false;
        else if(o == this) return true;
        else if(o instanceof URL)
        {
            return url_.equals(o.toString());
        }
        
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return url_.hashCode();
    }
    
    public static URL Get(String server, int port, String sid) throws URLException
    {
        assert sid != null : "Vorbedingung verletzt: sid != null";
        assert server != null : "Vorbedingung verletzt: server != null";
        
        if(server.isEmpty() || sid.isEmpty())
            throw new URLException(Strings.E_URL_EMPTY);
        
        if(port <= 0) throw new URLException(Strings.E_URL_PORT);
        
        server = urlify(server.toLowerCase());
        
        return new URL(server, port, sid);
    }
    
    private static String urlify(String s) throws URLException
    {
        Matcher m = URLP.matcher(s);
        if(m.matches()) s = m.group(1);
        
        if(s.contains(":") || s.contains("/") || s.startsWith(".") || s.endsWith("."))
            throw new URLException(Strings.E_URL_MALFORMED);

        return s;
    }
    
    private static final String FORMAT = "jdbc:oracle:thin:@%s:%d:%s";
    private static final Pattern URLP = Pattern.compile("[a-zA-Z]+://([^:/]+)([:/].*)?");
}
