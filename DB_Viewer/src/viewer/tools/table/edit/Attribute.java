package viewer.tools.table.edit;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Attribute
{
    private StringProperty name_;
    private Object value_;
    private Class<?> type_;
    
    public Attribute(String s, Object o, Class<?> t)
    {
        assert s != null : "Vorbedingung verletzt: s != null";
        assert t != null : "Vorbedingung verletzt: t != null";
        
        name_ = new SimpleStringProperty(this, "name", s);
        value_ = o;
        type_ = t;
    }
    
    public String getName() { return name_.get(); }
    public void setName(String s) { assert s != null : "Precondition violated: s != null"; name_.set(s); }
    public StringProperty nameProperty() { return name_; }
    
    public Object getValue() { return value_; }
    public void setValue(Object o) { value_ = o; }
    
    public Class<?> getType() { return type_; }
}
