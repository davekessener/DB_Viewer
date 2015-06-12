package viewer.literals;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PasswordTest
{
    @Test
    public void testRuntimeSymmetry()
    {
        String raw = "abc123";
        String enc = Password.Encrypt(raw);
        String dec = Password.Decrypt(enc);
        
        assertEquals(raw, dec);
    }
    
    @Test
    public void testStaticSymmetry()
    {
        String raw = "hello world";
        String enc = "cffa77e566c903616a93d64327e2d26e";

        assertEquals(enc, Password.Encrypt(raw));
        assertEquals(raw, Password.Decrypt(enc));
    }
}
