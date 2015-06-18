package viewer.tools.table.edit;

import viewer.literals.language.Literals;
import viewer.literals.language.Resources;
import viewer.literals.language.Strings;
import viewer.materials.Entry;
import viewer.materials.Relation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditDialog
{
    private Stage stage_;
    private UpdateEntry onclose_;
    private EditUI ui_;
    private Entry old_;
    
    public EditDialog()
    {
        ui_ = new EditUI();
        
        stage_ = createStage((Parent) ui_.getUI());
        
        registerHandlers();
    }
    
    public void show(Relation r, Entry e)
    {
        ui_.load(r, old_ = e);
        stage_.centerOnScreen();
        stage_.showAndWait();
    }
    
    public void registerOnClose(UpdateEntry r)
    {
        assert onclose_ == null : "Precondition violated: onclose_ == null";
        
        onclose_ = r;
    }
    
    public void close()
    {
        stage_.close();
    }
    
    private void updateAndClose()
    {
        assert onclose_ != null : "Precondition violated: onclose_ != null";
        
        close();
        onclose_.update(old_, ui_.getEntry());
    }
    
    private void registerHandlers()
    {
        ui_.registerOK(e -> updateAndClose());
        ui_.registerCancel(e -> close());
    }
    
    private Stage createStage(Parent ui)
    {
        Stage stage = new Stage();
        
        stage.setTitle(Literals.Get(Strings.UI_EDIT_TITLE_EDIT));
        stage.getIcons().add(Resources.GetImage(Resources.I_EDIT));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(ui));
        
        return stage;
    }
    
    public static interface UpdateEntry { void update(Entry e1, Entry e2); }
}
