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
public class Minigun extends Item implements Weapon{


    public Minigun(){
//        texture = new Texture(Gdx.files.internal("data/PowerUp1.png"));
        texture = new Texture(Gdx.files.internal("data/sprite_mirror_0.png"));
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x,y,width,height);
        pickupSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/pickup2.wav"));
        create();
    }

    @Override
    public void attachToPlayer(Player player){
        pickupSound.play();
//        player.getWeapon().setRapidFire();
        System.out.println("RAPID FIRE PICKUP");
    }

    @Override
    public void useWeapon(Vector2 target) {

    }

}
