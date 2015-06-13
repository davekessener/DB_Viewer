package viewer.tools.connection;

import javafx.scene.Node;
import viewer.service.connection.ConnectionService;
import viewer.tools.AbstractObservable;
import viewer.tools.Observable;

public class ConnectionManager extends AbstractObservable
implements Observable
{
    private String connectionID_;
    private ManagerUI ui_;
    private Connect connect_;
    private Connected connected_;
    private ConnectionService service_;

    public ConnectionManager(ConnectionService service)
    {
        ui_ = new ManagerUI();
        connect_ = new Connect(service, ui_);
        connected_ = new Connected(service, ui_);
        service_ = service;
        connectionID_ = "";
        
        ui_.setContent(connect_.getUI());

        registerHandlers();
    }
    
    public boolean isConnected() { return !connectionID_.isEmpty(); }
    public String getConnectionID() { return connectionID_; }
    
    public void close()
    {
        if(isConnected())
        {
            service_.closeConnection(connectionID_);
            connectionID_ = "";
        }
    }
    
    private void connect(String id)
    {
        connectionID_ = id;
        ui_.setContent(connected_.getUI());
        connected_.init(id);
        change();
    }
    
    private void registerHandlers()
    {
        connect_.registerOnConnected(id -> connect(id));
    }

    public Node getUI()
    {
        return ui_.getUI();
    }
}
