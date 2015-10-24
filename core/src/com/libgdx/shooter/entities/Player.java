package com.libgdx.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.libgdx.shooter.game.ShooterGame;

/**
 * Created by Conal on 26/09/2015.
 */
public class Player extends SpaceObject {


    private TextureRegion[] playerFrames;
    private TextureRegion currentFrame;
    private Animation playerAnimation;
    private float knobPercentX, knobPercentY;
    private float stateTime;
    private float maxSpeed;
    private int lives;
    private int score;

    public Player() {
        init();
    }

    public void init() {
        int FRAME_COLS = 8;
        int FRAME_ROWS = 1;

        texture = new Texture(Gdx.files.internal("data/shipAnimation.png"));
        TextureRegion[][] textureRegions = TextureRegion.split(texture, texture.getWidth() / FRAME_COLS, texture.getHeight() / FRAME_ROWS);
        playerFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];

        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                playerFrames[index++] = textureRegions[i][j];
            }
        }
        playerAnimation = new Animation(0.065f, playerFrames);

        width = texture.getWidth() / FRAME_COLS;
        height = texture.getHeight() / FRAME_ROWS;
        bounds = new Rectangle(x, y, width, height);

        //starting position
        x = 100 * ShooterGame.SCALE_RATIO_X;
        y = 800 * ShooterGame.SCALE_RATIO_Y;

        maxSpeed = 600f;
        knobPercentX = 0;
        knobPercentY = 0;
        health = 100;
        lives = 3;
        alive = true;
    }

    public void update(float dt) {
        if(lives==0)
            alive = false;

        x += knobPercentX * maxSpeed * dt;
        y += knobPercentY * maxSpeed * dt;

        if (y < 0)
            y = 0;
        if (x < 0)
            x = 0;
        if (x > 1850)
            x = 1850;
        if (y > 1020)
            y = 1020;

        bounds.x = x;
        bounds.y = y;

        stateTime += dt;
        currentFrame = playerAnimation.getKeyFrame(stateTime, true);
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(currentFrame, x, y);
    }

    @Override
    public void dispose(){
        super.dispose();
        currentFrame.getTexture().dispose();
        for(int i=0; i<playerFrames.length; i++){
            playerFrames[i].getTexture().dispose();
        }
    }

    public void setKnobPosition(float percentX, float percentY) {
        knobPercentX = percentX;
        knobPercentY = percentY;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void removeLife(){
        lives--;
    }

    public void addLife(){
        lives++;
    }

    public int getScore() {
        return (int)score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addPoints(float points){
        this.score += points;
    }

}
