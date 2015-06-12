package viewer.tools.connection;

import javafx.application.Platform;
import javafx.scene.Node;
import viewer.exception.ConnectionFailureException;
import viewer.exception.URLException;
import viewer.literals.URL;
import viewer.literals.language.Literals;
import viewer.literals.language.Strings;
import viewer.service.connection.ConnectionService;
import viewer.service.connection.VoidTask;
import viewer.tools.ui.Alert;
import viewer.tools.ui.Indicator;

public class Connect
{
    private Indicator indicator_;
    private ConnectUI ui_;
    private OnConnected onConnect_;
    private ConnectionService service_;
    
    public Connect(ConnectionService service, Indicator indicator)
    {
        service_ = service;
        indicator_ = indicator;
        ui_ = new ConnectUI();
        
        registerHandlers();
    }
    
    public Node getUI()
    {
        return ui_.getUI();
    }

    private void registerHandlers()
    {
        ui_.registerTest(e -> test());
        ui_.registerConnect(e -> connect());
    }
    
    public void registerOnConnected(OnConnected handler)
    {
        assert onConnect_ == null : "Precondition violated: onConnect_ == null";
        
        onConnect_ = handler;
    }

    private void test()
    {
        indicatorActivity(Strings.S_INFO_CONNECTION_INITIALIZATION, Strings.C_DEFAULT, () -> doTestConnection());
    }

    private void connect()
    {
        indicatorActivity(Strings.S_INFO_CONNECTION_CONNECT, Strings.C_DEFAULT, () -> doConnect());
    }
    
    private void indicatorActivity(String s, String c, VoidTask t)
    {
        indicator_.setColor(c);
        indicator_.setInfo(s);
        indicator_.setEnabled(false);
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
            indicator_.setColor(Strings.C_FAILURE);
            indicator_.setInfo(e.getMessage());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            indicator_.alert(Alert.AlertType.ERROR, "Exception", e.getLocalizedMessage());
        }

        indicator_.setEnabled(true);
    }
    
    private void doTestConnection() throws ConnectionFailureException, URLException
    {
        if(service_.doTestConnection(getURL(), ui_.getUser(), ui_.getPassword()))
        {
            indicator_.setColor(Strings.C_SUCCESS);
            indicator_.setInfo(Strings.S_INFO_SUCCESS);
        }
        else
        {
            indicator_.setColor(Strings.C_FAILURE);
            indicator_.setInfo(Strings.S_INFO_FAILURE);
        }
    }
    
    private void doConnect() throws ConnectionFailureException, URLException
    {
        assert onConnect_ != null : "Precondition violated: onConnect_ != null";

        String name = ui_.getName();
        String user = ui_.getUser(), password = ui_.getPassword();
        
        if(name.isEmpty()) name = Literals.Get(Strings.S_CONNECTION_DEFAULT);
        
        final String id = service_.doEstablishConnection(name, getURL(), user, password);
        
        indicator_.setColor(Strings.C_SUCCESS);
        indicator_.setInfo(Strings.S_INFO_CONNECTION_ESTABLISHED);
        indicator_.setEnabled(true);
        
        Platform.runLater(() -> onConnect_.act(id));
    }
    
    private URL getURL() throws URLException
    {
        return URL.Get(ui_.getServer(), ui_.getPort(), ui_.getSID());
    }

    public static interface OnConnected
    {
        void act(String connection);
    }
}
