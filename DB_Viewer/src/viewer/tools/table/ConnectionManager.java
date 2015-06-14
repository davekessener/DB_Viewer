package viewer.tools.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import viewer.literals.language.Literals;
import viewer.literals.language.Strings;
import viewer.service.connection.ConnectionService;
import viewer.tools.connection.ConnectDialog;
import viewer.tools.viewer.IndicatorDialog;

public class ConnectionManager
{
    private IndicatorDialog ui_;
    private EstablishUI establish_;
    private Connected connected_;
    private ConnectionService service_;
    private ConnectDialog connect_;
    private StringProperty title_;

    public ConnectionManager(ConnectionService service)
    {
        service_ = service;
        ui_ = new IndicatorDialog();
        connected_ = new Connected(service_, ui_);
        establish_ = new EstablishUI();
        connect_ = new ConnectDialog(service_);
        title_ = new SimpleStringProperty();
        
        title_.set(DefaultTitle());
        ui_.setContent(establish_.getUI());

        registerHandlers();
    }
    
    public StringProperty titleProperty() { return title_; }
    
    public void close()
    {
        if(service_.knowsConnection(title_.get()))
        {
            disconnect();
        }
        
        ui_.close();
    }
    
    private void connect(String id)
    {
        title_.set(id);
        ui_.setContent(connected_.getUI());
        connected_.init(id);
    }
    
    private void disconnect()
    {
        service_.closeConnection(title_.get());
        title_.set(DefaultTitle());
        ui_.setContent(establish_.getUI());
    }
    
    private void openDialog()
    {
        connect_.run();
    }
    
    private void registerHandlers()
    {
        establish_.registerOnClick(e -> openDialog());
        connect_.registerOnConnect(id -> connect(id));
        connected_.registerOnDisconnect(() -> disconnect());
    }

    public Node getUI()
    {
        return ui_.getUI();
    }
    
    private static String DefaultTitle()
    {
        return Literals.Get(Strings.S_NEWCONNECTION);
    }
}
