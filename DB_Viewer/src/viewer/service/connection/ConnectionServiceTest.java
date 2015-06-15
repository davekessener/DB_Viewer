package viewer.service.connection;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import viewer.literals.Password;
import viewer.literals.URL;
import viewer.materials.Connection;
import viewer.materials.Relation;

public class ConnectionServiceTest
{
    @Test
    public void test()
    {
        try(ConnectionService service = new ConnectionService();)
        {
            String id = "HAW";
            URL haw = URL.Get("ora14.informatik.haw-hamburg.de", 1521, "inf14");
            String user = "abp403", password = Password.PASSWORD;
            
            assertTrue(service.doTestConnection(haw, user, password));
            
            id = service.establishConnection(id, haw, user, password).get();
            
            Future<String> f = service.request(id, (Connection c) -> 
            {
                Relation r = c.query("SELECT Nachname FROM Kunde WHERE Vorname = 'Daiki'");
                
//                String s = new java.sql.Timestamp(115, 5, 15, 0, 0, 0, 0).toString().split("\\.")[0];
//                String s = new TIMESTAMP(new java.sql.Timestamp(93, 11, 25, 0, 0, 0, 0)).toString();
                
//                c.modify("CREATE TABLE Types (anumber NUMBER, avarchar varchar2(40), adate DATE, atimestamp TIMESTAMP, PRIMARY KEY (anumber))");
//                c.modify("INSERT INTO Types values (1, 'astring', to_date('2015-06-15', 'YYYY-MM-DD'), to_timestamp('1993-12-25', 'YYYY-MM-DD'))");
//                r = c.query("SELECT avarchar FROM Types WHERE atimestamp = to_timestamp('" + s + "', 'YYYY-MM-DD HH24:MI:SS.FF')");

                return r.getRows().get(0).getValue(r.getColumns().get(0)).get();
            });
            
            do
            {
                System.out.print(".");
                try { Thread.sleep(10); } catch(InterruptedException e) {}
            } while(!f.isDone());
            
            String r = f.get();
            
            System.out.println("\n" + r);
        }
        catch(RuntimeException e)
        {
            e.printStackTrace(); throw e;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
