package com.libgdx.shooter.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.libgdx.shooter.entities.Player;
import com.libgdx.shooter.gamestates.GameState;

/**
 * Created by Conal on 17/11/2015.
 */
public class OneHitKillPickup extends Item {

    public OneHitKillPickup() {
        super();
    }


    @Override
    protected void setPickupSound() {
        pickupSound = GameState.assetManager.get("data/Sound/pickupHealth.wav");
    }


    @Override
    protected void setTexture() {
        texture = new Texture(Gdx.files.internal("data/pickupOneHitKill.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }


    @Override
    public void use(Player p) {
        p.setOneHitKill();
    }

}
