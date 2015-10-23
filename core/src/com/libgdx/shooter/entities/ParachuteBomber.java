package com.libgdx.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

/**
 * Created by Conal on 22/10/2015.
 */
public class ParachuteBomber extends SpaceObject {

    private Texture texture;
    private float xSpeed, ySpeed;
    private Random rand;
    private float health;

    public ParachuteBomber(){
        rand = new Random();
        generate();
        init();
    }

    public void init(){
        texture = new Texture(Gdx.files.internal("data/mine.png"));
        health = 50;
    }

    public void update(float dt){
        x += xSpeed * dt;
        y += ySpeed * dt;

        if(x<-100)
            generate();
    }

    public void render(SpriteBatch spriteBatch){
        spriteBatch.draw(texture, x, y);
    }

    private void generate(){
        //nextInt(max-min+1)+min
        x = rand.nextInt(3000-2000+1)+2000;
        y = rand.nextInt(1000-0+1)+0;
        xSpeed = (rand.nextInt(500-100+1)+100)*-1;
        ySpeed = rand.nextInt(100)-50;
    }
}
