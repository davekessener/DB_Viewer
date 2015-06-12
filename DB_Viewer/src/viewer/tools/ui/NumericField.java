package viewer.tools.ui;

import java.util.regex.Pattern;

import javafx.scene.control.TextField;

public class NumericField extends TextField
{
    public int getValue()
    {
        return getText().isEmpty() ? 0 : Integer.parseInt(getText());
    }
    
    @Override
    public void replaceText(int i1, int i2, String text)
    {
        if(isValid(text))
        {
            super.replaceText(i1, i2, text);
        }
    }

    @Override
    public void replaceSelection(String text)
    {
        if(isValid(text))
        {
            super.replaceSelection(text);
        }
    }

    private static boolean isValid(String text)
    {
        return VALID_PTR.matcher(text).matches();
    }
    
    private static final Pattern VALID_PTR = Pattern.compile("\\d*");
}