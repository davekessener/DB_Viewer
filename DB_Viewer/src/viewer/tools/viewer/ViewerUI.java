package viewer.tools.viewer;

import viewer.literals.language.Literals;
import viewer.literals.language.Resources;
import viewer.literals.language.Strings;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

class ViewerUI
{
    private Stage stage_;
    private TabPane tabs_;
    private NewPageHandler newpagehandler_;
    
    public ViewerUI(Stage primary)
    {
        stage_ = primary;
        newpagehandler_ = null;
        
        initStage();
    }
    
    public void show()
    {
        addTab();
        stage_.show();
    }
    
    public void registerOnClose(Runnable r)
    {
        stage_.setOnCloseRequest(e -> r.run());
    }
    
    private void initStage()
    {
        AnchorPane root = new AnchorPane();
        tabs_ = new TabPane();
        Button addButton = new Button();
        ImageView v = new ImageView(Resources.GetImage(Resources.I_ADD));

        addButton.setMinSize(38, 24);
        addButton.setPrefSize(38, 24);
        addButton.setMaxSize(38, 24);
        
        v.setFitWidth(16);
        v.setPreserveRatio(true);
        v.setSmooth(true);
        addButton.setGraphic(v);

        AnchorPane.setTopAnchor(tabs_, 0.0);
        AnchorPane.setBottomAnchor(tabs_, 0.0);
        AnchorPane.setLeftAnchor(tabs_, 0.0);
        AnchorPane.setRightAnchor(tabs_, 0.0);
        AnchorPane.setTopAnchor(addButton, 0.0);
        AnchorPane.setLeftAnchor(addButton, 0.0);

        addButton.setOnAction(e -> addTab());
        
        tabs_.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
        
        root.getChildren().addAll(tabs_, addButton);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(Resources.CSS_TABS);
        
        stage_.setTitle(Literals.Get(Strings.UI_TITLE));
        stage_.getIcons().add(Resources.GetImage(Resources.I_DATABASE));
        stage_.setScene(scene);
        stage_.centerOnScreen();
    }
    
    private void addTab()
    {
        Tab tab = new Tab(Literals.Get(Strings.UI_NEWCONNECTION));
        
        if(newpagehandler_ != null) newpagehandler_.addContent(tab);
        
        switch(tabs_.getTabs().size())
        {
            case 0:
                tab.setClosable(false);
                break;
            case 1:
                tabs_.getTabs().get(0).setClosable(true);
                break;
        }
        
        tab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event e)
            {
                if(tabs_.getTabs().size() == 1)
                {
                    tabs_.getTabs().get(0).setClosable(false);
                }
            }
        });
        
        tabs_.getTabs().add(tab);
        tabs_.getSelectionModel().select(tab);
    }
    
    public void registerNewPageHandler(NewPageHandler nph)
    {
        newpagehandler_ = nph;
    }
    
    public static interface NewPageHandler
    {
        void addContent(Tab tab);
    }
}
