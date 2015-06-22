import java.util.Scanner;

import viewer.exception.ConnectionFailureException;
import viewer.exception.URLException;
import viewer.literals.Password;
import viewer.literals.URL;
import viewer.materials.Connection;
import viewer.materials.Entry;
import viewer.materials.Relation;
import viewer.service.Logger;


public class Interpreter
{
    public static void main(String[] args) throws Exception
    {
        try(Scanner in = new Scanner(System.in))
        {
            String query;
            
            Logger.setEnabled(false);
            
            puts("Connecting ... ");
            
            try(Connection c = getConnection())
            {
                puts("[DONE]\n");
                
                while(in.hasNextLine())
                {
                    query = process(in.nextLine());
                    
                    if(query == null)
                    {
                        puts("Invalid command.\n");
                        continue;
                    }
                    else if(query.equals("QUIT") || query.equals("EXIT"))
                    {
                        puts("Bye.\n");
                        break;
                    }
                    
                    try
                    {
                        if(query.startsWith("SELECT"))
                        {
                            printRelation(c.query(query));
                        }
                        else
                        {
                            puts("%d rows affected.\n", c.modify(query));
                        }
                    }
                    catch(ConnectionFailureException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    private static void printRelation(Relation r)
    {
        int[] sz = new int[r.getColCount()];
        int i = 0;
        StringBuilder sb = new StringBuilder();
        String sep = null;
        
        for(String c : r.getColumns())
        {
            sz[i] = c.length();
            
            for(Entry e : r.getRows())
            {
                int l = e.getItem(c).toString().length();
                
                if(l > sz[i]) sz[i] = l;
            }
            
            sz[i] += 1;
            
            String s = String.format(" |%" + sz[i] + "s", c);
            
            puts(s);
            
            sb.append("-+").append(s.replaceAll(".", "-").substring(2));
            
            ++i;
        }
        
        sep = sb.append("--").toString();
        
        puts("\n" + sep.replace('-', '=') + "\n");
        
        for(Entry e : r.getRows())
        {
            i = 0;
            for(String c : r.getColumns())
            {
                puts(" |%" + sz[i] + "s", e.getItem(c).toString()); ++i;
            }
            
            puts("\n");
        }
        
        puts(sep + "\n");
    }
    
    private static Connection getConnection() throws URLException, ConnectionFailureException
    {
        URL url = URL.Get(SERVER, PORT, SID);
        
        return new Connection(url, USER, PASSWORD);
    }
    
    private static void puts(String s, Object ... o) { puts(String.format(s, o)); }
    private static void puts(String s)
    {
        System.out.print(s);
        System.out.flush();
    }
    
    private static String process(String s)
    {
        if(s == null || s.isEmpty()) return null;
        
        StringBuilder sb = new StringBuilder();
        boolean q = false, e = false;
        
        for(char c : s.toCharArray())
        {
            if(e)
            {
                switch(c)
                {
                    case '\'': sb.append('\''); break;
                    case 'n': sb.append('\n'); break;
                    case 't': sb.append('\t'); break;
                    case '\\': sb.append('\\'); break;
                    default: return null;
                }
                
                e = false;
            }
            else if(c == '\'')
            {
                q = !q;
                sb.append('\'');
            }
            else if(c == '\\')
            {
                e = true;
            }
            else
            {
                sb.append(!q ? Character.toUpperCase(c) : c);
            }
        }
        
        if(e || q) return null;
        
        return sb.toString().trim();
    }
    
    private static final String SERVER = "ora14.informatik.haw-hamburg.de";
    private static final int PORT = 1521;
    private static final String SID = "inf14";
    private static final String USER = "abp403";
    private static final String PASSWORD = Password.PASSWORD;
}
