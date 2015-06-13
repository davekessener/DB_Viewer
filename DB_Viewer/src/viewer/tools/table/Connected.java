package viewer.tools.table;

import javafx.scene.Node;
import viewer.exception.ConnectionFailureException;
import viewer.literals.Relation;
import viewer.literals.language.Strings;
import viewer.materials.Connection;
import viewer.service.connection.ConnectionService;
import viewer.service.connection.Future;
import viewer.tools.ui.Alert;
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
        
        service_.request(id, (Connection c) -> doLoadRelation(c)).onDone(f -> evaluateLoadedRelation(f));
    }
    
    private void evaluateLoadedRelation(Future<Relation> f)
    {
        try
        {
            ui_.loadRelation(f.get());
            indicator_.setInfo("");
            indicator_.setEnabled(true);
        }
        catch(ConnectionFailureException e)
        {
            e.printStackTrace();
            indicator_.alert(AlertType.ERROR, "Failure", "Query failed.");
        }
        catch(RuntimeException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Alert.ShowExceptionError("Unknown Exception", null, e);
        }
    }
    
    private Relation doLoadRelation(Connection c) throws ConnectionFailureException
    {
        return c.query("SELECT * FROM Produkt");
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
