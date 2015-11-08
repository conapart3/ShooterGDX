package com.libgdx.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Conal on 29/10/2015.
 */
public class HealthPickup extends SpaceObject {

    public HealthPickup(){
        texture = new Texture(Gdx.files.internal("data/PowerUp1.png"));
    }

    public void pickMeUp(){

    }

}
