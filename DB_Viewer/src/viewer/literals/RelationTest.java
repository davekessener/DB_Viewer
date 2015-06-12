package viewer.literals;

import static org.junit.Assert.*;

import org.junit.Test;

public class RelationTest
{
    @Test
    public void test()
    {
        Relation.Factory f = Relation.GetFactory();
        String[] cols = new String[] {"ID", "Name"};
        String[][] rows = new String[][] {{"10", "Tetsu"}, {"5", "Aho"}};
        
        assertFalse(f.isFilling());
        assertFalse(f.isDone());
        
        for(String s : cols)
        {
            f.addColumn(s);
        }
        
        assertFalse(f.isFilling());
        assertFalse(f.isDone());
        
        for(String[] ss : rows)
        {
            f.addRow(ss);
        }
        
        assertTrue(f.isFilling());
        assertFalse(f.isDone());
        
        Relation r = f.finish();
        
        assertNotNull(r);
        
        assertFalse(f.isFilling());
        assertTrue(f.isDone());
        
        Relation.Row firstrow = r.getRow(0);
        
        assertNotNull(firstrow);
        
        assertEquals(firstrow.get("name"), rows[0][1]);
        
        int i = 0;
        for(Relation.Row row : r)
        {
            assertEquals(row.get("ID"), rows[i][0]);
            assertEquals(row.get(1), rows[i][1]);
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
