package UI.Styles;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;

public class Fonts {
    public static Font SCRABBLE_FONT_1;

    static
    {
        SCRABBLE_FONT_1 = createFontFromFile("UI/Assets/Fonts/ClearSans-Bold.ttf");
    }

    private static Font createFontFromFile(String pathname)
    {
        try {
            File fontFile = new File(pathname);
            return Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(fontFile)).deriveFont(18.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
