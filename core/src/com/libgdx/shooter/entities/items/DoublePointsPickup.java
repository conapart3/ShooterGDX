package com.libgdx.shooter.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.libgdx.shooter.entities.Player;
import com.libgdx.shooter.gamestates.GameState;

/**
 * Created by Conal on 17/11/2015.
 */
public class DoublePointsPickup extends Item {


    public DoublePointsPickup(){
        super();
    }


    @Override
    protected void setPickupSound(){
        pickupSound = GameState.assetManager.get("data/Sound/pickupHealth.wav");
    }


    @Override
    protected void setTexture(){
        texture = new Texture(Gdx.files.internal("data/pickupDoublePoints.png"));
    }

    @Override
    public void use(Player p){
        p.setDoublePoints();
    }

}
