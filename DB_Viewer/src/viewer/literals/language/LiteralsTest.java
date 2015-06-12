package viewer.literals.language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import viewer.service.Logger;

public class LiteralsTest
{
    @Test
    public void echoDB()
    {
        List<String> entries = new ArrayList<>(Language.DEFAULT.getEntries());
        
        Collections.sort(entries);
        
        for(String s : entries)
        {
            Logger.Log(s);
        }
    }
}
