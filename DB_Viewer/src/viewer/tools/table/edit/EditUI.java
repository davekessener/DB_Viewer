package viewer.tools.table.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import viewer.exception.SQLInstantiationException;
import viewer.literals.language.Literals;
import viewer.literals.language.Strings;
import viewer.materials.Entry;
import viewer.materials.Relation;
import viewer.materials.sql.SQLObject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;

public class EditUI
{
    private Node pane_;
    private TableView<Attribute> table_;
    private Button ok_, cancel_;
    
    public EditUI()
    {
        pane_ = createUI();
    }
    
    public Node getUI()
    {
        return pane_;
    }
    
    public void registerOK(EventHandler<ActionEvent> h)
    {
        ok_.setOnAction(h);
    }
    
    public void registerCancel(EventHandler<ActionEvent> h)
    {
        cancel_.setOnAction(h);
    }
    
    public void load(Relation r, Entry e)
    {
        table_.getItems().clear();
        
        for(String c : r.getColumns())
        {
            table_.getItems().add(new Attribute(c, e == null ? SQLObject.Instantiate(r.getType(c)) : e.getItem(c)));
        }
    }
    
    public Entry getEntry() throws SQLInstantiationException
    {
        List<String> cs = new ArrayList<>();
        List<SQLObject> vs = new ArrayList<>();
        Map<String, Class<? extends SQLObject>> ts = new HashMap<>();
        
        for(Attribute a : table_.getItems())
        {
            String n = a.getName();
            cs.add(n);
            vs.add(SQLObject.ValueOf(a.getType(), a.getValue()));
            ts.put(n, a.getType());
        }
        
        return new Entry(cs, ts, vs);
    }
    
    // # ======================================================================
    
    private Node createUI()
    {
        ScrollPane pane = new ScrollPane();
        
        pane.setFitToWidth(true);
        pane.setFitToHeight(true);
        
        pane.setContent(createContent());
        
        return pane;
    }
    
    private Node createContent()
    {
        BorderPane pane = new BorderPane();
        
        pane.setPadding(new Insets(15, 10, 10, 10));
        
        pane.setCenter(createTable());
        pane.setBottom(createButtons());
        
        return pane;
    }
    
    private Node createTable()
    {
        table_ = new TableView<>();

        TableColumn<Attribute, String> nameCol = new TableColumn<>(Literals.Get(Strings.UI_EDIT_COLUMN));
        TableColumn<Attribute, String> valueCol = new TableColumn<>(Literals.Get(Strings.UI_EDIT_VALUE));
        
        nameCol.setCellValueFactory(p -> p.getValue().nameProperty());
        valueCol.setCellValueFactory(p -> p.getValue().valueProperty());

        valueCol.setCellFactory(p -> new EditCell<Attribute>());
        
        nameCol.setSortable(false);
        valueCol.setSortable(false);
        
        table_.getColumns().add(nameCol);
        table_.getColumns().add(valueCol);
        
        table_.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table_.setEditable(true);
        
        return table_;
    }
    
    private Node createButtons()
    {
        TilePane pane = new TilePane();
        
        pane.setOrientation(Orientation.HORIZONTAL);
        pane.setAlignment(Pos.CENTER_RIGHT);
        pane.setHgap(10);
        pane.setPadding(new Insets(10, 0, 0, 0));
        
        ok_ = new Button(Literals.Get(Strings.BUTTON_OK));
        cancel_ = new Button(Literals.Get(Strings.BUTTON_CANCEL));

        ok_.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cancel_.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        pane.getChildren().addAll(ok_, cancel_);
        
        return pane;
    }
}
