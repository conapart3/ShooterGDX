package com.libgdx.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

import java.util.Random;

import static com.libgdx.shooter.game.ShooterGame.CEILING_OFFSET;
import static com.libgdx.shooter.game.ShooterGame.GROUND_OFFSET;

/**
 * Created by Conal on 28/10/2015.
 */
public class ShooterEnemy extends SpaceObject implements Pool.Poolable{

    private Random rand;
    private boolean isShooting;
    private float timeSinceLastFire, rateOfFire;
    private float bulletSpeed;
    private float dirX, dirY, dirLength;
    private double rotation;
    private Weapon weapon;

    public ShooterEnemy(){
        this.alive = false;
        rand = new Random();
//        texture = new Texture(Gdx.files.internal("data/shooterEnemy.png"));
        texture = new Texture(Gdx.files.internal("data/crab3d.png"));
        health = 200;
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x,y,width,height);
        isShooting = false;
        bulletSpeed = 500;
        rateOfFire = 1.6f;
        timeSinceLastFire = 0f;
        dirX = 0f;
        dirY = 0f;
    }

    //pass in the level - health = 200 + (200*level/2)
    public void create(int level){
        //max-min +1 +min
        x = rand.nextInt(3000-2000+1)+2000;
        y = rand.nextInt(1000-250+1)+250;
        xSpeed = (rand.nextInt(300-100 + 1)+100)*-1;
        ySpeed = rand.nextInt(50)-25;
        alive = true;
        dy = dx = 0;
        health = 100 + (200*level/2);
        isShooting = false;
        timeSinceLastFire = 0f;
        rateOfFire = 1.6f;
        dirX = 0f;
        dirY = 0f;
    }

    public void update(float dt, float targetX, float targetY){

        if(health<1)
            alive = false;

        //capping rate of fire so it doesn't go way above and allow many shots at once
        timeSinceLastFire+=dt;
        if(timeSinceLastFire > rateOfFire)
            timeSinceLastFire = rateOfFire;

        if(x<1900){
            dirX = targetX - x - width/2;
            dirY = targetY - y - height/2;
            dirLength = (float)Math.sqrt(dirX*dirX + dirY*dirY);
            dirX=dirX/dirLength;
            dirY=dirY/dirLength;
//            if(difference between targetY and y is only 50)
//                then move out of fire line
            if(timeSinceLastFire>=rateOfFire) {
                timeSinceLastFire-=rateOfFire;
                isShooting = true;
            } else {
                isShooting=false;
            }
        }
        rotation = (Math.atan2(dirY,dirX)*180.0d/Math.PI)-180.0f;
        xOffset = width/2 + dirX*30;
        yOffset = height/2 + dirY*30;

        xSpeed += dx*dt;
        ySpeed += dy*dt;

        x += xSpeed * dt;
        y += ySpeed * dt;

        bounds.x = x;
        bounds.y = y;

        if(x<-200)
            alive = false;
        if(x<1300)
            xSpeed=0;
        if(y>= CEILING_OFFSET || y<= GROUND_OFFSET)
            ySpeed *= -1;
    }

    public void render(SpriteBatch sb){
//        sb.draw(texture,x,y,width/2,height/2,width,height,1,1,(float)rotation);
        sb.draw(texture,x,y,width/2,height/2,width,height,1,1,(float) rotation,0,0,width,height,false,false);
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
        isShooting = false;
        timeSinceLastFire = 0f;
        rateOfFire=1.6f;
    }

    @Override
    public void dispose(){
        super.dispose();
    }

    public boolean isShooting() {
        return isShooting;
    }

    public void setIsShooting(boolean isShooting) {
        this.isShooting = isShooting;
    }

    public float getTimeSinceLastFire() {
        return timeSinceLastFire;
    }

    public void setTimeSinceLastFire(float timeSinceLastFire) {
        this.timeSinceLastFire = timeSinceLastFire;
    }

    public float getRateOfFire() {
        return rateOfFire;
    }

    public void setRateOfFire(float rateOfFire) {
        this.rateOfFire = rateOfFire;
    }

    public float getDirX() {
        return dirX;
    }

    public void setDirX(float dirX) {
        this.dirX = dirX;
    }

    public float getDirY() {
        return dirY;
    }

    public void setDirY(float dirY) {
        this.dirY = dirY;
    }
}
