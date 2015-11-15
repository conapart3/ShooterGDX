package com.libgdx.shooter.entities.weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.shooter.entities.items.Item;
import com.libgdx.shooter.entities.Player;
import com.libgdx.shooter.gamestates.GameState;

/**
 * Created by Conal on 08/11/2015.
 */
public class Minigun extends Weapon{


    public final WeaponType type = WeaponType.MINIGUN;

    public Minigun(){
        super();
        damage = 25;
        rateOfFire = 0.1f;
    }


//    @Override
//    protected void setTexture(){
//        texture = new Texture(Gdx.files.internal("data/sprite_mirror_0.png"));
//    }

    @Override
    protected void setShootSound(){
//        shootSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/shootSoundMinigun.wav"));
        shootSound = GameState.assetManager.get("data/Sound/shootSoundMinigun.wav");
    }
    @Override
    protected void setPickupSound(){
//        pickupSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/pickupMinigun.wav"));
        pickupSound = GameState.assetManager.get("data/Sound/pickupMinigun.wav");
    }

    @Override
    public void attachToPlayer(Player player){
        super.attachToPlayer(player);
        System.out.println("Minigun");
    }

    @Override
    public WeaponType getType() {
        return type;
    }

    @Override
    public void useWeapon(Vector2 target) {

    }

}
