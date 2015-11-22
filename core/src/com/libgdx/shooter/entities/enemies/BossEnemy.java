package com.libgdx.shooter.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.libgdx.shooter.entities.SpaceObject;
import com.libgdx.shooter.entities.weapons.MissileLauncher;
import com.libgdx.shooter.entities.weapons.Weapon;
import com.libgdx.shooter.gamestates.GameState;

import java.util.ArrayList;
import java.util.Random;

import static com.libgdx.shooter.game.ShooterGame.CEILING_OFFSET;
import static com.libgdx.shooter.game.ShooterGame.GROUND_OFFSET;

/**
 * Created by Conal on 08/11/2015.
 */
public class BossEnemy extends SpaceObject {

    private ArrayList<com.libgdx.shooter.entities.weapons.Weapon> weaponList;
    private Sound explosionSound;
    private int level;
    private boolean isShooting;
    private float dirLength;
    private Weapon weapon;


    public BossEnemy(){
        this.alive = false;
    }


    public BossEnemy(String filePath, int level){
        this.alive = false;
        texture = new Texture(Gdx.files.internal(filePath));
//        texture = GameState.assetManager.get("data/boss1.png");
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x+50,y+50,width-100,height-100);
        isShooting = false;
        dirX = 0f;
        dirY = 0f;
        this.level = level;
        health = 1000*level;
        explosionSound = GameState.assetManager.get("data/Sound/explosionShooterEnemy.wav");
        rand = new Random();
        weaponList = new ArrayList<Weapon>();
        x = -500;
        y = -500;
    }


    public void create(){
        x = 2000;
        y = 300;
        xSpeed = -100;
        ySpeed = rand.nextInt(100)-50;
        alive = true;
        dy = 0;
        dx = 0;
        health = 5000 + (1000*level);
        isShooting = false;
        dirX = 0f;
        dirY = 0f;
//        damage = 200;
        weapon = new MissileLauncher();
    }


    public void update(float dt, float targetX, float targetY){
//        for(int i=0; i<weaponList.size(); i++){
//            weaponList.get(i).update(dt);
////            isShooting=weaponList.get(0).isReadyToShoot();
//        }

        if(alive)
            weapon.update(dt);

        if (x < 1900) {
            dirX = 0.001f * (targetX - x - width / 2);
            dirY = 0.001f * (targetY - y - height / 2);
            dirLength = (float) Math.sqrt(dirX * dirX + dirY * dirY);
            dirX = dirX / dirLength;
            dirY = dirY / dirLength;

            isShooting = weapon.isReadyToShoot();
        }

        xOffset = width / 2 + dirX * 30;
        yOffset = height / 2 + dirY * 30;

        xSpeed += dx*dt;
        ySpeed += dy*dt;

        x+= xSpeed * dt;
        y+= ySpeed * dt;

        bounds.x = x+50;
        bounds.y = y+50;

        if(x<1000)
            xSpeed=0;
        if(y>= CEILING_OFFSET-(height/2) || y<= GROUND_OFFSET)
            ySpeed *= -1;
    }


    public void render(SpriteBatch sb){
//        sb.draw(texture,x,y,width/2,height/2,width,height,1,1,(float) rotation,0,0,width,height,false,false);
        sb.draw(texture,x,y);
    }

    @Override
    public void dispose(){
        if(texture!=null)
            texture.dispose();
    }

    public void playExplosion(){
        explosionSound.play();
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public boolean isShooting() {
        return isShooting;
    }

    public void setIsShooting(boolean isShooting) {
        this.isShooting = isShooting;
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
