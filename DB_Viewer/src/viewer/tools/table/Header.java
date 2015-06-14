package viewer.tools.table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Header
{
    private StringProperty name_;
    
    public Header(String name)
    {
        name_ = new SimpleStringProperty(this, "name", name);
    }
    
    public String getNameProperty()
    {
        return name_.get();
    }

    public void setNameProperty(String name)
    {
        assert name != null && !name.isEmpty() : "Precondition violated: name != null && !name.isEmpty()";
        
        name_.set(name);
    }
    
    public StringProperty nameProperty()
    {
        return name_;
    }
}
