package viewer.tools.table;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import viewer.exception.ConnectionFailureException;
import viewer.literals.language.Strings;
import viewer.materials.Connection;
import viewer.materials.Entry;
import viewer.materials.Pair;
import viewer.materials.Relation;
import viewer.service.connection.ConnectionService;
import viewer.service.connection.Future;
import viewer.service.connection.VoidTask;
import viewer.service.connection.Void;
import viewer.tools.ui.Alert;
import viewer.tools.ui.Alert.AlertType;
import viewer.tools.ui.Indicator;

public class Connected
{
    private ConnectedUI ui_;
    private Indicator indicator_;
    private ConnectionService service_;
    private OnDisconnect disconnect_;
    private TableManager tables_;
    private String connection_;
    
    public Connected(ConnectionService service, Indicator indicator)
    {
        this.service_ = service;
        this.indicator_ = indicator;
        this.tables_ = null;
        
        ui_ = new ConnectedUI();
        
        registerHandlers();
    }

    public Node getUI()
    {
        return ui_.getUI();
    }
    
    public void registerOnDisconnect(OnDisconnect h)
    {
        assert disconnect_ == null : "Precondition violated: disconnect_ == null";
        
        disconnect_ = h;
    }
    
    // # ----------------------------------------------------------------------

    private void add()
    {
    }
    
    private void remove(List<Entry> rows)
    {
        List<String> queries = new ArrayList<String>();
        String table = ui_.getSelected();
        
        for(Entry e : rows)
        {
            String t = tables_.getTable(e);
            List<String> fs = new ArrayList<String>();
            
            for(String k : e.getColumns())
            {
                fs.add(k + " = " + Connection.FormatElement(e.getItem(k), e.getType(k)));
            }
            
            queries.add("DELETE FROM " + t + " WHERE " + String.join(" AND ", fs));
        }
        
        Alert.DisplayYesNoDialog("Cornifm", "rly dlete?", r ->
        {
            if(r == Alert.Response.YES)
            {
                service_.request(connection_, (Connection c) -> c.executeAll(queries)).onDone(f -> evaluateDelete(f, table));
            }
        });
    }
    
    private void disconnect()
    {
        assert disconnect_ != null : "Vorbedingung verletzt: disconnect_ != null";
        
        disconnect_.act();
    }
    
    private void select(String s)
    {
        indicator_.setColor(Strings.C_DEFAULT);
        indicator_.setInfo(Strings.S_INFO_CONNECTION_LOADING);
        indicator_.setEnabled(false);
        
        service_.request(connection_, (Connection c) -> doLoadTable(c, s)).onDone(f -> evaluateTable(f));
    }
    
    private void registerHandlers()
    {
        ui_.registerAdd(e -> add());
        ui_.registerRemove(e -> remove(e));
        ui_.registerDisconnect(e -> disconnect());
        ui_.registerSelect(s -> select(s));
    }
    
    // # ----------------------------------------------------------------------
    
    public void init(String id)
    {
        indicator_.setColor(Strings.C_DEFAULT);
        indicator_.setInfo(Strings.S_INFO_CONNECTION_LOADING);
        indicator_.setEnabled(false);

        tables_ = null;
        
        ui_.clear();
        
        connection_ = id;
        
        service_.request(id, (Connection c) -> doLoadRelationTable(c)).onDone(f -> evaluateRelationTable(f));
    }
    
    private void evaluate(VoidTask t)
    {
        try
        {
            t.execute();
        }
        catch(ConnectionFailureException e)
        {
            e.printStackTrace();
            Alert.DisplayAlert(AlertType.ERROR, "Failure", null, "Query failed.");
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

    // # ----------------------------------------------------------------------
    
    private void evaluateDelete(Future<Void> f, String table)
    {
        evaluate(() ->
        {
            f.get();
            select(table);
        });
    }
    
    // # ----------------------------------------------------------------------
    
    private void evaluateTable(Future<Pair<String, Relation>> f)
    {
        evaluate(() ->
        {
            String id = f.get().first;
            Relation r = f.get().second;
            
            tables_.updateTable(id, r);
            
            ui_.load(tables_.getColumns(id), tables_.getRows(id));
            
            indicator_.setInfo(null);
            indicator_.setEnabled(true);
        });
    }
    
    private Pair<String, Relation> doLoadTable(Connection c, String id) throws ConnectionFailureException
    {
        return new Pair<>(id, c.query("SELECT * FROM " + id));
    }

    // # ----------------------------------------------------------------------
    
    private void evaluateRelationTable(Future<Relation> f)
    {
        evaluate(() ->
        {
            List<String> tbllist = new ArrayList<>();
            Relation tables = f.get();
            String id = tables.getColumns().get(0);
            
            for(Entry r : tables)
            {
                tbllist.add(r.getValue(id).get());
            }
            
            tables_ = new TableManager(tbllist);
            
            ui_.setSelection(tables_.getNames());
            
            indicator_.setInfo(null);
            indicator_.setEnabled(true); 
        });
    }
    
    private Relation doLoadRelationTable(Connection c) throws ConnectionFailureException
    {
        return c.query("SELECT TABLE_NAME FROM USER_TABLES");
    }

    // # ----------------------------------------------------------------------
    
    public static interface OnDisconnect { void act(); }
}
