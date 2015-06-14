package viewer.tools.ui;

import javafx.scene.Node;

public interface Indicator
{
    void setColor(String s);
    void setInfo(String s);
    void setEnabled(boolean f);
    void setContent(Node n);
    Node getUI();
}
