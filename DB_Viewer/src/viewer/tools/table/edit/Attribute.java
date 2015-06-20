package viewer.tools.table.edit;

import viewer.materials.sql.SQLObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Attribute
{
    private StringProperty name_;
    private StringProperty value_;
    private Class<? extends SQLObject> type_;
    
    public Attribute(String s, SQLObject o)
    {
        assert s != null : "Vorbedingung verletzt: s != null";
        assert o != null : "Vorbedingung verletzt: o != null";
        
        name_ = new SimpleStringProperty(this, "name", s);
        value_ = new SimpleStringProperty(this, "value", o.toString());
        type_ = o.getClass();
    }
    
    public String getName() { return name_.get(); }
    public void setName(String s) { assert s != null : "Precondition violated: s != null"; name_.set(s); }
    public StringProperty nameProperty() { return name_; }
    
    public String getValue() { return value_.get(); }
    public void setValue(String s) { assert s != null : "Vorbedingung verletzt: s != null"; value_.set(s); }
    public StringProperty valueProperty() { return value_; }
    
    public Class<? extends SQLObject> getType() { return type_; }
}
