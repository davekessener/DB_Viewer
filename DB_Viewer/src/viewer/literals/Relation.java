package viewer.literals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Relation implements Iterable<Relation.Row>
{
    private String[] cols_;
    private List<String[]> rows_;
    private Map<String, Integer> pos_;
    
    private Relation(String[] cols)
    {
        assert cols != null : "Precondition violated: cols_ != null";
        
        cols_ = cols;
        rows_ = new ArrayList<String[]>();
        pos_ = new HashMap<String, Integer>();
        
        for(int i = 0 ; i < cols_.length ; ++i)
        {
            pos_.put(cols_[i], i);
        }
    }
    
    public int colCount() { return cols_.length; }
    public int rowCount() { return rows_.size(); }
    
    private void addRow(String[] row)
    {
        assert row != null : "Vorbedingung verletzt: row != null";
        assert row.length == cols_.length : "Precondition violated: row.length == cols_.length";
        
        rows_.add(row);
    }
    
    public Row getRow(int i)
    {
        assert i >= 0 && i < rows_.size() : "Precondition violated: i > 0 && i < rows_.size()";
        
        return new Row(rows_.get(i));
    }

    @Override
    public Iterator<Row> iterator()
    {
        return new RowIterator();
    }
    
    public class Row implements Iterable<String>
    {
        private String[] row_;
        
        private Row(String[] row) { row_ = row; }
        
        public String get(String name)
        {
            name = name.toUpperCase();
            
            assert pos_.containsKey(name) : "Precondition violated: pos_.containsKey(name)";
            
            return row_[pos_.get(name)];
        }
        
        public String get(int i)
        {
            return row_[i];
        }

        @Override
        public Iterator<String> iterator()
        {
            return new CellIterator();
        }
        
        public class CellIterator implements Iterator<String>
        {
            private int i_ = 0;

            @Override
            public boolean hasNext()
            {
                return i_ < row_.length;
            }

            @Override
            public String next()
            {
                return row_[i_++];
            }
        }
    }
    
    public class RowIterator implements Iterator<Row>
    {
        private int i_ = 0;
        
        @Override
        public boolean hasNext()
        {
            return i_ < rowCount();
        }

        @Override
        public Row next()
        {
            return getRow(i_++);
        }
    }
    
    public static Factory GetFactory() { return new Factory(); }
    
    public static class Factory
    {
        private List<String> cols_;
        private Relation rel_;
        private boolean done_;
        
        private Factory()
        {
            cols_ = new ArrayList<String>();
            rel_ = null;
            done_ = false;
        }
        
        public boolean isFilling() { return rel_ != null; }
        public boolean isDone() { return done_; }
        
        public boolean hasColumn(String s) { return cols_.contains(s); }
        
        public void addColumn(String name)
        {
            name = name.toUpperCase();
            
            assert !isFilling() : "Precondition violated: !isFilling()";
            assert !isDone() : "Precondition violated: !isDone()";
            assert !hasColumn(name) : "Precondition violated: !hasColumn(name)";
            
            cols_.add(name);
        }
        
        public void addRow(String[] row)
        {
            assert !isDone() : "Precondition violated: !isDone()";
            
            init();
            
            rel_.addRow(row);
        }
        
        public Relation finish()
        {
            assert !isDone() : "Precondition violated: !isDone()";
            
            init();
            
            Relation r = rel_;
            
            done_ = true;
            rel_ = null;
            
            return r;
        }
        
        private void init()
        {
            if(!isFilling())
            {
                rel_ = new Relation(cols_.toArray(new String[cols_.size()]));
                cols_ = null;
            }
        }
    }
}

