package viewer.tools.viewer;

import java.util.HashMap;
import java.util.Map;

import viewer.literals.language.Literals;
import viewer.literals.language.Strings;
import viewer.service.connection.ConnectionService;
import viewer.tools.ObservableEvent;
import viewer.tools.Observer;
import viewer.tools.connection.ConnectionManager;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

public class Viewer implements Observer
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
        
        m.register(this);
        tab.setContent(m.getUI());
        tab.setOnCloseRequest(e -> m.close());
    }

    @Override
    public void onChange(ObservableEvent e)
    {
        ConnectionManager m = (ConnectionManager) e.get();
        Tab tab = tabs_.get(m);
        
        if(m.isConnected())
        {
            tab.setText(m.getConnectionID());
        }
        else
        {
            tab.setText(Literals.Get(Strings.S_NEWCONNECTION));
        }
    }
}
