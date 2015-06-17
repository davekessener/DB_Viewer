package viewer.materials;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Table
{
    private Relation relation_;
    private ObservableList<Entry> rows_;
    private ObservableList<Filter> filters_ = FXCollections.observableArrayList();
    
    public void update(Relation r)
    {
        relation_ = r;
        rows_ = FXCollections.observableList(new ArrayList<>(r.getRows()));
    }
    
    public ObservableList<Filter> getFilters()
    {
        return filters_;
    }
    
    public boolean containsRow(Entry e)
    {
        return rows_.contains(e);
    }
    
    public List<String> getColumns()
    {
        return relation_.getColumns();
    }
    
    public ObservableList<Entry> getRows()
    {
        return rows_;
    }
    
    public Relation getRelation()
    {
        return relation_;
    }
}
