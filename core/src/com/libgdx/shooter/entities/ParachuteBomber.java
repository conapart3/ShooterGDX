package com.libgdx.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

import java.util.Random;

/**
 * Created by Conal on 22/10/2015.
 */
public class ParachuteBomber extends SpaceObject implements Pool.Poolable {

    private float xSpeed, ySpeed;
    private Random rand;
    private int points = 100;

    public ParachuteBomber(){
        this.alive = false;
        rand = new Random();
        texture = new Texture(Gdx.files.internal("data/mine.png"));
        health = 50;
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x,y,texture.getWidth(),texture.getHeight());
    }

    public void init(){
        x = rand.nextInt(3000-2000+1)+2000;
        y = rand.nextInt(1000-0+1)+0;
        xSpeed = (rand.nextInt(500-100+1)+100)*-1;
        ySpeed = rand.nextInt(100)-50;
        alive = true;
    }

    public void update(float dt){
        x += xSpeed * dt;
        y += ySpeed * dt;

        bounds.x = x;
        bounds.y = y;

        if(x<-100)
            alive = false;
        if(y>1100 || y< -50)
            alive = false;
    }

    public void render(SpriteBatch sb){
        sb.draw(texture, x, y);
    }


    @Override
    public void reset() {
        x = -100;
        y = -100;
        alive = false;
        xSpeed = 0;
        ySpeed = 0;
    }
}
