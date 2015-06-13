package viewer.tools.connection;

import viewer.exception.URLException;
import viewer.literals.URL;
import viewer.literals.language.Literals;
import viewer.literals.language.Strings;
import viewer.tools.ui.NumericField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

class ConnectUI
{
    private Node pane_;
    private Button cancel_, connect_, test_;
    private TextField name_, server_, sid_, user_, password_;
    private NumericField port_;
    
    public ConnectUI()
    {
        pane_ = createUI();
    }
    
    public Node getUI()
    {
        return pane_;
    }
    
    public Form getInput()
    {
        return new Form(
                    name_.getText(),
                    server_.getText(),
                    port_.getValue(),
                    sid_.getText(),
                    user_.getText(),
                    password_.getText());
    }
    
    public void registerConnect(EventHandler<ActionEvent> h)
    {
        connect_.setOnAction(h);
    }
    
    public void registerCancel(EventHandler<ActionEvent> h)
    {
        cancel_.setOnAction(h);
    }
    
    public void registerTest(EventHandler<ActionEvent> h)
    {
        test_.setOnAction(h);
    }
    
    private Node createUI()
    {
        GridPane grid = new GridPane();
        int row = 0;
        
        configureGrid(grid);
        
        addLabel(grid, Strings.S_ESTABLISH_NAME, 0, row);
        addLabel(grid, Strings.S_ESTABLISH_SID, 2, row);

        grid.add(name_ = new TextField(), 1, row);
        grid.add(sid_ = new TextField(), 3, row);
        
        ++row;
        
        addLabel(grid, Strings.S_ESTABLISH_SERVER, 0, row);
        addLabel(grid, Strings.S_ESTABLISH_PORT, 2, row);

        grid.add(server_ = new TextField(), 1, row);
        grid.add(port_ = new NumericField(), 3, row);
        
        ++row;

        sid_.setMinWidth(20);
        sid_.setPrefWidth(80);
        port_.setMinWidth(20);
        port_.setPrefWidth(80);
        
        name_.setText(Literals.Get(Strings.S_NEWCONNECTION));
        
        if(Literals.DEBUG)
        {
            doDebugWork(grid);
            
            ++row;
        }

        addLabel(grid, Strings.S_ESTABLISH_USER, 0, row);

        grid.add(createUserPassword(), 1, row, 3, 1);
        
        ++row;
        
        grid.add(createButtons(), 0, row, 4, 1);
        
        connect_.defaultButtonProperty().bind(connect_.focusedProperty());

        name_.setOnKeyPressed(e -> checkEnter(e));
        server_.setOnKeyPressed(e -> checkEnter(e));
        port_.setOnKeyPressed(e -> checkEnter(e));
        sid_.setOnKeyPressed(e -> checkEnter(e));
        user_.setOnKeyPressed(e -> checkEnter(e));
        password_.setOnKeyPressed(e -> checkEnter(e));
        
        return grid;
    }
    
    private void checkEnter(KeyEvent e)
    {
        if(e.getCode().equals(KeyCode.ENTER)) connect_.getOnAction().handle(new ActionEvent());
    }
    
    private Node createButtons()
    {
        HBox pane = new HBox();
        
        pane.setSpacing(10);
        pane.setAlignment(Pos.BOTTOM_RIGHT);

        pane.getChildren().add(test_ = new Button(Literals.Get(Strings.B_ESTABLISH_TEST)));
        pane.getChildren().add(connect_ = new Button(Literals.Get(Strings.B_ESTABLISH_CONNECT)));
        pane.getChildren().add(cancel_ = new Button(Literals.Get(Strings.B_ESTABLISH_CANCEL)));
        
        return pane;
    }
    
    private void addLabel(GridPane grid, String s, int x, int y) { addLabel(grid, s, x, y, 1, 1); }
    private void addLabel(GridPane grid, String s, int x, int y, int w, int h)
    {
        Label lbl = new Label(Literals.Get(s));
        GridPane.setHalignment(lbl, HPos.RIGHT);
        grid.add(lbl, x, y, w, h);
    }
    
    private Node createUserPassword()
    {
        GridPane pane = new GridPane();
        
        pane.setHgap(10);
        
        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.SOMETIMES);
        pane.getColumnConstraints().add(cc);
        pane.getColumnConstraints().add(new ColumnConstraints());
        pane.getColumnConstraints().add(cc);
        
        pane.add(user_ = new TextField(), 0, 0);
        addLabel(pane, Strings.S_ESTABLISH_PASSWORD, 1, 0);
        pane.add(password_ = new PasswordField(), 2, 0);
        
        return pane;
    }
    
    private void configureGrid(GridPane grid)
    {
        grid.setHgap(10.0);
        grid.setVgap(10.0);
        grid.setPadding(new Insets(15, 10, 10, 10));

        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().add(new ColumnConstraints());
        grid.getColumnConstraints().add(cc);
        
        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.ALWAYS);
        grid.getRowConstraints().add(new RowConstraints());
        grid.getRowConstraints().add(new RowConstraints());
        grid.getRowConstraints().add(new RowConstraints());
        if(Literals.DEBUG) grid.getRowConstraints().add(new RowConstraints());
        grid.getRowConstraints().add(rc);
    }
    
    private void doDebugWork(GridPane grid)
    {
        server_.setText("http://ora14.informatik.haw-hamburg.de/");
        port_.setText("1521");
        sid_.setText("inf14");
        
        TextField debug = new TextField();
        Runnable updateDebugField = () ->
        {
            try
            {
                debug.setText(URL.Get(server_.getText(), port_.getValue(), sid_.getText()).toString());
            }
            catch(URLException e)
            {
                debug.setText("---");
            }
        };
        
        debug.setEditable(false);
        
        grid.add(debug, 0, 2, 4, 1);

        server_.textProperty().addListener((ob, o, n) -> updateDebugField.run());
        port_.textProperty().addListener((ob, o, n) -> updateDebugField.run());
        sid_.textProperty().addListener((ob, o, n) -> updateDebugField.run());
        
        updateDebugField.run();
    }
}
