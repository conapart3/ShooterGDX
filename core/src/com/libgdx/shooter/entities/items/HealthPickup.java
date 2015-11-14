package com.libgdx.shooter.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.libgdx.shooter.entities.Player;

/**
 * Created by Conal on 29/10/2015.
 */
public class HealthPickup extends com.libgdx.shooter.entities.items.Item {


    public HealthPickup(){
        super();
    }

    @Override
    protected void setPickupSound(){
        pickupSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/pickupHealth.wav"));
    }

    @Override
    protected void setTexture(){
        texture = new Texture(Gdx.files.internal("data/healthPickup.png"));
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
