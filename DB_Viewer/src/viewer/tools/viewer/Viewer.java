package viewer.tools.viewer;

import viewer.service.connection.ConnectionService;
import viewer.tools.connection.ConnectionManager;
import javafx.stage.Stage;

public class Viewer
{
    private ViewerUI ui_;
    private ConnectionService service_;
    
    public Viewer(Stage primary, ConnectionService service)
    {
        ui_ = new ViewerUI(primary);
        service_ = service;
        
        registerHandlers();
    }
    
    public void run()
    {
        ui_.show();
    }
    
    private void registerHandlers()
    {
        ui_.registerNewPageHandler(tab -> { tab.setContent((new ConnectionManager(service_)).getUI()); });
    }
}
