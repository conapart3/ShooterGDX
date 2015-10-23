package com.libgdx.shooter.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;


/**
 * Created by Conal on 30/09/2015.
 */
public class SpaceObject {

    protected float x, y;
    protected float xSpeed, ySpeed;
    protected Rectangle bounds;
    protected Texture texture;
    protected boolean alive;
    protected int health;

    protected int width, height;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public boolean collides(Rectangle a){
        return a.overlaps(bounds);
    }

    public void dispose(){
        texture.dispose();
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

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
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

    public float getySpeed() {
        return ySpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
    }

    public float getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public void takeDamage(int i) {
        this.health -= i;
    }
}
