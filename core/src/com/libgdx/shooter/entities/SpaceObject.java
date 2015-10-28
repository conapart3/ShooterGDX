package com.libgdx.shooter.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;


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

    protected int width, height;

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
}
