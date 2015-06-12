package viewer.tools.connection;

import viewer.literals.language.Literals;
import viewer.tools.ui.Alert;
import viewer.tools.ui.Indicator;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ManagerUI implements Indicator
{
    private StackPane pane_;
    private Label info_;
    private Node content_, indicator_;
    
    public ManagerUI()
    {
        pane_ = createUI();
    }
    
    public Node getUI()
    {
        return pane_;
    }
    
    public void setContent(Node content)
    {
        this.content_ = content;
        
        pane_.getChildren().clear();
        pane_.getChildren().addAll(indicator_, content_);
    }
    
    // # ----------------------------------------------------------------------

    @Override
    public void alert(Alert.AlertType type, String title, String msg)
    {
        Platform.runLater(() -> doAlert(type, title, msg));
    }
    
    @Override
    public void setInfo(String s)
    {
        Platform.runLater(() -> doSetInfo(s));
    }
    
    @Override
    public void setEnabled(boolean f)
    {
        Platform.runLater(() -> doSetEnabled(f));
    }
    
    @Override
    public void setColor(String c)
    {
        Platform.runLater(() -> doSetColor(c));
    }
    
    public void doAlert(Alert.AlertType type, String title, String msg)
    {
        displayAlert(type, title, null, msg);
    }
    
    public void doSetInfo(String s)
    {
        info_.setText(Literals.Get(s));
    }
    
    public void doSetEnabled(boolean f)
    {
        if(content_ != null)
        {
            content_.setDisable(!f);
        }
    }
    
    public void doSetColor(String c)
    {
        info_.setTextFill(Color.web(Literals.Get(c)));
    }
    
    // # ----------------------------------------------------------------------
    
    private StackPane createUI()
    {
        StackPane pane = new StackPane();
        
        pane.getChildren().add(indicator_ = createIndicator());
        
        return pane;
    }
    
    private Node createIndicator()
    {
        HBox pane = new HBox();
        
        pane.setSpacing(10);
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setAlignment(Pos.BOTTOM_LEFT);

        pane.getChildren().add(info_ = new Label());
        
        return pane;
    }

    private void displayAlert(Alert.AlertType type, String title, String header, String msg)
    {
        Alert alert = new Alert(type);
        alert.setTitle(Literals.Get(title));
        alert.setHeaderText(Literals.Get(header));
        alert.setContentText(Literals.Get(msg));
        alert.showAndWait();
    }
}
