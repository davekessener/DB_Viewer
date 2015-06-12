package viewer.tools.connection;

import viewer.exception.ConnectionFailureException;
import viewer.exception.URLException;
import viewer.literals.URL;
import viewer.literals.language.Literals;
import viewer.literals.language.Strings;
import viewer.service.connection.ConnectionService;
import viewer.service.connection.VoidTask;
import viewer.tools.ui.Alert;
import javafx.scene.Node;

public class ConnectionManager
{
    private ManagerUI ui_;
    private ConnectUI connectUI_;
    private ConnectedUI connectedUI_;
    private ConnectionService service_;

    public ConnectionManager(ConnectionService service)
    {
        ui_ = new ManagerUI();
        connectUI_ = new ConnectUI();
        connectedUI_ = new ConnectedUI();
        service_ = service;
        
        ui_.setContent(connectUI_.getUI());

        registerHandlers();
    }
    
    public Node getUI()
    {
        return ui_.getUI();
    }

    private void test()
    {
        indicatorActivity(Strings.S_ESTABLISH_TEST_INITIALIZATION, Strings.C_DEFAULT, () -> doTestConnection());
    }

    private void connect()
    {
        indicatorActivity(Strings.S_ESTABLISH_TEST_CONNECT, Strings.C_DEFAULT, () -> doConnect());
    }

    private void disconnect()
    {
    }

    private void registerHandlers()
    {
        connectUI_.registerTest(e -> test());
        connectUI_.registerConnect(e -> connect());
        connectedUI_.registerDisconnect(e -> disconnect());
    }
    
    // # ---------------------------------------------------------------------------
    
    private void indicatorActivity(String s, String c, VoidTask t)
    {
        ui_.doSetColor(c);
        ui_.doSetInfo(s);
        ui_.doSetEnabled(false);
        service_.register(() -> doConnectionActivity(t));
    }
    
    private void doConnectionActivity(VoidTask t)
    {
        try
        {
            t.execute();
        }
        catch(ConnectionFailureException | URLException e)
        {
            ui_.setColor(Strings.C_FAILURE);
            ui_.setInfo(e.getMessage());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ui_.alert(Alert.AlertType.ERROR, "Exception", e.getLocalizedMessage());
        }

        ui_.setEnabled(true);
    }
    
    // TODO Indicator Interface + Connect/Connected Tool (/w onConnect & onDisconnect)
    
    private void doTestConnection() throws ConnectionFailureException, URLException
    {
        if(service_.doTestConnection(getURL(), connectUI_.getUser(), connectUI_.getPassword()))
        {
            ui_.setColor(Strings.C_SUCCESS);
            ui_.setInfo(Strings.S_ESTABLISH_TEST_SUCCESS);
        }
        else
        {
            ui_.setColor(Strings.C_FAILURE);
            ui_.setInfo(Strings.S_ESTABLISH_TEST_FAILURE);
        }
    }
    
    private void doConnect() throws ConnectionFailureException, URLException
    {
        String name = connectUI_.getName();
        String user = connectUI_.getUser(), password = connectUI_.getPassword();
        
        if(name.isEmpty()) name = Literals.Get(Strings.S_CONNECTION_DEFAULT);
        
        name = service_.doEstablishConnection(name, getURL(), user, password);
    }
    
    private URL getURL() throws URLException
    {
        return URL.Get(connectUI_.getServer(), connectUI_.getPort(), connectUI_.getSID());
    }
}
