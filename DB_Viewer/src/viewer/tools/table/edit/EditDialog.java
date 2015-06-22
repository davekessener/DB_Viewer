package viewer.tools.table.edit;

import java.util.ArrayList;
import java.util.List;

import viewer.exception.SQLInstantiationException;
import viewer.literals.language.Literals;
import viewer.literals.language.Resources;
import viewer.literals.language.Strings;
import viewer.materials.Connection;
import viewer.materials.Entry;
import viewer.materials.Relation;
import viewer.service.connection.ConnectionService;
import viewer.service.connection.Future;
import viewer.service.connection.Void;
import viewer.tools.table.Connected;
import viewer.tools.ui.Alert;
import viewer.tools.ui.Alert.AlertType;
import viewer.tools.ui.Indicator;
import viewer.tools.viewer.IndicatorUI;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditDialog
{
    private Stage stage_;
    private Indicator indicator_;
    private EditUI ui_;
    private Entry old_;
    private ConnectionService service_;
    private String connection_, table_;
    private Runnable onClose_;
    
    public EditDialog(ConnectionService service)
    {
        ui_ = new EditUI();
        indicator_ = new IndicatorUI();
        service_ = service;
        
        stage_ = createStage((Parent) indicator_.getUI());
        
        indicator_.setContent(ui_.getUI());
        
        registerHandlers();
    }
    
    public void show(String c, String s, Relation r, Entry e)
    {
        connection_ = c;
        table_ = s;
        ui_.load(r, old_ = e);
        stage_.centerOnScreen();
        stage_.showAndWait();
    }
    
    public void close()
    {
        stage_.close();
    }
    
    public void registerOnClose(Runnable r)
    {
        assert onClose_ == null : "Precondition violated: onClose_ == null";
        
        onClose_ = r;
    }
    
    private void updateAndClose()
    {
        assert onClose_ != null : "Precondition violated: onClose_ != null";
        
        try
        {
            update(old_, ui_.getEntry());
        }
        catch(SQLInstantiationException e)
        {
            Alert.DisplayAlert(AlertType.ERROR, Strings.ERROR_TITLE, null, e.getLocalizedMessage());
        }
    }
    
    private void registerHandlers()
    {
        ui_.registerOK(e -> updateAndClose());
        ui_.registerCancel(e -> close());
    }
    
    private Stage createStage(Parent ui)
    {
        Stage stage = new Stage();
        
        stage.setTitle(Literals.Get(Strings.UI_EDIT_TITLE_EDIT));
        stage.getIcons().add(Resources.GetImage(Resources.I_EDIT));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(ui));
        
        return stage;
    }

    private String insertInto(String table, Entry e)
    {
        assert e != null : "Vorbedingung verletzt: e != null";
        
        StringBuilder sb = new StringBuilder();
        List<String> vs = new ArrayList<>();
        
        for(String c : e.getColumns())
        {
            vs.add(e.getItem(c).toQueryString());
        }
        
        sb.append("INSERT INTO ").append(table).append(" values (");
        sb.append(String.join(", ", vs)).append(")");
        
        return sb.toString();
    }
    
    private String updateEntry(String table, Entry e1, Entry e2)
    {
        assert e1 != null : "Vorbedingung verletzt: e1 != null";
        assert e2 != null : "Vorbedingung verletzt: e2 != null";
        assert e1.getColumns().equals(e2.getColumns()) : "Precondition violated: e1.getColumns().equals(e2.getColumns())";
        
        StringBuilder sb = new StringBuilder();
        List<String> rs = new ArrayList<>();
        List<String> ts = new ArrayList<>();
        
        for(String c : e1.getColumns())
        {
            rs.add(c + " = " + e2.getItem(c).toQueryString());
            ts.add(c + " = " + e1.getItem(c).toQueryString());
        }
        
        sb.append("UPDATE ").append(table).append(" SET ");
        sb.append(String.join(", ", rs)).append(" WHERE ");
        sb.append(String.join(" AND ", ts));
        
        return sb.toString();
    }
    
    private void finish(Future<Void> f) throws Exception
    {
        f.get();
        
        close();
        
        onClose_.run();
    }
    
    private void update(Entry e1, Entry e2)
    {
        final String query = e1 == null ? insertInto(table_, e2) : updateEntry(table_, e1, e2);

        service_.request(connection_, (Connection c) -> c.execute(query)).onDone(f -> Connected.evaluate(() -> finish(f)));
    }
}
