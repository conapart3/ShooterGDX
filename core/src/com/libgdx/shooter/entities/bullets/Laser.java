package com.libgdx.shooter.entities.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.libgdx.shooter.entities.Soundable;
import com.libgdx.shooter.entities.SoundToPlay;

/**
 * Created by Conal on 12/11/2015.
 */
public class Laser extends Bullet implements Soundable {

    public Laser() {
        super();
        damage = 100;
        maxSpeed = 700f;
        soundToPlay = SoundToPlay.HIT_SOUND_LASER;
    }

    @Override
    protected void setTexture() {
        texture = new Texture(Gdx.files.internal("data/laserRed.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
}
