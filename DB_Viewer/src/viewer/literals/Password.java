package viewer.literals;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Password
{
    private static final Password INSTANCE = new Password();
    
    public static final String PASSWORD = Decrypt("3cbe7e4b8ef7ecd02b83f9d2edfa23a1");
    
    private Key key_;
    private Cipher cipher_;
    
    public static String Encrypt(String s) { return INSTANCE.encrypt(s); }
    public static String Decrypt(String s) { return INSTANCE.decrypt(s); }
    
    private Password()
    {
        try
        {
            key_ = new SecretKeySpec("R?83Hkd?7%dC%G_v".getBytes(), "AES");
            cipher_ = Cipher.getInstance("AES");
        }
        catch(NoSuchAlgorithmException | NoSuchPaddingException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private String encrypt(String s)
    {
        try
        {
            cipher_.init(Cipher.ENCRYPT_MODE, key_);
            byte[] encrypted = cipher_.doFinal(s.getBytes());
            return toString(encrypted);
        }
        catch(Throwable e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        
        return null;
    }
    
    private String decrypt(String s)
    {
        try
        {
            cipher_.init(Cipher.DECRYPT_MODE, key_);
            return new String(cipher_.doFinal(toBytes(s)));
        }
        catch(Throwable e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        
        return null;
    }

    
    private static final byte[] toBytes(String s)
    {
        byte[] a = new byte[s.length() / 2];

        for(int i = 0 ; i < a.length ; ++i)
        {
            a[i] = (byte) (fromChar(s.charAt(2 * i)) * 16 + fromChar(s.charAt(2 * i + 1)));
        }
        
        return a;
    }
    
    private static final int fromChar(char c) { return (c >= '0' && c <= '9' ? (c - '0') : c - 'a' + 10); }
    
    private static final String toString(byte[] a)
    {
        StringBuilder sb = new StringBuilder();
        
        for(byte v : a)
        {
            sb.append(String.format("%02x", v));
        }
        
        return sb.toString();
    }
}
