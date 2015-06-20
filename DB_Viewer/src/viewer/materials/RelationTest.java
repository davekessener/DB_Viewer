package viewer.materials;

import static org.junit.Assert.*;

import org.junit.Test;

import viewer.exception.SQLInstantiationException;
import viewer.materials.sql.Varchar;

public class RelationTest
{
    @Test
    public void test() throws SQLInstantiationException
    {
        Relation.Factory f = Relation.GetFactory();
        String[] cols = new String[] {"ID", "Name"};
        Varchar[][] rows = new Varchar[][] {{Varchar.ValueOf("10"), Varchar.ValueOf("Tetsu")}, 
                                                 {Varchar.ValueOf("5"), Varchar.ValueOf("Aho")}};
        
//        assertFalse(f.isFilling());
//        assertFalse(f.isDone());
        
        for(String s : cols)
        {
            f.addColumn(s, Varchar.class);
        }
        
//        assertFalse(f.isFilling());
//        assertFalse(f.isDone());
        
        for(Varchar[] ss : rows)
        {
            f.addRow(ss);
        }
        
//        assertTrue(f.isFilling());
//        assertFalse(f.isDone());
        
        Relation r = f.produce();
        
        assertNotNull(r);
        
//        assertFalse(f.isFilling());
//        assertTrue(f.isDone());
        
        Entry firstrow = r.getRows().get(0);
        
        assertNotNull(firstrow);
        
        assertEquals(firstrow.getValue("Name").get(), rows[0][1]);
        
        String firstColumnName = r.getColumns().get(1);
        
        int i = 0;
        for(Entry row : r)
        {
            assertEquals(row.getValue("ID").get(), rows[i][0]);
            assertEquals(row.getValue(firstColumnName).get(), rows[i][1]);
            ++i;
        }
    }
    
//    @Test
//    public void test()
//    {
//        Relation.Factory f = Relation.GetFactory();
//        List<Object> row = new ArrayList<Object>();
//        
//        row.add(10);
//        row.add("tetsu");
//
//        assertFalse(f.isFilling());
//        assertFalse(f.isDone());
//
//        f.addColumn("ID", Number.class);
//        f.addColumn("Name", String.class);
//        
//        assertFalse(f.isFilling());
//        assertFalse(f.isDone());
//
//        f.addRow(row);
//
//        assertTrue(f.isFilling());
//        assertFalse(f.isDone());
//        
//        Relation r = f.finish();
//
//        assertFalse(f.isFilling());
//        assertTrue(f.isDone());
//        
//        assertEquals(r.getRow(0), row);
//    }
}
