package viewer.materials;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Relation implements Iterable<Entry>
{
    private List<String> cols_;
    private List<Class<?>> types_;
    private List<Entry> rows_;
    
    private Relation(List<String> names, List<Class<?>> types, List<Object[]> items)
    {
        this.cols_ = new ArrayList<>(names);
        this.types_ = new ArrayList<>(types);
        this.rows_ = new ArrayList<>();
        
        for(Object[] o : items)
        {
            rows_.add(new Entry(names, types, Arrays.asList(o)));
        }
    }
    
    public int getColCount() { return cols_.size(); }
    public int getRowCount() { return rows_.size(); }
    
    public List<String> getColumns()
    {
        return Collections.unmodifiableList(cols_);
    }
    
    public List<Class<?>> getTypes()
    {
        return Collections.unmodifiableList(types_);
    }
    
    public List<Entry> getRows()
    {
        return Collections.unmodifiableList(rows_);
    }
    
    @Override
    public Iterator<Entry> iterator()
    {
        return rows_.iterator();
    }
    
    // # ----------------------------------------------------------------------
    
    public static Factory GetFactory() { return new Factory(); }
    
    public static class Factory
    {
        private Stage stage_ = new Stage1();
        
        private Factory() { }
        
        public void addColumn(String name, Class<?> type)
        {
            stage_.addColumn(name, type);
        }
        
        public void addRow(Object[] items)
        {
            stage_.addRow(items);
        }
        
        public Relation produce()
        {
            return stage_.produce();
        }
        
        private interface Stage
        {
            void addColumn(String name, Class<?> type);
            void addRow(Object[] items);
            Relation produce();
        }
        
        // # ==================================================================
        
        private class Stage1 implements Stage
        {
            protected List<String> names_ = new ArrayList<>();
            protected List<Class<?>> types_ = new ArrayList<>();
            
            @Override
            public void addColumn(String name, Class<?> type)
            {
                assert !names_.contains(name) : "Precondition violated: !names_.contains(name)";
                
                names_.add(name);
                types_.add(type);
            }

            @Override
            public void addRow(Object[] items)
            {
                stage_ = new Stage2(this);
                stage_.addRow(items);
            }

            @Override
            public Relation produce()
            {
                return (new Stage2(this)).produce();
            }
        }
        
        private class Stage2 implements Stage
        {
            protected List<String> names_;
            protected List<Class<?>> types_;
            protected List<Object[]> items_;
            
            public Stage2(Stage1 s)
            {
                names_ = s.names_; s.names_ = null;
                types_ = s.types_; s.types_ = null;
                items_ = new ArrayList<>();
            }

            @Override
            public void addColumn(String name, Class<?> type)
            {
                throw new Error("Tried to add column after stage 1!");
            }

            @Override
            public void addRow(Object[] items)
            {
                assert names_.size() == items.length : "Precondition violated: cols_.size() == items.length";
                assert types_.size() == items.length : "Precondition violated: cols_.size() == items.length";
                
                for(int i = 0 ; i < items.length ; ++i)
                {
//                    System.out.println(String.format("'%s instanceof %s' should hold", 
//                    types_.get(i).toGenericString(), items[i].getClass().toGenericString()));
                    assert types_.get(i).isAssignableFrom(items[i].getClass()) : 
                        "Precondition violated: item instanceof type";
                }
                
                items_.add(items);
            }

            @Override
            public Relation produce()
            {
                Relation r = new Relation(names_, types_, items_);
                
                stage_ = new Stage3();
                
                return r;
            }
        }
        
        private class Stage3 implements Stage
        {
            @Override
            public void addColumn(String name, Class<?> type)
            {
                throw new Error("Called 'addColumn' after production.");
            }

            @Override
            public void addRow(Object[] items)
            {
                throw new Error("Called 'addRow' after production.");
            }

            @Override
            public Relation produce()
            {
                throw new Error("Called 'produce' after production.");
            }
        }
    }
}
