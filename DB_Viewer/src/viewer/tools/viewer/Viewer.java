package viewer.tools.viewer;

import java.util.HashMap;
import java.util.Map;

import viewer.service.connection.ConnectionService;
import viewer.tools.table.ConnectionManager;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

public class Viewer
{
    private ViewerUI ui_;
    private ConnectionService service_;
    private Map<ConnectionManager, Tab> tabs_;
    
    public Viewer(Stage primary, ConnectionService service)
    {
        ui_ = new ViewerUI(primary);
        service_ = service;
        tabs_ = new HashMap<>();
        
        registerHandlers();
    }
    
    public void run()
    {
        ui_.show();
    }
    
    private void registerHandlers()
    {
        ui_.registerNewPageHandler(tab -> openNewTab(tab));
    }
    
    private void openNewTab(Tab tab)
    {
        ConnectionManager m = new ConnectionManager(service_);
        
        tabs_.put(m, tab);
        
        tab.textProperty().bind(m.titleProperty());
        tab.setContent(m.getUI());
        tab.setOnCloseRequest(e -> m.close());
    }
}
