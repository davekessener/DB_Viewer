package viewer.tools.connection;

import viewer.literals.Relation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ConnectedUI
{
    private TableView<Relation.Row> pane_;
    
    public ConnectedUI()
    {
        pane_ = createUI();
    }
    
    public Node getUI()
    {
        return pane_;
    }
    
    public void loadRelation(Relation r)
    {
        pane_.setItems(FXCollections.observableList(r.getRows()));
        
        for(String n : r.getColumns())
        {
            TableColumn<Relation.Row, String> c = new TableColumn<>(n);
            c.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().get(n)));
            pane_.getColumns().add(c);
        }
    }
    
    private TableView<Relation.Row> createUI()
    {
        TableView<Relation.Row> pane = new TableView<>();
        
        return pane;
    }
    
    public void registerDisconnect(EventHandler<ActionEvent> h)
    {
    }
}
