package viewer.literals.language;

import java.util.Locale;

public class Literals
{
    private static Language LANGUAGE = Language.DEFAULT;
    
    public static String Get(String s)
    {
        return LANGUAGE.get(s);
    }

    public static void Load(Locale l)
    {
        LANGUAGE = new Language(l);
    }
    
    private Literals() { }
}
