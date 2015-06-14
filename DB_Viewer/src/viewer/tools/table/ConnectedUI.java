package viewer.tools.table;

import java.util.List;

import viewer.literals.Relation;
import viewer.tools.StringDecimalComparator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.util.Callback;

public class ConnectedUI
{
    private Node pane_;
    private ComboBox<String> select_;
    private Button add_, commit_, rollback_;
    private Button disconnect_;
    private TableView<Entry> table_;
    
    public ConnectedUI()
    {
        pane_ = createUI();
    }
    
    public Node getUI()
    {
        return pane_;
    }
    
    public void registerDisconnect(EventHandler<ActionEvent> h)
    {
        disconnect_.setOnAction(h);
    }
    
//    public void loadRelation(Relation r)
//    {
//    }
    
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
//        pane.add(createScrollPane(), 0, 1, 3, 1);
        pane.add(table_ = new TableView<>(), 0, 1, 3, 1);
        pane.add(createButtons(), 0, 2, 3, 1);
        
        return pane;
    }
    
    private Node createSelect()
    {
        HBox pane = new HBox();
        
        pane.setAlignment(Pos.CENTER_LEFT);
        pane.setSpacing(10);
        
        pane.getChildren().add(new Label("Tables:"));
        pane.getChildren().add(select_ = new ComboBox<>());
        
        return pane;
    }
    
    private Node createLinks()
    {
        HBox pane = new HBox();
        
        pane.setAlignment(Pos.CENTER_RIGHT);
        pane.setSpacing(10);
        
        return pane;
    }
    
    private Node createScrollPane()
    {
        ScrollPane pane = new ScrollPane();

        pane.setHbarPolicy(ScrollBarPolicy.NEVER);
        pane.setVbarPolicy(ScrollBarPolicy.NEVER);
        pane.setFitToHeight(true);
        pane.setFitToWidth(true);
        
        pane.setContent(createScrollContent());
        
        return pane;
    }
    
    private Node createScrollContent()
    {
        BorderPane pane = new BorderPane();
        
        pane.setCenter(table_ = new TableView<>());
        pane.setBottom(createInnerButtons());

        pane.prefHeightProperty().bind(table_.prefHeightProperty());
        pane.prefWidthProperty().bind(table_.prefWidthProperty());
        
        return pane;
    }
    
    private Node createInnerButtons()
    {
        HBox pane = new HBox();
        
        pane.setAlignment(Pos.CENTER_LEFT);
        pane.setSpacing(10);
        
        pane.getChildren().add(add_ = new Button("Add"));
//        pane.getChildren().add(clear_ = new Button("Clear"));
        
        return pane;
    }
    
    private Node createButtons()
    {
        GridPane pane = new GridPane();
        
        pane.setHgap(10);

        ColumnConstraints cc = new ColumnConstraints();
        ColumnConstraints ce = new ColumnConstraints();
        ce.setHgrow(Priority.ALWAYS);
        pane.getColumnConstraints().addAll(cc, cc, ce, cc);
        
        pane.add(commit_ = new Button("Commit"), 0, 0);
        pane.add(rollback_ = new Button("Rollback"), 1, 0);
        pane.add(disconnect_ = new Button("Disconnect"), 3, 0);
        
        return pane;
    }
    
//    
//    public ConnectedUI()
//    {
//        pane_ = createUI();
//    }
//    
//    public Node getUI()
//    {
//        return pane_;
//    }
    
    public void load(List<String> cs, ObservableList<Entry> rs)
    {
        table_.setItems(rs);
        
        for(String n : cs)
        {
            TableColumn<Entry, String> c = new TableColumn<>(n);
            
            c.setCellValueFactory(p -> p.getValue().getProperty(n));
            c.setComparator(new StringDecimalComparator());
            
            table_.getColumns().add(c);
        }
    }
    
//    public void loadRelation(Relation r)
//    {
//        table_.setItems(FXCollections.observableList(r.getRows()));
//        
//        for(String n : r.getColumns())
//        {
//            TableColumn<Relation.Row, String> c = new TableColumn<>(n);
//            c.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(n)));
//            c.setComparator(new StringDecimalComparator());
//            table_.getColumns().add(c);
//        }
//    }
//    
//    private TableView<Relation.Row> createUI()
//    {
//        TableView<Relation.Row> pane = new TableView<>();
//        
//        return pane;
//    }
//    
//    public void registerDisconnect(EventHandler<ActionEvent> h)
//    {
//    }
}
