package viewer.tools.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import viewer.literals.Relation;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Entry
{
    private Map<String, StringProperty> row_;
    
    public Entry(List<String> columns, Relation.Row row)
    {
        row_ = new HashMap<>();
        
        for(String s : columns)
        {
            row_.put(s, new SimpleStringProperty(this, s, row.get(s)));
        }
        
        assert row_.size() == row.size() : "Precondition violated: row_.size() == row.size()";
    }
    
    public String get(String id)
    {
        assert row_.containsKey(id) : "Precondition violated: row_.containsKey(id)";
        
        return row_.get(id).get();
    }
    
    public StringProperty getProperty(String id)
    {
        assert row_.containsKey(id) : "Precondition violated: row_.containsKey(id)";
        
        return row_.get(id);
    }
}
