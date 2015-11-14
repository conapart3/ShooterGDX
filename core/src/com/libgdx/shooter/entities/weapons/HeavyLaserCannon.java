package com.libgdx.shooter.entities.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.shooter.entities.items.Item;
import com.libgdx.shooter.entities.Player;

/**
 * Created by Conal on 08/11/2015.
 */
public class HeavyLaserCannon extends Weapon {

    public final WeaponType type = WeaponType.HEAVY_LASER_CANNON;

    public HeavyLaserCannon(){
        super();
        damage = 100;
        rateOfFire = 0.5f;
    }


    @Override
    protected void setTexture(){
        texture = new Texture(Gdx.files.internal("data/heavyCannon.png"));
    }

        @Override
    protected void setShootSound(){
        shootSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/shootSoundHeavyLaserCannon.wav"));
    }

    @Override
    protected void setPickupSound(){
        pickupSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/pickupHeavyLaser.wav"));
    }

    @Override
    public WeaponType getType() {
        return type;
    }

    @Override
    public void useWeapon(Vector2 target) {

    }

    @Override
    public void attachToPlayer(Player player){
        super.attachToPlayer(player);
        System.out.println("HEAVY CANNON PICKUP");
    }
}
