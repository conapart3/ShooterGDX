package com.libgdx.shooter.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.libgdx.shooter.entities.Player;
import com.libgdx.shooter.gamestates.GameState;

/**
 * Created by Conal on 29/10/2015.
 */
public class HealthPickup extends com.libgdx.shooter.entities.items.Item {


    public HealthPickup(){
        super();
    }


    @Override
    protected void setPickupSound(){
        pickupSound = GameState.assetManager.get("data/Sound/pickupHealth.wav");
    }


    @Override
    protected void setTexture(){
        texture = new Texture(Gdx.files.internal("data/pickupHealth.png"));
    }

    @Override
    public void use(Player p){
        p.addHealth(1000);
    }
}
