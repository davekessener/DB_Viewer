package viewer.materials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Entry
{
    private List<String> cols_;
    private Map<String, Object> items_;
    private Map<String, Class<?>> types_;
    
    public Entry(List<String> names, List<Class<?>> types, List<Object> items)
    {
        assert names.size() == items.size() : "Precondition violated: cols.size() == items.size()";
        assert types.size() == items.size() : "Precondition violated: cols.size() == items.size()";
        
        cols_ = new ArrayList<>();
        items_ = new HashMap<>();
        types_ = new HashMap<>();
        
        for(int i = 0 ; i < names.size() ; ++i)
        {
            String n = names.get(i);
            
            cols_.add(n);
            items_.put(n, items.get(i));
            types_.put(n, types.get(i));
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
    
    public Object getItem(String id)
    {
        assert items_.containsKey(id) : "Precondition violated: items_.containsKey(id)";
        
        return items_.get(id);
    }
    
    public ReadOnlyStringProperty getValue(String id)
    {
        return new SimpleStringProperty(getItem(id).toString());
    }
}
