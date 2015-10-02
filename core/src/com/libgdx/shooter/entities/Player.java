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

/**
 * Created by Conal on 26/09/2015.
 */
public class Player extends SpaceObject{

    private Texture playerSheet;
    private TextureRegion[] playerFrames;
    private TextureRegion currentFrame;
    private Animation playerAnimation;
    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private float knobPercentX, knobPercentY;
    private float stateTime;

    private float acceleratingTimer, acceleration, deceleration, maxSpeed;


    public Player(){
        create();
    }

    public void create(){
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

        acceleratingTimer = 0;
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
        x = 600;
        y = 600;
        radians = 0;
        rotationSpeed = 0;
        acceleration = 100f;
        deceleration = 20f;
        maxSpeed = 15f;
        knobPercentX = 0;
        knobPercentY = 0;
    }

    public void update(float dt){

//        //acceleration
//        if(leftPressed)
//            radians += rotationSpeed * dt;
//        if(rightPressed)
//            radians -= rotationSpeed * dt;
//
//        if(upPressed) {
//            dx += MathUtils.cos(radians) * acceleration * dt;
//            dy += MathUtils.sin(radians) * acceleration * dt;
//
//            acceleratingTimer += dt;
//
//            if (acceleratingTimer > 0.1f) {
//                acceleratingTimer = 0;
//            }
//        }else{
//            acceleratingTimer = 0;
//        }
//
//        //deceleration
//        float vec = (float) Math.sqrt(dx * dx + dy * dy);
//        if(vec>0){
//            dx -= (dx/vec)*deceleration*dt;
//            dy -= (dy/vec)*deceleration*dt;
//        }
//        if(vec > maxSpeed){
//            dx = (dx/vec)*maxSpeed;
//            dy = (dy/vec)*maxSpeed;
//        }
//
//        //set position
//        x += dx*dt;
//        y += dy*dt;

        x += knobPercentX * maxSpeed;
        y += knobPercentY * maxSpeed;

    }

    public void render(SpriteBatch spriteBatch, float deltaTime){

        stateTime += deltaTime;
        currentFrame = playerAnimation.getKeyFrame(stateTime, true);

        spriteBatch.draw(currentFrame, x, y);
    }


    public void setUpPressed(boolean t) {
        upPressed = t;
    }

    public void setDownPressed(boolean t) {
        downPressed = t;
    }

    public void setLeftPressed(boolean t) {
        leftPressed = t;
    }

    public void setRightPressed(boolean t) {
        rightPressed = t;
    }

    public void setKnobPosition(float percentX, float percentY){
        knobPercentX = percentX;
        knobPercentY = percentY;
    }
}
