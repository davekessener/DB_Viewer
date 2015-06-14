package viewer.tools.viewer;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import viewer.literals.language.Literals;
import viewer.tools.ui.Indicator;

public class IndicatorDialog implements Indicator
{
    private StackPane pane_;
    private Label info_;
    private Stage stage_;
    private Node content_;
    
    public IndicatorDialog()
    {
        pane_ = new StackPane();
        stage_ = new Stage();
        
        init();
    }
    
    public void close()
    {
        stage_.close();
    }
    
    @Override
    public void setColor(String s)
    {
        info_.setTextFill(Color.web(Literals.Get(s)));
    }

    @Override
    public void setInfo(String s)
    {
        if(s == null || s.isEmpty())
        {
            stage_.close();
            setEnabled(true);
        }
        else
        {
            info_.setText(Literals.Get(s));
            setEnabled(false);
            stage_.centerOnScreen();
            stage_.show();
        }
    }

    @Override
    public void setEnabled(boolean f)
    {
        if(content_ != null) content_.setDisable(!f);
    }

    @Override
    public void setContent(Node n)
    {
        pane_.getChildren().clear();
        pane_.getChildren().add(content_ = n);
    }

    @Override
    public Node getUI()
    {
        return pane_;
    }
    
    private void init()
    {
        StackPane pane = new StackPane();
        
        pane.getChildren().add(info_ = new Label());
        
        stage_.setTitle("");
        stage_.setScene(new Scene(pane, 400, 150));
        stage_.setResizable(false);
        stage_.setOnCloseRequest(e -> e.consume());
    }
}
