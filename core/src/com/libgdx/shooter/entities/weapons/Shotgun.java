package com.libgdx.shooter.entities.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.shooter.entities.items.Item;
import com.libgdx.shooter.entities.Player;

/**
 * Created by Conal on 08/11/2015.
 */
public class Shotgun extends Item implements Weapon{

    public Shotgun(){
//        texture = new Texture(Gdx.files.internal("data/powerupFin.png"));
        texture = new Texture(Gdx.files.internal("data/Shotgun.png"));
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x,y,width,height);
        pickupSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/pickup2.wav"));
        create();
    }

    @Override
    public void attachToPlayer(Player player) {
        pickupSound.play();
//        player.setHealth(1000);
        System.out.println("SHOTGUN PICKUP");
    }

    @Override
    public void useWeapon(Vector2 target) {

    }
}
