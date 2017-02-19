package com.libgdx.shooter.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.libgdx.shooter.entities.Player;
import com.libgdx.shooter.entities.SoundToPlay;

/**
 * Created by Conal on 29/10/2015.
 */
public class HealthPickup extends com.libgdx.shooter.entities.items.Item {

    public HealthPickup() {
        super();
        soundToPlay = SoundToPlay.PICKUP_SOUND_HEALTH;
    }

    @Override
    protected void setTexture() {
        texture = new Texture(Gdx.files.internal("data/pickupHealth.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public void use(Player p) {
        p.addHealth(1000);
    }
}
