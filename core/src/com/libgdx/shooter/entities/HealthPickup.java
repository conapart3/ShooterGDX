package com.libgdx.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Conal on 29/10/2015.
 */
public class HealthPickup extends Item {

    public HealthPickup(){
//        texture = new Texture(Gdx.files.internal("data/powerupFin.png"));
        texture = new Texture(Gdx.files.internal("data/healthPickup.png"));
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x,y,width,height);
        pickupSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/healthPickup.wav"));
        create();
    }

    @Override
    public void attachToPlayer(Player player) {
        pickupSound.play();
        player.setHealth(1000);
        System.out.println("HEALTH PICKUP");
    }
//
//    @Override
//    public void create(){
//        super.create();
//    }
//
//    @Override
//    public void update(float dt){
//        super.update(dt);
//    }
//
//    @Override
//    public void render(SpriteBatch sb){
//        super.render(sb);
//    }
}
