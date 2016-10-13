package com.libgdx.shooter.levels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.libgdx.shooter.gamestates.GameState;

/**
 * Created by Conal on 17/11/2015.
 */
public class Level {

    protected Texture bgBack, bgMiddle, bgFront;
    protected String bgBackFilePath, bgMiddleFilePath, bgFrontFilePath;
    protected float srcX = 0f;
    protected float levelNumber;


    public Level(String bgBackFilePath, String bgMiddleFilePath, String bgFrontFilePath) {
        this.bgBackFilePath = bgBackFilePath;
        this.bgMiddleFilePath = bgMiddleFilePath;
        this.bgFrontFilePath = bgFrontFilePath;
    }

    public void create() {
        /** Load the background textures into the asset manager for this level. **/
        GameState.assetManager.load(bgBackFilePath, Texture.class);
        GameState.assetManager.load(bgMiddleFilePath, Texture.class);
        GameState.assetManager.load(bgFrontFilePath, Texture.class);

        /** Don't proceed unless assetmanager has finished loading. **/
        GameState.assetManager.finishLoading();

        bgBack = GameState.assetManager.get(bgBackFilePath, Texture.class);
        bgMiddle = GameState.assetManager.get(bgMiddleFilePath, Texture.class);
        bgFront = GameState.assetManager.get(bgFrontFilePath, Texture.class);

    }

    public void update(float dt) {
        srcX += 1;
    }

    public void render(SpriteBatch spriteBatch) {
        /** Draw the backgrounds and have them scroll within themselves based on srcX **/
        bgBack.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        spriteBatch.draw(bgBack, 0, 0, (int) (srcX / 3), (int) 0, bgBack.getWidth(), bgBack.getHeight());
        bgMiddle.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        spriteBatch.draw(bgMiddle, 0, -100, (int) (srcX), (int) 0, bgMiddle.getWidth(), bgMiddle.getHeight());
        bgFront.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        spriteBatch.draw(bgFront, 0, -75, (int) (srcX * 4), (int) 0, bgFront.getWidth(), bgFront.getHeight());
    }

    public void dispose() {
        bgBack.dispose();
        bgMiddle.dispose();
        bgFront.dispose();
    }

}
