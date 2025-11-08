package UI.Styles;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;

public class Fonts {
    public static Font SCRABBLE_FONT_1;

    static
    {
        try {
            File scrabble_font_1File = new File("UI/Assets/Fonts/ClearSans-Bold.ttf");
            SCRABBLE_FONT_1 = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(scrabble_font_1File)).deriveFont(18.0f);
        } catch (Exception e) { //implement more detailed handling later
            e.printStackTrace();
        }
    }
}
