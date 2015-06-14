package viewer.tools.connection;

import javafx.scene.Node;
import viewer.exception.ConnectionFailureException;
import viewer.exception.URLException;
import viewer.literals.URL;
import viewer.literals.language.Literals;
import viewer.literals.language.Strings;
import viewer.service.connection.ConnectionService;
import viewer.service.connection.Future;
import viewer.service.connection.VoidTask;
import viewer.tools.ui.Alert;
import viewer.tools.ui.Indicator;

public class Connect
{
    private Indicator indicator_;
    private ConnectUI ui_;
    private OnConnected onConnect_;
    private OnCancel onCancel_;
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
        ui_.registerCancel(e -> cancel());
    }
    
    public void registerOnConnected(OnConnected handler)
    {
        assert onConnect_ == null : "Precondition violated: onConnect_ == null";
        
        onConnect_ = handler;
    }
    
    public void registerOnCancel(OnCancel handler)
    {
        assert onCancel_ == null : "Precondition violated: onCancel_ == null";
        
        onCancel_ = handler;
    }

    private void test()
    {
        Form input = ui_.getInput();
        
        indicatorActivity(Strings.S_INFO_CONNECTION_INITIALIZATION, Strings.C_DEFAULT);
        
        service_.request(() -> doConnectionTest(input)).onDone(f -> evaluateConnectionTest(f));
    }

    private void connect()
    {
        Form input = ui_.getInput();
        
        indicatorActivity(Strings.S_INFO_CONNECTION_CONNECT, Strings.C_DEFAULT);
        
        service_.request(() -> doConnect(input)).onDone(f -> evaluateConnect(f));
    }
    
    private void cancel()
    {
        onCancel_.act();
    }
    
    private void indicatorActivity(String s, String c)
    {
        indicator_.setColor(c);
        indicator_.setInfo(s);
        indicator_.setEnabled(false);
    }
    
    private void evaluate(VoidTask t)
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
            Alert.DisplayAlert(Alert.AlertType.ERROR, "Exception", null, e.getLocalizedMessage());
        }

        indicator_.setEnabled(true);
    }
    
    private void evaluateConnectionTest(Future<Boolean> f)
    {
        evaluate(() ->
        {
            if(f.get())
            {
                indicator_.setColor(Strings.C_SUCCESS);
                indicator_.setInfo(Strings.S_INFO_SUCCESS);
            }
            else
            {
                indicator_.setColor(Strings.C_FAILURE);
                indicator_.setInfo(Strings.S_INFO_FAILURE);
            }
        });
    }
    
    private boolean doConnectionTest(Form input) throws ConnectionFailureException, URLException
    {
        return service_.doTestConnection(GetURL(input), input.USER, input.PASSWORD);
    }
    
    private void evaluateConnect(Future<String> f)
    {
        evaluate(() ->
        {
            String id = f.get();
            
            indicator_.setColor(Strings.C_SUCCESS);
            indicator_.setInfo(Strings.S_INFO_CONNECTION_ESTABLISHED);
            indicator_.setEnabled(true);
            
            onConnect_.act(id);
        });
    }
    
    private String doConnect(Form input) throws ConnectionFailureException, URLException
    {
        assert onConnect_ != null : "Precondition violated: onConnect_ != null";

        String name = input.NAME;
        
        if(name.isEmpty() || name.equals(Literals.Get(Strings.S_NEWCONNECTION))) 
            name = Literals.Get(Strings.S_CONNECTION_DEFAULT);
        
        return service_.doEstablishConnection(name, GetURL(input), input.USER, input.PASSWORD);
    }
    
    private static URL GetURL(Form in) throws URLException
    {
        return URL.Get(in.SERVER, in.PORT, in.SID);
    }

    public static interface OnConnected { void act(String connection); }
    public static interface OnCancel { void act(); }
}
