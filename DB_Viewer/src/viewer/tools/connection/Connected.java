package viewer.tools.connection;

import javafx.application.Platform;
import javafx.scene.Node;
import viewer.exception.ConnectionFailureException;
import viewer.literals.Relation;
import viewer.literals.language.Strings;
import viewer.materials.Connection;
import viewer.service.connection.ConnectionService;
import viewer.tools.ui.Alert.AlertType;
import viewer.tools.ui.Indicator;

public class Connected
{
    private ConnectedUI ui_;
    private Indicator indicator_;
    private ConnectionService service_;
    private OnDisconnect disconnect_;
    
    public Connected(ConnectionService service, Indicator indicator)
    {
        this.service_ = service;
        this.indicator_ = indicator;
        
        ui_ = new ConnectedUI();
        
        registerHandlers();
    }
    
    private void registerHandlers()
    {
        ui_.registerDisconnect(e -> disconnect());
    }
    
    public Node getUI()
    {
        return ui_.getUI();
    }
    
    public void init(String id)
    {
        indicator_.setColor(Strings.C_DEFAULT);
        indicator_.setInfo(Strings.S_INFO_CONNECTION_LOADING);
        indicator_.setEnabled(false);
        
        service_.register(id, (Connection c) -> Platform.runLater(() -> display(c)));
    }
    
    private void display(Connection c)
    {
        try
        {
            Relation r = c.query("SELECT * FROM Kunde");
            
        }
        catch(ConnectionFailureException e)
        {
            e.printStackTrace();
            indicator_.alert(AlertType.ERROR, "Failure", "Query failed.");
        }
    }
    
    public void registerOnDisconnect(OnDisconnect h)
    {
        assert disconnect_ == null : "Precondition violated: disconnect_ == null";
        
        disconnect_ = h;
    }
    
    private void disconnect()
    {
        assert disconnect_ != null : "Vorbedingung verletzt: disconnect_ != null";
        
        indicator_.setColor(Strings.C_DEFAULT);
        indicator_.setInfo(Strings.S_INFO_CONNECTION_DISCONNECTED);
        
        disconnect_.act();
    }
    
    public static interface OnDisconnect
    {
        void act();
    }
}
