package com.libgdx.shooter.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.libgdx.shooter.managers.GameStateManager;

import static com.libgdx.shooter.game.ShooterGame.WORLD_HEIGHT;
import static com.libgdx.shooter.game.ShooterGame.WORLD_WIDTH;

/**
 * Created by Conal on 25/10/2015.
 */
public class MenuState extends State {

    private final String title = "ShooterGDX";
    private SpriteBatch sb;
    private BitmapFont font;
    private TextButton textButton1, textButton2, textButton3;
    private TextButton.TextButtonStyle textButtonStyle;
    private Stage stage;

    public MenuState(GameStateManager gsm){
        super(gsm);
    }

    @Override
    public void create() {
        cam.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);

        sb = new SpriteBatch();

//        font = new BitmapFont(Gdx.files.internal("data/Fonts/outrider.fnt"), Gdx.files.internal("data/Fonts/outrider_0.png"), false);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/Fonts/Montserrat-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 72;
        font = generator.generateFont(parameter);
        generator.dispose();
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        textButton1 = new TextButton("Play", textButtonStyle);
        textButton2 = new TextButton("High Scores", textButtonStyle);
        textButton3 = new TextButton("Quit", textButtonStyle);

//        textButton1.setBounds(500f, 500f, 300, 300);
        textButton1.setPosition(500f,500f);
        textButton2.setPosition(500f,400f);
        textButton3.setPosition(500f,300f);

        stage = new Stage(viewport);
        stage.addActor(textButton1);
        stage.addActor(textButton2);
        stage.addActor(textButton3);

        Gdx.input.setInputProcessor(stage);

//        textButton1.setBackground(Gdx.files.internal("data/ship.png"));
    }

    @Override
    public void update(float delta) {
        handleInput();

    }

    @Override
    public void render() {
        sb.setProjectionMatrix(cam.combined);

        sb.begin();

        textButton1.draw(sb, 1);
        textButton2.draw(sb, 1);
        textButton3.draw(sb, 1);

        sb.end();
    }

    @Override
    public void handleInput() {
        if (textButton1.isPressed()){
            gameStateManager.setState(GameStateManager.GAME, 0);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
            gameStateManager.setState(GameStateManager.GAME,0);
    }

    @Override
    public void dispose() {
        sb.dispose();
        font.dispose();
        stage.dispose();
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height);
        cam.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
//        stage.getViewport().update(WORLD_WIDTH, (int)(WORLD_WIDTH * SCREEN_ASPECT_RATIO), false);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    private void initStage(){

    }
}
