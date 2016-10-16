package com.libgdx.shooter.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by Conal on 21/11/2015.
 */
public class Animator implements Pool.Poolable {


    private Texture texture;
    private TextureRegion[] frames;
    private TextureRegion[][] textureRegions;
    private TextureRegion currentFrame;
    private Animation animation;
    private float stateTime = 0f;
    private float x, y, xSpeed;
    private int FRAME_COLS, FRAME_ROWS;
    private int index = 0;
    private boolean looping;
    private boolean alive;
    private int numFrames;


    public Animator(String filePath, int columns, int rows, boolean looping) {
        this.FRAME_ROWS = rows;
        this.FRAME_COLS = columns;
        this.looping = looping;

        texture = new Texture(Gdx.files.internal(filePath));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion[][] textureRegions = TextureRegion.split(texture, texture.getWidth() / FRAME_COLS, texture.getHeight() / FRAME_ROWS);

        numFrames = FRAME_COLS * FRAME_ROWS;
        frames = new TextureRegion[numFrames];
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = textureRegions[i][j];
            }
        }

        animation = new Animation(0.065f, frames);
    }


    public void create(float x, float y) {
        this.x = x;
        this.y = y;
        xSpeed = -250;
        alive = true;
        stateTime = 0;
    }


    public void update(float dt) {
        x += xSpeed * dt;
        stateTime += dt;
        currentFrame = animation.getKeyFrame(stateTime, looping);
        alive = !animation.isAnimationFinished(stateTime);
    }


    public void render(SpriteBatch sb) {
        if (currentFrame != null)
            sb.draw(currentFrame, x, y);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void reset() {
        x = -500;
        y = -500;
        stateTime = 0;
        alive = false;
        xSpeed = 0;
    }

    public void dispose() {
        texture.dispose();
        currentFrame.getTexture().dispose();
        for (int i = 0; i < frames.length; i++) {
            frames[i].getTexture().dispose();
        }
    }

}
