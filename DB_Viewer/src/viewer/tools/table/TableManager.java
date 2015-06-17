package viewer.tools.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import viewer.materials.Entry;
import viewer.materials.Relation;
import viewer.materials.Table;

public class TableManager
{
    private List<String> names_;
    private Map<String, Table> tables_;
    
    public TableManager(List<String> names)
    {
        this.names_ = new ArrayList<>(names);
        this.tables_ = new HashMap<>();
    }
    
    public List<String> getNames()
    {
        return Collections.unmodifiableList(names_);
    }
    
    public String getTableName(Entry e)
    {
        for(Map.Entry<String, Table> v : tables_.entrySet())
        {
            if(v.getValue().containsRow(e)) return v.getKey();
        }
        
        throw new Error("Entry now known!");
    }
    
    public void updateTable(String id, Relation relation)
    {
        assert knowsTable(id) : "Precondition violated: knowsTable(id)";
        
        if(!tables_.containsKey(id))
        {
            tables_.put(id, new Table());
        }
        
        tables_.get(id).update(relation);
    }
    
    public boolean knowsTable(String id)
    {
        return names_.contains(id);
    }
    
    public boolean hasRelation(String id)
    {
        return tables_.containsKey(id);
    }
    
    public Table getTable(String id)
    {
        assert hasRelation(id) : "Precondition violated: hasRelation(id)";
        
        return tables_.get(id);
    }
}
