package com.libgdx.shooter.entities.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.shooter.entities.SoundToPlay;

/**
 * Created by Conal on 08/11/2015.
 */
public class HeavyLaserCannon extends Weapon {

    public final WeaponType type = WeaponType.HEAVY_LASER_CANNON;

    public HeavyLaserCannon() {
        super();
        damage = 100;
        rateOfFire = 0.5f;
        soundToPlay = SoundToPlay.SHOOT_SOUND_HEAVYLASERCANNON;
    }

    @Override
    protected void setTexture() {
        texture = new Texture(Gdx.files.internal("data/pickupHeavyLaser.png"));
    }

    //todo: weapons have both a pickup sound and a shoot sound. will need to change the way this works
    //todo: players should not pickup weapons, only PICKUPS.
//    @Override
//    protected void setShootSound() {
////        shootSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/shootSoundHeavyLaserCannon.wav"));
//        shootSound = GameState.assetManager.get("data/Sound/shootSoundHeavyLaserCannon.wav");
//    }

//    @Override
//    protected void setPickupSound() {
////        pickupSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/pickupHeavyLaser.wav"));
//        pickupSound = GameState.assetManager.get("data/Sound/pickupHeavyLaser.wav");
//    }

    @Override
    public WeaponType getType() {
        return type;
    }

    @Override
    public void useWeapon(Vector2 target) {

    }
}
