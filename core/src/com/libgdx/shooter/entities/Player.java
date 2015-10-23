package com.libgdx.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Conal on 26/09/2015.
 */
public class Player extends SpaceObject{

    private Texture playerSheet;
    private TextureRegion[] playerFrames;
    private TextureRegion currentFrame;
    private Animation playerAnimation;
    private float knobPercentX, knobPercentY;
    private float stateTime;
    private float health;
    private float maxSpeed;

    public Player(){
        init();
    }

    public void init(){
        int FRAME_COLS = 8;
        int FRAME_ROWS = 1;

        playerSheet = new Texture(Gdx.files.internal("data/shipAnimation.png"));
        TextureRegion[][] textureRegions = TextureRegion.split(playerSheet,playerSheet.getWidth()/FRAME_COLS, playerSheet.getHeight()/FRAME_ROWS);
        playerFrames = new TextureRegion[FRAME_COLS*FRAME_ROWS];

        int index = 0;
        for(int i = 0; i< FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                playerFrames[index++] = textureRegions[i][j];
            }
        }
        playerAnimation = new Animation(0.065f, playerFrames);

        //starting position
        x = 100;
        y = 600;

        radians = 0;
        rotationSpeed = 0;
        maxSpeed = 500f;
        knobPercentX = 0;
        knobPercentY = 0;
        health = 100;
    }

    public void update(float dt){
        x += knobPercentX * maxSpeed * dt;
        y += knobPercentY * maxSpeed * dt;

        if(y<0)
            y=0;
        if(x<0)
            x=0;
        if(x>1850)
            x=1850;
        if(y>1020)
            y=1020;

        stateTime += dt;
        currentFrame = playerAnimation.getKeyFrame(stateTime, true);

    }

    public void render(SpriteBatch spriteBatch){
        spriteBatch.draw(currentFrame, x, y);
    }

    public void setKnobPosition(float percentX, float percentY){
        knobPercentX = percentX;
        knobPercentY = percentY;
    }
}
