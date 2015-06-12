package viewer.tools.connection;

import javafx.scene.Node;
import viewer.literals.language.Strings;
import viewer.service.connection.ConnectionService;
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
        indicator_.setInfo(Strings.S_CONNECTION_DISCONNECTED);
        
        disconnect_.act();
    }
    
    public static interface OnDisconnect
    {
        void act();
    }
}
