package viewer.tools.connection;

import viewer.literals.Relation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TableView;

public class ConnectedUI
{
    private Node pane_;
    
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
    }
    
    private Node createUI()
    {
        TableView<Relation.Row> pane = new TableView<>();
        
        return pane;
    }
    
    public void registerDisconnect(EventHandler<ActionEvent> h)
    {
    }
}
