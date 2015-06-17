package viewer.tools.filter;

import java.util.List;

import viewer.literals.language.Literals;
import viewer.literals.language.Resources;
import viewer.literals.language.Strings;
import viewer.materials.Filter;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FilterDialog
{
    private Stage stage_;
    private FilterUI ui_;
    private Runnable onclose_;
    
    public FilterDialog()
    {
        ui_ = new FilterUI();
        stage_ = createStage((Parent) ui_.getUI());
        
        registerHandlers();
    }
    
    public void show(List<String> columns, ObservableList<Filter> filters)
    {
        ui_.load(columns, filters);
        stage_.centerOnScreen();
        stage_.show();
    }
    
    public void registerOnClose(Runnable onclose)
    {
        assert onclose_ == null : "Precondition violated: onclose_ == null";
        
        onclose_ = onclose;
    }
    
    private void close()
    {
        assert onclose_ != null : "Precondition violated: onclose_ != null";
        
        stage_.close();
        onclose_.run();
    }
    
    private void registerHandlers()
    {
        ui_.registerClose(e -> close());
    }
    
    private Stage createStage(Parent ui)
    {
        Stage stage = new Stage();
        
        stage.setTitle(Literals.Get(Strings.UI_FILTERS_TITLE));
        stage.getIcons().add(Resources.GetImage(Resources.I_SEARCH));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(ui));
        
        return stage;
    }
}
