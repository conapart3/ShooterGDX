package com.libgdx.shooter.entities.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.shooter.gamestates.GameState;

/**
 * Created by Conal on 12/11/2015.
 */
public class MissileLauncher extends Weapon {

    public final WeaponType type = WeaponType.MISSILE_LAUNCHER;

    public MissileLauncher() {
        super();
        damage = 300;
        rateOfFire = 0.8f;
    }

    @Override
    protected void setTexture() {
        texture = new Texture(Gdx.files.internal("data/pickupMissile.png"));
    }

    @Override
    protected void setShootSound() {
//        shootSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/shootSoundMissile.wav"));
        shootSound = GameState.assetManager.get("data/Sound/shootSoundMissile.wav");
    }

    @Override
    protected void setPickupSound() {
//        pickupSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/pickupMissileLauncher.wav"));
        pickupSound = GameState.assetManager.get("data/Sound/pickupMissileLauncher.wav");
    }

    @Override
    public WeaponType getType() {
        return type;
    }

    @Override
    public void useWeapon(Vector2 target) {

    }


}
