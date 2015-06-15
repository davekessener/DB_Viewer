package viewer.tools.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import viewer.materials.Entry;
import viewer.materials.Relation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TableManager
{
    private List<String> names_;
    private Map<String, Relation> relations_;
    private Map<String, ObservableList<Entry>> tables_;
    
    public TableManager(List<String> names)
    {
        this.names_ = new ArrayList<>(names);
        this.relations_ = new HashMap<>();
        this.tables_ = new HashMap<>();
    }
    
    public List<String> getNames()
    {
        return Collections.unmodifiableList(names_);
    }
    
    public String getTable(Entry e)
    {
        for(Map.Entry<String, ObservableList<Entry>> v : tables_.entrySet())
        {
            if(v.getValue().contains(e)) return v.getKey();
        }
        
        throw new Error("Entry now known!");
    }
    
    public void updateTable(String id, Relation relation)
    {
        assert knowsTable(id) : "Precondition violated: knowsTable(id)";
        
        List<Entry> rows = new ArrayList<>(relation.getRows());
        
        relations_.put(id, relation);
        tables_.put(id, FXCollections.observableList(rows));
    }
    
    public boolean knowsTable(String id)
    {
        return names_.contains(id);
    }
    
    public boolean hasRelation(String id)
    {
        return relations_.containsKey(id);
    }
    
    public List<String> getColumns(String id)
    {
        assert hasRelation(id) : "Precondition violated: knowsTable(id)";
        
        return relations_.get(id).getColumns();
    }
    
    public ObservableList<Entry> getRows(String id)
    {
        assert hasRelation(id) : "Precondition violated: knowsTable(id)";
        
        return tables_.get(id);
    }
    
    public Relation getRelation(String id)
    {
        assert hasRelation(id) : "Precondition violated: hasRelation(id)";
        
        return relations_.get(id);
    }
}
