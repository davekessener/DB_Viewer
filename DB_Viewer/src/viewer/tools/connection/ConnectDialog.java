package viewer.tools.connection;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import viewer.literals.language.Literals;
import viewer.literals.language.Resources;
import viewer.literals.language.Strings;
import viewer.service.connection.ConnectionService;
import viewer.tools.viewer.IndicatorUI;

public class ConnectDialog
{
    private ConnectionService service_;
    private Connect tool_;
    private IndicatorUI ui_;
    private Stage stage_;
    
    public ConnectDialog(ConnectionService service)
    {
        this.service_ = service;
        this.ui_ = new IndicatorUI();
        this.tool_ = new Connect(service_, ui_);
        
        ui_.setContent(tool_.getUI());
        
        stage_ = createWindow();
        
        registerHandlers();
    }
    
    public void run()
    {
        stage_.centerOnScreen();
        stage_.showAndWait();
    }
    
    public void registerOnConnect(Connect.OnConnected h)
    {
        tool_.registerOnConnected(id -> { close(); h.act(id); });
    }
    
    private void registerHandlers()
    {
        tool_.registerOnCancel(() -> close());
    }
    
    private void close()
    {
        ui_.setInfo("");
        stage_.close();
    }
    
    private Stage createWindow()
    {
        Stage stage = new Stage();
        
        stage.setTitle(Literals.Get(Strings.UI_CONNECT_TITLE));
        stage.getIcons().add(Resources.GetImage(Resources.I_ADD));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene((Parent) ui_.getUI()));
        stage.setResizable(false);
        
        return stage;
    }
}
