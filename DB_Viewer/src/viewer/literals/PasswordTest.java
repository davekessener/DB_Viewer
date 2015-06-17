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
        String enc = "e94f9e84e94dc6ce303d714c21bd8660";
        
        assertEquals(enc, Password.Encrypt(raw));
        assertEquals(raw, Password.Decrypt(enc));
    }
}
