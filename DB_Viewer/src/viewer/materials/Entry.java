package viewer.materials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Entry implements Iterable<StringProperty>
{
    private List<String> cols_;
    private Map<String, StringProperty> row_;
    
    public Entry(List<String> columns, Relation.Row row)
    {
        row_ = new HashMap<>();
        cols_ = new ArrayList<>(columns);
        
        for(String s : columns)
        {
            row_.put(s, new SimpleStringProperty(this, s, row.get(s)));
        }
        
        assert row_.size() == row.size() : "Precondition violated: row_.size() == row.size()";
    }
    
    public List<String> getColumns()
    {
        return Collections.unmodifiableList(cols_);
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

    @Override
    public Iterator<StringProperty> iterator()
    {
        return new EntryIterator();
    }
    
    private class EntryIterator implements Iterator<StringProperty>
    {
        private Iterator<String> i_ = cols_.iterator();
        
        @Override
        public boolean hasNext()
        {
            return i_.hasNext();
        }

        @Override
        public StringProperty next()
        {
            return row_.get(i_.next());
        }
    }
}
