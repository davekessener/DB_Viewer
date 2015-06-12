package viewer.tools.ui;

public interface Indicator
{
    void setColor(String s);
    void setInfo(String s);
    void setEnabled(boolean f);
    void alert(Alert.AlertType type, String title, String msg);
}
