package com.libgdx.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Conal on 08/11/2015.
 */
public class LightLaserCannon extends Item implements Weapon{

    private float damage = 50;
    private float rateOfFire = 0.2f;
    private float rapidFireExpiryTime = 20f;
    private boolean rapidFire;

    public LightLaserCannon(){
//        texture = new Texture(Gdx.files.internal("data/PowerUp1.png"));
        texture = new Texture(Gdx.files.internal("data/lightCannon.png"));
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x,y, width,height);
        rapidFire=false;
        pickupSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/pickup2.wav"));
        super.create();
    }

    @Override
    public void useWeapon(Vector2 target){

    }

    @Override
    public void setRapidFire() {
        rapidFire=true;
        rateOfFire = rateOfFire/2;
    }

    @Override
    public void attachToPlayer(Player player) {
        pickupSound.play();
        player.setWeapon(this);
        System.out.println("LIGHT CANNON PICKUP");

    }
}
