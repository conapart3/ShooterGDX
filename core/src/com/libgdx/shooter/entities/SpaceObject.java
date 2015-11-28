package com.libgdx.shooter.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;


/**
 * Created by Conal on 30/09/2015.
 */
public class SpaceObject {

    protected float x, y;
    protected float xSpeed, ySpeed;
    protected float radians;
    protected boolean strafe;

    /**
     * Proper speed, velocity and acceleration implementation.
     */
    protected Vector2 velocity = new Vector2();
    protected float maxSpeed = 500f;
    protected Vector2 acceleration = new Vector2();

    protected float dx, dy;
    protected Rectangle bounds;
    protected Texture texture;
    protected boolean alive;
    protected int health;
    protected float dirX, dirY;//for movement, and rotation, the directional vector for where it should point/move/shoot
    protected double rotation;
    protected float xOffset,yOffset;
    protected Random rand;
    protected boolean explosionFinished;

    protected int width, height;

    public boolean collides(Rectangle a){
        return a.overlaps(bounds);
    }

    public void dispose(){
        texture.dispose();
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    protected void move(float dt){
        xSpeed += dx*dt;
        ySpeed += dy*dt;

        x += xSpeed * dt;
        y += ySpeed * dt;

        bounds.x = x;
        bounds.y = y;
    }

    protected void speedLimit(){
        if(xSpeed>maxSpeed)
            xSpeed = maxSpeed;
        if(xSpeed<-maxSpeed)
            xSpeed = -maxSpeed;
        if(ySpeed>maxSpeed)
            ySpeed = maxSpeed;
        if(ySpeed<-maxSpeed)
            ySpeed = -maxSpeed;
    }
//
//    protected void randomStrafe(){
//        strafe = true;
//        radians = MathUtils.random(2 * 3.1415f);
//        dx = MathUtils.cos(radians)*100;
//        dy = MathUtils.sin(radians)*100;
//    }

    public void render(SpriteBatch sb){
        sb.draw(texture,x,y,width,height);

    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void takeDamage(int i) {
        this.health -= i;
        if(health<1)
            alive = false;
    }

    public float getxOffset() {
        return xOffset;
    }

    public void setxOffset(float xOffset) {
        this.xOffset = xOffset;
    }

    public float getyOffset() {
        return yOffset;
    }

    public void setyOffset(float yOffset) {
        this.yOffset = yOffset;
  }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean isExplosionFinished() {
        return explosionFinished;
    }

    public void setExplosionFinished(boolean explosionFinished) {
        this.explosionFinished = explosionFinished;
    }
}
