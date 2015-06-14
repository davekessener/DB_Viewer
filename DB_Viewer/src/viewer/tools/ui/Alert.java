package viewer.tools.ui;

import org.controlsfx.dialog.Dialogs;

import viewer.literals.language.Literals;

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
    
    public static void ShowExceptionError(String title, String msg, Throwable t)
    {
        Dialogs.create().title(title).message(msg).showException(t);
    }


    public static void DisplayAlert(Alert.AlertType type, String title, String header, String msg)
    {
        Alert alert = new Alert(type);
        alert.setTitle(Literals.Get(title));
        alert.setHeaderText(Literals.Get(header));
        alert.setContentText(Literals.Get(msg));
        alert.showAndWait();
    }
    
    public static enum AlertType
    {
        ERROR,
        WARNING,
        INFORMATION
    }
}
