package viewer.literals.language;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class StringsTest
{
    @Test
    public void generate() throws FileNotFoundException
    {
        Map<String, String> m = Node.Read(new FileReader(new File(Resources.GetLanguageFile(Language.DEFAULT_LOCALE)))).flatten();
        List<String> l = new ArrayList<>(m.keySet());
        
        Collections.sort(l);
        
        for(String s : l)
        {
            StringBuilder sb = new StringBuilder();
            
            for(String p : s.split("\\."))
            {
                sb.append('_').append(p);
            }
            
            System.out.println("public static final String " + sb.substring(1).toUpperCase() + " = \"" + s + "\";");
        }
    }
}
