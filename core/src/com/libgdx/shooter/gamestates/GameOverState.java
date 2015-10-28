package com.libgdx.shooter.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.libgdx.shooter.managers.GameStateManager;

import static com.libgdx.shooter.game.ShooterGame.WORLD_HEIGHT;
import static com.libgdx.shooter.game.ShooterGame.WORLD_WIDTH;

/**
 * Created by Conal on 23/10/2015.
 */
public class GameOverState extends State{

    private SpriteBatch spriteBatch;
    private String GAME_OVER = "GAME OVER";
    private float score;
    private BitmapFont font;
    private Label labelA, labelB;
    private Stage stage;

    public GameOverState(GameStateManager gsm, int score){
        super(gsm);
        this.score = score;
    }

    @Override
    public void create() {
        cam.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);

        spriteBatch = new SpriteBatch();

        //init font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/Fonts/Montserrat-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.minFilter = Texture.TextureFilter.Nearest;
        parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
        parameter.size = 72;
        font = generator.generateFont(parameter);
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        labelA = new Label("GAME OVER", labelStyle);
//        labelA.setFontScale(3);
        labelA.setPosition(100, 500);

        labelB = new Label("Your score was ", labelStyle);
//        labelB.setFontScale(3);
        labelB.setPosition(100, 400);

        stage = new Stage(viewport);
        stage.addActor(labelA);
        stage.addActor(labelB);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void update(float delta) {
        handleInput();
    }

    @Override
    public void render() {
        spriteBatch.setProjectionMatrix(cam.combined);
        spriteBatch.begin();

        labelB.setText("Your score was " +(int)score);

        labelA.draw(spriteBatch, 1);
        labelB.draw(spriteBatch, 1);


        spriteBatch.end();

    }

    @Override
    public void handleInput() {

        if (Gdx.input.justTouched()){
            gameStateManager.setState(GameStateManager.GAME, 0);
        }
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        stage.dispose();
        font.dispose();
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height);
        cam.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
