package com.libgdx.shooter.entities.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.libgdx.shooter.gamestates.GameState;

/**
 * Created by Conal on 12/11/2015.
 */
public class Missile extends Bullet {

    public Missile() {
        super();
        damage = 500;
        maxSpeed = 500f;
    }

    @Override
    protected void setHitSound() {
        hitSound = GameState.assetManager.get("data/Sound/hitSoundMissile.wav");
    }

    @Override
    protected void setTexture() {
        texture = new Texture(Gdx.files.internal("data/missile.png"));
    }


}
