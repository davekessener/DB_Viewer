package viewer.materials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Filter implements Predicate<Entry>
{
    private StringProperty match_;
    private ObjectProperty<Type> type_;
    private StringProperty column_;
    private ObjectProperty<Model> model_;
    private BooleanProperty active_;
    private List<String> cols_;
    private Pattern ptrn_;
    
    public Filter(List<String> columns)
    {
        match_ = new SimpleStringProperty(this, "match");
        type_ = new SimpleObjectProperty<>(this, "type", Type.MATCH);
        column_ = new SimpleStringProperty(this, "column");
        model_ = new SimpleObjectProperty<>(this, "model");
        active_ = new SimpleBooleanProperty(this, "active");
        cols_ = new ArrayList<>(columns);
        ptrn_ = Pattern.compile("");
    }
    
    public List<String> getColumns()
    {
        return Collections.unmodifiableList(cols_);
    }

    @Override
    public boolean test(Entry e)
    {
        return isActive() ? matches(e) : true;
    }
    
    public boolean matches(Entry e)
    {
        List<String> cs = e.getColumns();
        
        if(isAny())
        {
            for(String s : cs)
            {
                if(matches(e, s)) return true;
            }
            
            return false;
        }
        else if(isAll())
        {
            for(String s : cs)
            {
                if(!matches(e, s)) return false;
            }
            
            return true;
        }
        else
        {
            return matches(e, getColumn());
        }
    }
    
    public boolean matches(Entry e, String n)
    {
        assert e != null : "Vorbedingung verletzt: e != null";
        assert n != null : "Vorbedingung verletzt: n != null";
        
        Matcher m = ptrn_.matcher(e.getValue(n).get());
        
        switch(getType())
        {
            case MATCH:
                return m.matches();
            case MISMATCH:
                return !m.matches();
            case CONTAINS:
                return m.find();
            case CONTAINS_NOT:
                return !m.find();
        }
        
        throw new Error("Unknown filter-type " + getType().toString());
    }
    
    public String getMatch() { return match_.get(); }
    public Type getType() { return type_.get(); }
    public String getColumn() { return column_.get(); }
    public boolean isAny() { return model_.get() == Model.ANY; }
    public boolean isAll() { return model_.get() == Model.ALL; }
    public boolean isActive() { return active_.get(); }
    
    public void setMatch(String s) { assert s != null : "Precondition violated: s != null"; match_.set(s); ptrn_ = Pattern.compile(s); }
    public void setType(Type t) { assert t != null : "Vorbedingung verletzt: t != null"; type_.set(t); model_.set(Model.COLUMN); }
    public void setColumn(String s) { assert s != null : "Vorbedingung verletzt: s != null"; column_.set(s); }
    public void setAny() { model_.set(Model.ANY); }
    public void setAll() { model_.set(Model.ALL); }
    public void setActive(boolean f) { active_.set(f); }
    
    public StringProperty matchProperty() { return match_; }
    public ObjectProperty<Type> typeProperty() { return type_; }
    public StringProperty columnProperty() { return column_; }
    public ObjectProperty<Model> modelProperty() { return model_; }
    public BooleanProperty activeProperty() { return active_; }
    
    public static enum Type
    {
        MATCH,
        MISMATCH,
        CONTAINS,
        CONTAINS_NOT
    }
    
    public static enum Model
    {
        ANY,
        ALL,
        COLUMN
    }
}
