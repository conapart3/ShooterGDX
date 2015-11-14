package com.libgdx.shooter.entities.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.shooter.entities.Player;
import com.libgdx.shooter.entities.items.Item;

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
    }

    public void update(float dt){
        super.update(dt);
        timeSinceLastFire += dt;
        if (timeSinceLastFire > rateOfFire)
            timeSinceLastFire = rateOfFire;
    }

    protected void setShootSound(){
        shootSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/laserFire.wav"));
    }

    public boolean isReadyToShoot(){
        if(timeSinceLastFire>=rateOfFire){
            timeSinceLastFire-=rateOfFire;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void attachToPlayer(Player player){
        pickupSound.play();
        player.setWeapon(this);
    }

    public void playShootSound(){
//        shootSound.stop();
        shootSound.play();
    }

    @Override
    public void dispose(){
        super.dispose();
        shootSound.dispose();
    }
}
