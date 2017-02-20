package com.libgdx.shooter.entities.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.libgdx.shooter.entities.Soundable;
import com.libgdx.shooter.entities.SoundToPlay;

/**
 * Created by Conal on 12/11/2015.
 */
public class Missile extends Bullet implements Soundable {

    public Missile() {
        super();
        damage = 500;
        maxSpeed = 500f;
        soundToPlay = SoundToPlay.HIT_SOUND_MISSILE;
    }

    @Override
    protected void setTexture() {
        texture = new Texture(Gdx.files.internal("data/missile.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }


}
