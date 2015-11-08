package com.libgdx.shooter.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;


/**
 * Created by Conal on 30/09/2015.
 */
public class SpaceObject {

    protected float x, y;
    protected float xSpeed, ySpeed;
    protected float dx, dy;
    protected Rectangle bounds;
    protected Texture texture;
    protected boolean alive;
    protected int health;
    protected float dirX, dirY;//for movement, and rotation, the directional vector for where it should point/move/shoot
    protected double rotation;
    protected float xOffset,yOffset;
    protected Random rand;

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
//
//    public void move(float dt){
//        xSpeed += dx*dt;
//        ySpeed += dy*dt;
//
//        x += xSpeed * dt;
//        y += ySpeed * dt;
//
//        bounds.x = x;
//        bounds.y = y;
//
////        if(dx>0)
////            dx--;
////        else if(dx<0)
////            dx++;
////
////        if(dy>0)
////            dy--;
////        else if(dy<0)
////            dy++;
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
}
