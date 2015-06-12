package viewer.tools.connection;

import javafx.scene.Node;
import viewer.service.connection.ConnectionService;

public class ConnectionManager
{
    private ManagerUI ui_;
    private Connect connect_;
    private Connected connected_;

    public ConnectionManager(ConnectionService service)
    {
        ui_ = new ManagerUI();
        connect_ = new Connect(service, ui_);
        connected_ = new Connected(service, ui_);
        
        ui_.setContent(connect_.getUI());

        registerHandlers();
    }
    
    private void connect(String id)
    {
        ui_.setContent(connected_.getUI());
        connected_.init(id);
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
