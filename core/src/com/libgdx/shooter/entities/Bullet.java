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

    private int damage;
    private float xOffset, yOffset;
    private float maxSpeed;
    private boolean isShotFromEnemy;
//    private Texture texture2;

    public Bullet(){
        this.alive = false;
//        texture = new Texture(Gdx.files.internal("data/laser.png"));
//        texture = new Texture(Gdx.files.internal("data/laserRed.png"));
        texture = new Texture(Gdx.files.internal("data/enemyLaser.png"));
        xSpeed = 0;
        ySpeed = 0;
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x,y,width,height);
        damage = 50;
        maxSpeed=600f;
    }

    public void init(float startXPosition, float startYPosition, float dirX, float dirY, boolean isShotFromEnemy){
        this.x = startXPosition;
        this.y = startYPosition;
        this.dirX = dirX;
        this.dirY = dirY;
        alive = true;
        this.xSpeed = dirX*maxSpeed;
        this.ySpeed = dirY*maxSpeed;
        damage = 50;
        this.isShotFromEnemy = isShotFromEnemy;
    }

    public void update(float dt){
        rotation = (Math.atan2(dirY,dirX)*180.0d/Math.PI)-180.0f;

        x += dirX * maxSpeed * dt;
        y += dirY * maxSpeed * dt;

        bounds.x = x;
        bounds.y = y;

        if(x>2000)
            alive = false;
    }

    public void render(SpriteBatch sb){
//        sb.draw(texture,x,y);
//        if(!isShotFromEnemy) {
        sb.draw(texture, x, y, width / 2, height / 2, width, height, 1, 1, (float) rotation,
                0, 0, width, height, false, false);
//        } else {
//            sb.draw(texture2, x, y, width / 2, height / 2, width, height, 1, 1, (float) rotation,
//                    0, 0, width, height, false, false);
////        }
    }

    @Override
    public void reset() {
        x = -50;
        y = 0;
        alive = false;
        xSpeed = 0;
        ySpeed = 0;
        damage = 0;
    }

    @Override
    public void dispose(){
        super.dispose();
//        texture2.dispose();
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isShotFromEnemy() {
        return isShotFromEnemy;
    }

    public void setIsShotFromEnemy(boolean isShotFromEnemy) {
        this.isShotFromEnemy = isShotFromEnemy;
    }
}
