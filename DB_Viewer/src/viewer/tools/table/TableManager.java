package viewer.tools.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import viewer.materials.Entry;
import viewer.materials.Pair;
import viewer.materials.Relation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TableManager
{
    private List<String> tables_;
    private Map<String, Pair<List<String>, ObservableList<Entry>>> relations_;
    
    public TableManager(List<String> tables)
    {
        this.tables_ = new ArrayList<>(tables);
        this.relations_ = new HashMap<>();
    }
    
    public List<String> getTables()
    {
        return tables_;
    }
    
    public String getTable(Entry e)
    {
        for(Map.Entry<String, Pair<List<String>, ObservableList<Entry>>> v : relations_.entrySet())
        {
            if(v.getValue().second.contains(e)) return v.getKey();
        }
        
        throw new Error("Entry now known!");
    }
    
    public void updateTable(String id, Relation relation)
    {
        assert knowsTable(id) : "Precondition violated: knowsTable(id)";
        
        List<String> columns = relation.getColumns();
        List<Entry> rows = new ArrayList<>();
        
        for(Relation.Row row : relation)
        {
            rows.add(new Entry(columns, row));
        }
        
        relations_.put(id, new Pair<>(columns, FXCollections.observableList(rows)));
    }
    
    public boolean knowsTable(String id)
    {
        return tables_.contains(id);
    }
    
    public boolean hasRelation(String id)
    {
        return relations_.containsKey(id);
    }
    
    public List<String> getColumns(String id)
    {
        assert hasRelation(id) : "Precondition violated: knowsTable(id)";
        
        return relations_.get(id).first;
    }
    
    public ObservableList<Entry> getRows(String id)
    {
        assert hasRelation(id) : "Precondition violated: knowsTable(id)";
        
        return relations_.get(id).second;
    }
}
