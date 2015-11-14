package com.libgdx.shooter.entities.enemies;

import com.badlogic.gdx.audio.Sound;
import com.libgdx.shooter.entities.SpaceObject;

import java.util.Random;

/**
 * Created by Conal on 14/11/2015.
 */
public class Enemy extends SpaceObject {

    protected Random rand;
    protected Sound explosionSound;
    protected int points;
    protected int health;

    public Enemy(){
        this.alive = false;
        rand = new Random();
        setTexture();
    }

    protected void setTexture(){

    }

}
