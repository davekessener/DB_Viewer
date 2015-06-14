package viewer.tools.viewer;

import viewer.literals.language.Literals;
import viewer.tools.ui.Indicator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class IndicatorUI implements Indicator
{
    private StackPane pane_;
    private Label info_;
    private Node content_, indicator_;
    
    public IndicatorUI()
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
    public void setInfo(String s)
    {
        info_.setText(Literals.Get(s));
    }
    
    @Override
    public void setEnabled(boolean f)
    {
        if(content_ != null)
        {
            content_.setDisable(!f);
        }
    }
    
    @Override
    public void setColor(String c)
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
}
