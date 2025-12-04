package UI.Styles;

import java.awt.Font;
import java.io.InputStream;

public class Fonts {
    public static final float DEFAULT_FONT_SIZE = 18.0f;
    public static Font SCRABBLE_FONT_1;

    static {
        SCRABBLE_FONT_1 = loadFont("/resources/ClearSans-Bold.ttf", DEFAULT_FONT_SIZE);
    }

    private static Font loadFont(String resourcePath, float size) {
        try
        {
            try (InputStream is = Fonts.class.getResourceAsStream(resourcePath))
            {
                if (is != null) {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
                    is.close();
                    return font;
                }
            }
        } 
        catch (Exception e) 
        { e.printStackTrace(); }

        System.err.println("Failed to load font from " + resourcePath + ", using default font.");
        return new Font("SansSerif", Font.PLAIN, (int) size);
    }
}
