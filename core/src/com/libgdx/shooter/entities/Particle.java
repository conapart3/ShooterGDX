package com.libgdx.shooter.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Conal on 21/11/2015.
 */
public class Particle extends SpaceObject {

    private float timer;
    private float time;
    private boolean remove;
    private float radians;

    public Particle(float x, float y){
        this.x = x;
        this.y = y;
        width = height = 2;
        maxSpeed = 50;
        radians = MathUtils.random(2*3.1415f);
        dx = MathUtils.cos(radians)*maxSpeed;
        dy = MathUtils.sin(radians)*maxSpeed;

        timer = 0;
        timer = 1;
    }

    public boolean shouldRemove(){
        return remove;
    }

    public void update(float dt){
        x += dx * dt;
        y += dy * dt;

        timer += dt;
        if(timer > time)
            remove = true;
    }

    public void render(ShapeRenderer sr){
        sr.setColor(1,1,1,1);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.circle(x-width/2, y-width/2, width/2);
        sr.end();
    }
}
