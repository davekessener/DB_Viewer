package viewer.literals.language;

import java.io.File;
import java.util.Locale;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Resources
{
    public static final String D_RESOURCES = "resources/";
    public static final String D_LANG_FILES = D_RESOURCES + "lang/";
    public static final String D_IMGS_FILES = D_RESOURCES + "images/";
    public static final String D_STYLE_FILES = D_RESOURCES + "style/";
    public static final String S_LANG_EXT = ".json";
    public static final String S_IMG_EXT = ".png";
    public static final String S_STYLE_EXT = ".css";
    
    private static final String URI_FILE = "file:";
    
    public static final String I_ADD = D_IMGS_FILES + "add" + S_IMG_EXT;
    
    public static final String CSS_TABS = URI_FILE + D_STYLE_FILES + "tabs" + S_STYLE_EXT;
    
    public static String GetLanguageFile(Locale l)
    {
        return D_LANG_FILES + l.getCountry() + "_" + l.getLanguage() + S_LANG_EXT;
    }
    
    public static ImageView GetImageNode(String img)
    {
        File f = new File(img);
        Image i = new Image(f.toURI().toString());
        ImageView v = new ImageView();
        v.setImage(i);
        return v;
    }
    
    private Resources() { }
}
