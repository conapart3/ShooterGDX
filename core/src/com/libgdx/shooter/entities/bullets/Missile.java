package com.libgdx.shooter.entities.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Conal on 12/11/2015.
 */
public class Missile extends Bullet{

    public Missile(){
        super();
        damage = 500;
        maxSpeed = 500f;
    }

    @Override
    protected void setHitSound(){
        hitSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/hitSoundBoss.wav"));
    }

    @Override
    protected void setTexture(){
        texture = new Texture(Gdx.files.internal("data/missile.png"));
    }


}
