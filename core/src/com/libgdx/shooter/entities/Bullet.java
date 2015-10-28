package com.libgdx.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;


/**
 * Created by Conal on 22/10/2015.
 */
public class Bullet extends SpaceObject implements Pool.Poolable{

    private float xSpeed, ySpeed;
    private float lifeTimer, lifeTime;
    private float damage;

    public Bullet(){
        this.alive = false;
        lifeTimer = 0f;
        lifeTime = 4f;
//        texture = new Texture(Gdx.files.internal("data/laser.png"));
        texture = new Texture(Gdx.files.internal("data/laserRed.png"));
        xSpeed = 0;
        ySpeed = 0;
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x,y,width,height);
        damage = 50;
    }

    public void init(float startXPosition, float startYPosition, float xSpeed, float ySpeed){
        this.x = startXPosition;
        this.y = startYPosition;
        alive = true;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public void update(float dt){
        x += xSpeed * dt;
        y += ySpeed * dt;

        bounds.x = x;
        bounds.y = y;

        if(x>2000)
            alive = false;
    }

    public void render(SpriteBatch sb){
        sb.draw(texture, x, y);
    }

    @Override
    public void reset() {
        x = -50;
        y = 0;
        alive = false;
        xSpeed = 0;
        ySpeed = 0;
    }

}
