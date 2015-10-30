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
    private int damage;

    public ParachuteBomber(){
        this.alive = false;
        rand = new Random();
        texture = new Texture(Gdx.files.internal("data/mine.png"));
        health = 50;
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x,y,width,height);
        damage = 200;
    }

    public void init(int level){
        //((max - min) + 1) + min
        x = rand.nextInt(3000-2000+1)+2000;
        y = rand.nextInt(1000-250+1)+250;
        xSpeed = (rand.nextInt(800-400+1)+400)*-1;
        ySpeed = rand.nextInt(100)-50;
        alive = true;
        dy = 0;
        dx = 0;
        health = 25 + (25*level/2);
        damage = 200 + (200*level/5);
    }

    public void update(float dt, float targetX, float targetY){
        if(health<1)
            alive = false;

//        if(Math.sqrt(Math.pow((targetY-y),2) + Math.pow((targetX-x),2)) < 600){
        if(x-targetX < 600){
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

        if(x<-200)
            alive = false;
        if(y== CEILING_OFFSET || y< GROUND_OFFSET)
            ySpeed *= -1;

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
        dx = 0;
        dy = 0;
        health = 0;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
