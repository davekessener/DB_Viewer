package viewer.tools.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import viewer.literals.language.Literals;
import viewer.literals.language.Strings;
import viewer.materials.Entry;
import viewer.materials.Filter;
import viewer.tools.StringDecimalComparator;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.TilePane;

public class ConnectedUI
{
    private Node pane_;
    private ComboBox<String> select_;
    private Button add_, remove_;
    private Button disconnect_;
    private Hyperlink filters_;
    private TableView<Entry> table_;
    private Set<Node> active_;
    
    public ConnectedUI()
    {
        active_ = new HashSet<Node>();
        pane_ = createUI();
        
        configure();
    }
    
    public Node getUI()
    {
        return pane_;
    }
    
    // # ----------------------------------------------------------------------

    public void registerAdd(EventHandler<ActionEvent> h)
    {
        add_.setOnAction(h);
    }

    public void registerRemove(RemoveHandler h)
    {
        remove_.setOnAction(e ->
        {
            List<Entry> entries = table_.getSelectionModel().getSelectedItems();
            
            if(!entries.isEmpty()) h.act(entries);
        });
    }
    
    public void registerFilters(EventHandler<ActionEvent> h)
    {
        filters_.setOnAction(h);
    }
    
    public void registerDisconnect(EventHandler<ActionEvent> h)
    {
        disconnect_.setOnAction(h);
    }
    
    public void registerSelect(SelectHandler h)
    {
        select_.valueProperty().addListener((o, ov, nv) ->
        {
            if(nv != null && !nv.isEmpty())
            {
                table_.getSelectionModel().clearSelection();
                
                h.act(nv);
            }
        });
    }
    
    // # ----------------------------------------------------------------------
    
    public void setSelection(List<String> s)
    {
        select_.setItems(FXCollections.observableList(s));
        clear();
    }

    public void load(List<String> cs, ObservableList<Entry> rs, ObservableList<Filter> fs)
    {
        FilteredList<Entry> fl = new FilteredList<>(rs);
        SortedList<Entry> l = new SortedList<>(fl);
        
        fl.setPredicate(e ->
        {
            for(Filter f : fs)
            {
                if(!f.test(e)) return false;
            }
            
            return true;
        });
        
        l.comparatorProperty().bind(table_.comparatorProperty());
        
        table_.getColumns().clear();
        table_.setItems(l);
        
        for(String n : cs)
        {
            TableColumn<Entry, String> c = new TableColumn<>(n);

            c.setCellValueFactory(p -> p.getValue().getColumns().contains(n) ? 
                    p.getValue().getValue(n) : (ReadOnlyStringProperty) new SimpleStringProperty("")); // FIXME
//            c.setCellValueFactory(p -> p.getValue().getValue(n));
            c.setComparator(new StringDecimalComparator());
            
            table_.getColumns().add(c);
        }
        
        setEnabled(true);
    }
    
    public void setEnabled(boolean f)
    {
        for(Node n : active_)
        {
            n.setDisable(!f);
        }
    }
    
    public void clear()
    {
        table_.setItems(null);
        select_.setValue(null);
        setEnabled(false);
        table_.getColumns().clear();
    }
    
    public String getSelected()
    {
        return select_.getSelectionModel().getSelectedItem();
    }
    
    // # ======================================================================
    
    private void configure()
    {
        add_.disableProperty().bind(select_.getSelectionModel().selectedItemProperty().isNull());
        table_.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        remove_.disableProperty().bind(table_.getSelectionModel().selectedItemProperty().isNull());
    }
    
    private Node createUI()
    {
        GridPane pane = new GridPane();
        
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setHgap(10);
        pane.setVgap(10);

        ColumnConstraints cc = new ColumnConstraints();
        ColumnConstraints ce = new ColumnConstraints();
        ce.setHgrow(Priority.ALWAYS);
        pane.getColumnConstraints().addAll(cc, ce, cc);

        RowConstraints rc = new RowConstraints();
        RowConstraints re = new RowConstraints();
        re.setVgrow(Priority.ALWAYS);
        pane.getRowConstraints().addAll(rc, re, rc);
        
        pane.add(createSelect(), 0, 0);
        pane.add(createLinks(), 2, 0);
        pane.add(createTable(), 0, 1, 3, 1);
        pane.add(createButtons(), 0, 2, 3, 1);

        return pane;
    }
    
    private Node createSelect()
    {
        HBox pane = new HBox();
        
        pane.setAlignment(Pos.CENTER_LEFT);
        pane.setSpacing(10);
        
        pane.getChildren().add(new Label(Literals.Get(Strings.UI_TABLES)));
        pane.getChildren().add(select_ = new ComboBox<>());
        
        return pane;
    }
    
    private Node createLinks()
    {
        HBox pane = new HBox();
        
        pane.setAlignment(Pos.CENTER_RIGHT);
        pane.setSpacing(10);
        
        pane.getChildren().add(filters_ = new Hyperlink(Literals.Get(Strings.UI_FILTERS_TITLE)));
        
        active_.add(pane);
        
        return pane;
    }
    
    private Node createTable()
    {
        ContextMenu cm = new ContextMenu();
        table_ = new TableView<>();

        MenuItem selectAll = new MenuItem(Literals.Get(Strings.UI_TABLE_SELECTALL));
        MenuItem unselectAll = new MenuItem(Literals.Get(Strings.UI_TABLE_SELECTNONE));
        MenuItem inverseSelect = new MenuItem(Literals.Get(Strings.UI_TABLE_SELECTINVERSE));
        
        selectAll.setOnAction(e -> table_.getSelectionModel().selectAll());
        unselectAll.setOnAction(e -> table_.getSelectionModel().clearSelection());
        inverseSelect.setOnAction(e -> 
        {
            List<Entry> all = new ArrayList<>(table_.getItems());
            List<Entry> selected = table_.getSelectionModel().getSelectedItems();
            
            for(Entry entry : selected)
            {
                all.remove(entry);
            }
            
            table_.getSelectionModel().clearSelection();
            
            for(Entry entry : all)
            {
                table_.getSelectionModel().select(entry);
            }
        });
        
        cm.getItems().addAll(selectAll, unselectAll, inverseSelect);
        
        table_.setContextMenu(cm);

        return table_;
    }
    
    private Node createButtons()
    {
        BorderPane pane = new BorderPane();
        
        pane.setLeft(createButtonPane());
        pane.setRight(disconnect_ = new Button(Literals.Get(Strings.BUTTON_DISCONNECT)));
        
        return pane;
    }
    
    private Node createButtonPane()
    {
        TilePane pane = new TilePane();
        
        pane.setOrientation(Orientation.HORIZONTAL);
        pane.setHgap(10);

        add_ = new Button(Literals.Get(Strings.BUTTON_ADD));
        remove_ = new Button(Literals.Get(Strings.BUTTON_REMOVE));

        add_.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        remove_.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        pane.getChildren().addAll(add_, remove_);
        
        active_.add(pane);

        return pane;
    }

    public static interface RemoveHandler { void act(List<Entry> e); }
    public static interface SelectHandler { void act(String s); }
}
