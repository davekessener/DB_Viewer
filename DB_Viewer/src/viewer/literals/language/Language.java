package viewer.literals.language;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Locale;
import java.util.Map;

import viewer.exception.ConfigError;

class Language
{
    public static final Locale DEFAULT_LOCALE = Locale.US;
    public static final Language DEFAULT = new DefaultLanguage(DEFAULT_LOCALE);
    
    private Map<String, String> data_;
    
    public Language(Locale l)
    {
        String fn = Resources.GetLanguageFile(l);
        File f = new File(fn);
        
        assert f.exists() : "Precondition violated: '" + fn + "'.exists()";
        
        try
        {
            data_ = Node.Read(new FileReader(f)).flatten();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
            
            throw new ConfigError();
        }
    }
    
    public boolean knows(String s)
    {
        return data_.containsKey(s);
    }
    
    public String get(String s)
    {
        return knows(s) ? data_.get(s) : DEFAULT.get(s);
    }
    
    private static class DefaultLanguage extends Language
    {
        public DefaultLanguage(Locale l)
        {
            super(l);
        }
        
        @Override
        public String get(String s)
        {
            return knows(s) ? super.get(s) : s;
        }
    }
}
