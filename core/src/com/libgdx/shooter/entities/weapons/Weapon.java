package com.libgdx.shooter.entities.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.shooter.entities.Player;
import com.libgdx.shooter.entities.items.Item;
import com.libgdx.shooter.gamestates.GameState;

/**
 * Created by Conal on 08/11/2015.
 */
public abstract class Weapon extends Item{

    protected float timeSinceLastFire;
    protected float rateOfFire;
    protected float damage;
    protected Sound shootSound;

    public abstract WeaponType getType();
    public abstract void useWeapon(Vector2 target);

    public Weapon(){
        super();
        setShootSound();
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x,y,width,height);
        super.create();
        isWeapon = true;
    }

    public void update(float dt){
        super.update(dt);
        timeSinceLastFire += dt;
        if (timeSinceLastFire > rateOfFire)
            timeSinceLastFire = rateOfFire;
    }

    protected void setShootSound(){
        shootSound = GameState.assetManager.get("data/Sound/shootSoundLightLaserCannon.wav");
    }

    public boolean isReadyToShoot(){
        if(timeSinceLastFire>=rateOfFire){
            timeSinceLastFire-=rateOfFire;
            return true;
        } else {
            return false;
        }
    }

    public void playShootSound(){
//        if(GameState.assetManager.isLoaded())
        shootSound.stop();
        shootSound.play(0.3f);
    }

    @Override
    public void dispose(){
        super.dispose();
        shootSound.dispose();
    }
}
