package viewer.service.connection;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import viewer.literals.Password;
import viewer.literals.Relation;
import viewer.literals.URL;

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
            
            Future<String> f = service.register(id, c -> 
            {
                Relation r = c.query("SELECT Nachname FROM Kunde WHERE Vorname = 'Daiki'");
                
                return r.getRow(0).get(0);
            });
            
            do
            {
                System.out.print(".");
                try { Thread.sleep(10); } catch(InterruptedException e) {}
            } while(!f.isDone());
            
            String r = f.get();
            
            System.out.println("\n" + r);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
