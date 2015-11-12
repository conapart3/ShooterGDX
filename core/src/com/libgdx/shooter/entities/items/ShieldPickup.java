package com.libgdx.shooter.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.libgdx.shooter.entities.Player;

/**
 * Created by Conal on 08/11/2015.
 */
public class ShieldPickup extends com.libgdx.shooter.entities.items.Item {

    public ShieldPickup(){
//        texture = new Texture(Gdx.files.internal("data/PowerUp1.png"));
        texture = new Texture(Gdx.files.internal("data/power_up_shield.png"));
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x,y,width,height);
        pickupSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/pickup2.wav"));
        super.create();
    }

    @Override
    public void attachToPlayer(Player player) {
        pickupSound.play();
        System.out.println("SHIELD PICKUP");

    }
}
