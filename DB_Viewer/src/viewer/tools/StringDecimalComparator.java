package viewer.tools;

import java.util.Comparator;

public class StringDecimalComparator implements Comparator<String>
{
    @Override
    public int compare(String s1, String s2)
    {
        try
        {
            return Double.valueOf(s1).compareTo(Double.valueOf(s2));
        }
        catch(NumberFormatException e)
        {
            return s1.compareTo(s2);
        }
    }
}
