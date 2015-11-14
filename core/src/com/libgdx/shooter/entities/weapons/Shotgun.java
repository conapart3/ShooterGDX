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
public class Shotgun extends Weapon{

    public final WeaponType type = WeaponType.SHOTGUN;

    public Shotgun(){
        super();
        damage = 50;
        rateOfFire = 0.8f;
    }

    @Override
    protected void setTexture(){
        texture = new Texture(Gdx.files.internal("data/Shotgun.png"));
    }

        @Override
    protected void setShootSound(){
        shootSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/shootSoundShotgun.wav"));
    }

        @Override
    protected void setPickupSound(){
        pickupSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/pickupShotgun.wav"));
    }

    @Override
    public void attachToPlayer(Player player){
        super.attachToPlayer(player);
        System.out.println("Shotgun");
    }

    @Override
    public WeaponType getType() {
        return type;
    }

    @Override
    public void useWeapon(Vector2 target) {

    }
}
