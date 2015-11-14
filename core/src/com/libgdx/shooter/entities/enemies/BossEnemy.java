package com.libgdx.shooter.entities.enemies;

import com.badlogic.gdx.audio.Sound;
import com.libgdx.shooter.entities.SpaceObject;

import java.util.ArrayList;

/**
 * Created by Conal on 08/11/2015.
 */
public class BossEnemy extends SpaceObject {

    private ArrayList<com.libgdx.shooter.entities.weapons.Weapon> weaponList;
    private Sound explosionSound;

    public BossEnemy(){

    }

    public void create(){

    }

    public void update(float dt){

    }

    public void render(){

    }

    public void playExplosion(){
        explosionSound.play();
    }
}
