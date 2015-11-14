package com.libgdx.shooter.entities.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Conal on 12/11/2015.
 */
public class Laser extends Bullet{

    public Laser(){
        super();
        damage = damage/2;
        maxSpeed = 700f;
    }

    @Override
    protected void setHitSound(){
        hitSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/hitSoundLaser.wav"));
    }

    @Override
    protected void setTexture(){
        texture = new Texture(Gdx.files.internal("data/laserRed.png"));
    }
}
