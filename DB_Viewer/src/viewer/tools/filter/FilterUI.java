package viewer.tools.filter;

import java.util.List;

import viewer.literals.language.Literals;
import viewer.literals.language.Strings;
import viewer.materials.Filter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.util.Callback;

public class FilterUI
{
    private Node pane_;
    private TableView<Filter> table_;
    private Button add_, remove_, close_;
    private List<String> cols_;
    
    public FilterUI()
    {
        pane_ = createUI();
        
        configure();
        registerHandlers();
    }
    
    public Node getUI()
    {
        return pane_;
    }
    
    public void load(List<String> columns, ObservableList<Filter> filters)
    {
        cols_ = columns;
        table_.setItems(filters);
    }
    
    // # ----------------------------------------------------------------------
    
    private void add()
    {
        table_.getItems().add(new Filter(cols_));
    }
    
    private void removeSelected()
    {
        table_.getItems().removeAll(table_.getSelectionModel().getSelectedItems());
    }
    
    private void registerHandlers()
    {
        add_.setOnAction(e -> add());
        remove_.setOnAction(e -> removeSelected());
    }
    
    public void registerClose(EventHandler<ActionEvent> h)
    {
        close_.setOnAction(h);
    }
    
    // # ======================================================================
    
    private void configure()
    {
        table_.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        remove_.disableProperty().bind(table_.getSelectionModel().selectedItemProperty().isNull());
    }
    
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

        TableColumn<Filter, String> matchCol = new TableColumn<>(Literals.Get(Strings.UI_FILTERS_TABLE_MATCH));
        TableColumn<Filter, Filter> typeCol = new TableColumn<>(Literals.Get(Strings.UI_FILTERS_TABLE_TYPE));
        TableColumn<Filter, Filter> colCol = new TableColumn<>(Literals.Get(Strings.UI_FILTERS_TABLE_COLUMN));
        TableColumn<Filter, Filter> activeCol = new TableColumn<>(Literals.Get(Strings.UI_FILTERS_TABLE_ACTIVE));
        
        matchCol.setCellValueFactory(p -> p.getValue().matchProperty());
        typeCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue()));
        colCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue()));
        activeCol.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue()));

        matchCol.setCellFactory(TextFieldTableCell.forTableColumn());
        typeCol.setCellFactory(p -> new NodeCell(f -> createTypeComboBox(f)));
        colCol.setCellFactory(p -> new NodeCell(f -> createColumnComboBox(f)));
        activeCol.setCellFactory(p -> new NodeCell(f -> createCheckbox(f)));
        
        matchCol.setOnEditCommit(e ->
        {
            ((Filter) e.getTableView().getItems().get(e.getTablePosition().getRow())).setMatch(e.getNewValue());
        });

        matchCol.setSortable(false);
        typeCol.setSortable(false);
        colCol.setSortable(false);
        activeCol.setSortable(false);

        table_.getColumns().add(matchCol);
        table_.getColumns().add(typeCol);
        table_.getColumns().add(colCol);
        table_.getColumns().add(activeCol);
        
        table_.setPrefWidth(600);
        table_.setEditable(true);
        
        table_.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        return table_;
    }
    
    private Node createButtons()
    {
        BorderPane pane = new BorderPane();
        
        pane.setPadding(new Insets(10, 0, 0, 0));

        pane.setLeft(createLeftButtons());
        pane.setCenter(createRightButtons());
        
        return pane;
    }
    
    private Node createLeftButtons()
    {
        TilePane pane = new TilePane();
        
        pane.setOrientation(Orientation.HORIZONTAL);
        pane.setHgap(10);
        
        add_ = new Button(Literals.Get(Strings.BUTTON_ADD));
        remove_ = new Button(Literals.Get(Strings.BUTTON_REMOVE));

        add_.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        remove_.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        pane.getChildren().addAll(add_, remove_);
        
        return pane;
    }
    
    private Node createRightButtons()
    {
        AnchorPane pane = new AnchorPane();
        close_ = new Button(Literals.Get(Strings.BUTTON_OK));
        
        AnchorPane.setRightAnchor(close_, 0.0);
        AnchorPane.setBottomAnchor(close_, 0.0);

        close_.setMaxWidth(Double.MAX_VALUE);
        close_.prefWidthProperty().bind(add_.widthProperty());
        close_.minWidthProperty().bind(add_.widthProperty());
        
        pane.getChildren().add(close_);
        
        return pane;
    }
    
    private Node createTypeComboBox(Filter f)
    {
        ComboBox<String> node = new ComboBox<>();
        
        for(Filter.Type t : Filter.Type.values())
        {
            node.getItems().add(t.toString());
        }
        
        node.valueProperty().addListener((o, ov, nv) ->
        {
            f.setType(Filter.Type.valueOf(nv));
        });

        node.getSelectionModel().select(0);

        return node;
    }
    
    private Node createColumnComboBox(Filter f)
    {
        ComboBox<String> node = new ComboBox<>();

        node.getItems().add(Literals.Get(Strings.UI_FILTERS_ANY));
        node.getItems().add(Literals.Get(Strings.UI_FILTERS_ALL));
        node.getItems().addAll(f.getColumns());
        
        node.valueProperty().addListener((o, ov, nv) ->
        {
            if(nv.equals(Literals.Get(Strings.UI_FILTERS_ANY)))
            {
                f.setAny();
            }
            else if(nv.equals(Literals.Get(Strings.UI_FILTERS_ALL)))
            {
                f.setAll();
            }
            else
            {
                f.setColumn(nv);
            }
        });

        node.getSelectionModel().select(0);

        return node;
    }
    
    private Node createCheckbox(Filter f)
    {
        CheckBox node = new CheckBox();
        
        node.selectedProperty().addListener((o, ov, nv) ->
        {
            f.setActive(nv);
        });
        
        return node;
    }
    
    private static class NodeCell extends TableCell<Filter, Filter>
    {
        private Callback<Filter, Node> hook_;
        
        public NodeCell(Callback<Filter, Node> hook) { hook_ = hook; }
        
        @Override
        public void updateItem(Filter o, boolean empty)
        {
            super.updateItem(o, empty);
            
            this.setAlignment(Pos.CENTER);
            
            setGraphic(empty ? null : hook_.call(o));
        }
    }
    
    public static interface RemoveHandler { void act(List<Filter> filters); }
}