//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//
//public class Relation implements Iterable<List<Object>>
//{
//    private final Column[] cols_;
//    
//    private Relation(List<String> ns, List<Class<?>> cs)
//    {
//        assert ns.size() == cs.size() : "Precondition violated: ns.size() == cs.size()";
//        assert ns != null : "Precondition violated: ns != null";
//
//        cols_ = new Column[ns.size()];
//        
//        for(int i = 0 ; i < cols_.length ; ++i)
//        {
//            cols_[i] = new Column(ns.get(i), cs.get(i));
//        }
//    }
//    
//    private void addRow(Collection<Object> r)
//    {
//        assert r.size() == cols_.length : "Precondition violated: r.size() == cols_.length";
//        
//        int i = 0;
//        for(Object o : r)
//        {
//            cols_[i++].add(o);
//        }
//    }
//    
//    public String getColumnName(int i)
//    {
//        return cols_[i].getName();
//    }
//    
//    public List<Object> getRow(int i)
//    {
//        assert i < rowCount() : "Precondition violated: i < rowCount()";
//        
//        List<Object> row = new ArrayList<Object>(colCount());
//        
//        for(Column c : cols_)
//        {
//            row.add(c.get(i));
//        }
//        
//        return row;
//    }
//    
//    public int colCount() { return cols_.length; }
//    public int rowCount() { return colCount() > 0 ? cols_[0].size() : 0; }
//
//    @Override
//    public Iterator<List<Object>> iterator()
//    {
//        return new RowIterator();
//    }
//    
//    public class RowIterator implements Iterator<List<Object>>
//    {
//        private int i_ = 0;
//
//        @Override
//        public boolean hasNext()
//        {
//            return i_ < rowCount();
//        }
//
//        @Override
//        public List<Object> next()
//        {
//            return getRow(i_++);
//        }
//    }
//    
//    private static class Column
//    {
//        private final String name_;
//        private final Class<?> class_;
//        private List<Object> content_;
//        
//        public Column(String name, Class<?> c)
//        {
//            assert name != null : "Vorbedingung verletzt: name != null";
//            
//            if(name.isEmpty())
//                throw new RuntimeException("ERR: Empty Attribute Name");
//            
//            this.name_ = name;
//            this.class_ = c;
//            this.content_ = new ArrayList<Object>();
//        }
//        
//        public String getName() { return name_; }
//        public int size() { return content_.size(); }
//        
//        public Object get(int i)
//        {
//            return content_.get(i);
//        }
//        
//        public void add(Object o)
//        {
//            assert class_.isInstance(o) : "Precondition violated: t instanceof class_";
//            
//            content_.add(o);
//        }
//    }
//    
//    public static Factory GetFactory()
//    {
//        return new Factory();
//    }
//    
//    public static class Factory
//    {
//        private List<String> cn_;
//        private List<Class<?>> cc_;
//        private Relation rel_;
//        private boolean done_;
//        
//        private Factory()
//        {
//            cn_ = new ArrayList<String>();
//            cc_ = new ArrayList<Class<?>>();
//            rel_ = null;
//            done_ = false;
//        }
//        
//        public boolean isFilling() { return rel_ != null; }
//        public boolean isDone() { return done_; }
//        
//        public Relation finish()
//        {
//            assert !isDone() : "Precondition violated: !isDone()";
//            init();
//            
//            Relation r = rel_;
//            
//            done_ = true;
//            rel_ = null;
//            
//            return r;
//        }
//        
//        public void addColumn(String name, Class<?> c)
//        {
//            assert !isDone() : "Precondition violated: !isDone()";
//            assert !isFilling() : "Precondition violated: !isFilling()";
//            assert !cn_.contains(name.toLowerCase()) : "Precondition violated: !cn_.contains(name.toLowerCase())";
//            
//            cn_.add(name.toLowerCase());
//            cc_.add(c);
//        }
//        
//        public void addRow(Collection<Object> v)
//        {
//            assert !isDone() : "Precondition violated: !isDone()";
//            init();
//            
//            rel_.addRow(v);
//        }
//        
//        private void init()
//        {
//            if(!isFilling())
//            {
//                rel_ = new Relation(cn_, cc_);
//                cn_ = null;
//                cc_ = null;
//            }
//        }
//    }
//}
