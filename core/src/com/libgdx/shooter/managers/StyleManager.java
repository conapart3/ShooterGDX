package com.libgdx.shooter.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Conal on 13/10/2016.
 */
public class StyleManager {

    /**
     * Create all styles here and make accessible to public so the same styles are created once.
     **/

    public static BitmapFont font;
    public static TextButton.TextButtonStyle textButtonStyle;

    static {
        init();
    }

    public static void init() {
//        font = new BitmapFont(Gdx.files.internal("data/Fonts/outrider.fnt"), Gdx.files.internal("data/Fonts/outrider_0.png"), false);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/Fonts/Montserrat-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 72;
        font = generator.generateFont(parameter);
        generator.dispose();
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
    }
}
