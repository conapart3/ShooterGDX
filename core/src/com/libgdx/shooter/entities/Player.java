package com.libgdx.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

import static com.libgdx.shooter.game.ShooterGame.*;

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
    private int score;
    private Weapon weapon;
//    private boolean left,right,up,down;

    public Player() {
        create();
    }

    public void create() {
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
        x = 150 * SCALE_RATIO_X;
        y = 800 * SCALE_RATIO_Y;

        maxSpeed = 600f;
        knobPercentX = 0;
        knobPercentY = 0;
        health = 1000;
        alive = true;
//        left=right=up=down=false;

        xOffset = width/2+dirX*30;
        yOffset = height/2+dirY*30;
        weapon = new LightLaserCannon();
    }

    public void update(float dt) {
        if(health<1)
            alive = false;

//        dx = knobPercentX * maxSpeed;
//        dy = knobPercentY * maxSpeed;

        x += knobPercentX * maxSpeed * dt;
        y += knobPercentY * maxSpeed * dt;

//        xSpeed = maxSpeed* knobPercentX;
//        ySpeed = maxSpeed* knobPercentY;

//        if(up)
//            ySpeed += 100f;
//        if(down)
//            ySpeed -= 100f;
//        if(left)
//            xSpeed -= 100f;
//        if(right)
//            xSpeed += 100f;

//        x += xSpeed * dt;
//        y += ySpeed * dt;

//        xSpeed*=0.5;
//        ySpeed*=0.5;

//        xSpeed += knobPercentX * maxSpeed * dt;
//        ySpeed += knobPercentY * maxSpeed * dt;

        //apply acceleration to speed (dy and dx are acceleration)
//        xSpeed += dx*dt;
//        ySpeed += dy*dt;

        //position differs by the velocity
//        x += xSpeed * dt;
//        y += ySpeed * dt;

        if (y < GROUND_OFFSET)
            y = GROUND_OFFSET;
        if (x < 0)
            x = 0;
        if (x > 1850)
            x = 1850;
        if (y > PLAYER_CEILING_OFFSET)
            y = PLAYER_CEILING_OFFSET;

        bounds.x = x;
        bounds.y = y;

        stateTime += dt;
        currentFrame = playerAnimation.getKeyFrame(stateTime, true);
    }

    @Override
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addPoints(float points){
        this.score += points;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void addItem(Item item) {

    }
}
