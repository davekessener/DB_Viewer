package viewer.tools.ui;

import org.controlsfx.dialog.Dialogs;

@SuppressWarnings("deprecation")
public class Alert
{
    private AlertType type_;
    private String title_;
    private String header_;
    private String msg_;
    
    public Alert(AlertType t)
    {
        type_ = t;
        title_ = null;
        header_ = null;
        msg_ = null;
    }
    
    public void setTitle(String title)
    {
        title_ = title;
    }
    
    public void setHeaderText(String header)
    {
        header_ = header;
    }
    
    public void setContentText(String msg)
    {
        msg_ = msg;
    }
    
    public void showAndWait()
    {
       Dialogs d = Dialogs.create().title(title_).masthead(header_).message(msg_);
       
       switch(type_)
       {
           case ERROR: d.showError(); break;
           case INFORMATION: d.showInformation(); break;
           case WARNING: d.showWarning(); break;
       }
    }
    
    public static enum AlertType
    {
        ERROR,
        WARNING,
        INFORMATION
    }
}
