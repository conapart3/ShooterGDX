package com.libgdx.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.libgdx.shooter.game.ShooterGame;

import java.util.Random;

import static com.libgdx.shooter.game.ShooterGame.CEILING_OFFSET;
import static com.libgdx.shooter.game.ShooterGame.GROUND_OFFSET;

/**
 * Created by Conal on 22/10/2015.
 */
public class ParachuteBomber extends SpaceObject implements Pool.Poolable {

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
        //((max - min) + 1) + min
        x = rand.nextInt(3000-2000+1)+2000;
        y = rand.nextInt(1000-250+1)+250;
        xSpeed = (rand.nextInt(500-100+1)+100)*-1;
        ySpeed = rand.nextInt(100)-50;
        alive = true;
        dy = 0;
        dx = 0;
    }

    public void update(float dt, float targetX, float targetY){
        if(Math.sqrt(Math.pow(Math.abs(targetY-y),2) + Math.pow(Math.abs(targetX-x),2)) < 400){
//            dx = (targetX - x);
            dy = (targetY - y);
        }else{
//            dx=0;
            dy=0;
        }

        //apply acceleration to speed (dy and dx are acceleration)
        xSpeed += dx*dt;
        ySpeed += dy*dt;

        //position differs by the velocity
        x += xSpeed * dt;
        y += ySpeed * dt;

        bounds.x = x;
        bounds.y = y;

        if(x<-50)
            alive = false;
        if(y== CEILING_OFFSET || y< GROUND_OFFSET)
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
