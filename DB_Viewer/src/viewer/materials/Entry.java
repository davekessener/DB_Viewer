package viewer.materials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import viewer.materials.sql.SQLObject;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Entry
{
    private List<String> cols_;
    private Map<String, SQLObject> items_;
    private Map<String, Class<? extends SQLObject>> types_;
    
    public Entry(List<String> names, Map<String, Class<? extends SQLObject>> types, List<SQLObject> items)
    {
        assert names.size() == items.size() : "Precondition violated: cols.size() == items.size()";
        assert types.size() == items.size() : "Precondition violated: cols.size() == items.size()";
        
        cols_ = new ArrayList<>(names);
        items_ = new HashMap<>();
        types_ = new HashMap<>(types);
        
        for(int i = 0 ; i < names.size() ; ++i)
        {
            items_.put(names.get(i), items.get(i));
        }
    }
    
    public List<String> getColumns()
    {
        return Collections.unmodifiableList(cols_);
    }
    
    public Class<?> getType(String id)
    {
        assert types_.containsKey(id) : "Precondition violated: types_.containsKey(id)";
        
        return types_.get(id);
    }
    
    public SQLObject getItem(String id)
    {
        assert items_.containsKey(id) : "Precondition violated: items_.containsKey(id)";
        
        return items_.get(id);
    }
    
    public ReadOnlyStringProperty getValue(String id)
    {
        return new SimpleStringProperty(getItem(id).toString());
    }
}
