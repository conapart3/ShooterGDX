package com.libgdx.shooter.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.libgdx.shooter.entities.Player;
import com.libgdx.shooter.entities.SoundToPlay;

/**
 * Created by Conal on 17/11/2015.
 */
public class DoublePointsPickup extends Item {

    public DoublePointsPickup() {
        super();
        soundToPlay = SoundToPlay.PICKUP_SOUND_HEALTH;
    }

    @Override
    protected void setTexture() {
        texture = new Texture(Gdx.files.internal("data/pickupDoublePoints.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public void use(Player p) {
        p.setDoublePoints();
    }
}
