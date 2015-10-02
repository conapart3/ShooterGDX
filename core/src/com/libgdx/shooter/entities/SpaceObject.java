package com.libgdx.shooter.entities;

/**
 * Created by Conal on 30/09/2015.
 */
public class SpaceObject {

    protected float x, y;
    protected float dx, dy;
    protected float speed, rotationSpeed, radians;

    protected int width, height;

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
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
}
