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
    private Map<String, Boolean> editable_;
    
    public TableManager(Map<String, Boolean> names)
    {
        this.names_ = new ArrayList<>(names.keySet());
        this.tables_ = new HashMap<>();
        this.editable_ = new HashMap<>(names);
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
            tables_.put(id, new Table(editable_.get(id)));
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
