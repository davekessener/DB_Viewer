package viewer.tools.table;

import viewer.literals.language.Literals;
import viewer.literals.language.Strings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;

public class EstablishUI
{
    private Node pane_;
    private Button open_;
    
    public EstablishUI()
    {
        pane_ = createUI();
    }
    
    public Node getUI()
    {
        return pane_;
    }
    
    public void registerOnClick(EventHandler<ActionEvent> e)
    {
        open_.setOnAction(e);
    }
    
    private Node createUI()
    {
        BorderPane pane = new BorderPane();
        
        pane.setCenter(open_ = new Button(Literals.Get(Strings.B_ESTABLISH)));
        
        pane.setOnKeyPressed(e -> { if(e.getCode() == KeyCode.ENTER) open_.getOnAction().handle(new ActionEvent()); });
        
        open_.defaultButtonProperty().bind(open_.focusedProperty());
        
        return pane;
    }
}
