package viewer.literals;

import static org.junit.Assert.*;

import org.junit.Test;

import viewer.exception.URLException;

public class URLTest
{
    @Test
    public void testHAW() throws URLException
    {
        URL.Get("ora14.informatik.haw-hamburg.de", 1521, "inf14.infromatik.haw-hamburg.de");
    }
    
    @Test
    public void testProtokol() throws URLException
    {
        URL u1 = URL.Get("http://google.com:80", 1, "a");
        URL u2 = URL.Get("http://google.com/en", 1, "a");
        URL u3 = URL.Get("http://google.com:80/en", 1, "a");

        assertEquals(u1, u2);
        assertEquals(u1, u3);
    }
    
    @Test
    public void testEquality() throws URLException
    {
        URL u1 = URL.Get("a", 1, "b");
        URL u2 = URL.Get("a", 1, "b");
        URL u3 = URL.Get("d", 1, "e");
        URL u4 = URL.Get("A", 1, "b");
        URL u5 = URL.Get("a", 1, "B");
        
        assertEquals("Identity", u1, u1);
        assertEquals(u1, u2);
        assertNotEquals(u1, u3);
        assertEquals("Case", u1, u4);
        assertNotEquals("Case", u1, u5);
    }
    
    @Test(expected = URLException.class)
    public void testDots() throws URLException
    {
        URL.Get("abc.", 1, "a");
    }
    
    @Test(expected = URLException.class)
    public void AllInvalid() throws URLException
    {
        URL.Get("", 0, "");
    }
    
    @Test(expected = URLException.class)
    public void ServerEmpty() throws URLException
    {
        URL.Get("", 1, "b");
    }
    
    @Test(expected = URLException.class)
    public void PortNull() throws URLException
    {
        URL.Get("a", 0, "b");
    }
    
    @Test(expected = URLException.class)
    public void InstanceEmpty() throws URLException
    {
        URL.Get("a", 1, "");
    }
    
    @Test
    public void Valid() throws URLException
    {
        URL.Get("a", 1, "b");
    }
}
