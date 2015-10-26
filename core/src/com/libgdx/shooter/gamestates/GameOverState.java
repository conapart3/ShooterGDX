package com.libgdx.shooter.gamestates;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    public GameOverState(GameStateManager gsm){
        super(gsm);
    }

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void update(float delta) {
        handleInput();
    }

    @Override
    public void render() {
        spriteBatch.setProjectionMatrix(cam.combined);
        spriteBatch.begin();

        spriteBatch.end();

    }

    @Override
    public void handleInput() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
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
