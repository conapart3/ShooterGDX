package com.libgdx.shooter.entities.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.libgdx.shooter.entities.Player;
import com.libgdx.shooter.entities.SpaceObject;
import com.libgdx.shooter.entities.weapons.*;
import com.libgdx.shooter.entities.weapons.HeavyLaserCannon;
import com.libgdx.shooter.entities.weapons.LightLaserCannon;
import com.libgdx.shooter.gamestates.GameState;

import java.util.Random;

import static com.libgdx.shooter.game.ShooterGame.CEILING_OFFSET;
import static com.libgdx.shooter.game.ShooterGame.GROUND_OFFSET;

/**
 * Created by Conal on 08/11/2015.
 */
public class Item extends SpaceObject {


    protected Sound pickupSound;//set default item pickupsound, and sub classes can use their own
    protected boolean isWeapon;

    public Item(){
        setPickupSound();
        setTexture();
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x,y,width,height);
        create();
        isWeapon = false;
    }


    protected void setPickupSound(){
        pickupSound = GameState.assetManager.get("data/Sound/pickupShotgun.wav");
    }


    protected void setTexture(){
        texture = new Texture(Gdx.files.internal("data/sprite_mirror_0.png"));
    }


    public void create(){
        //max-min +1 +min
        rand = new Random();
        x = rand.nextInt(3000-2000+1)+2000;
        y = rand.nextInt(1000-250+1)+250;
//        xSpeed = (rand.nextInt(300-100 + 1)+100)*-1;
        xSpeed = -250;
        ySpeed = rand.nextInt(150)-75;
        alive = true;
        dy = dx = 0;
        dirX = 0f;
        dirY = 0f;
    }


    public void update(float dt){
        xSpeed += dx*dt;
        ySpeed += dy*dt;

        x += xSpeed * dt;
        y += ySpeed * dt;

        bounds.x = x;
        bounds.y = y;

        if(x<-200)
            alive = false;
        if(y >= CEILING_OFFSET || y <= GROUND_OFFSET)
            ySpeed *= -1;
    }


    public void render(SpriteBatch sb){
        super.render(sb);
    }


    public void playPickupSound(){
        pickupSound.play();
    }


    public void dispose(){
        super.dispose();
        pickupSound.dispose();
    }

    public boolean isWeapon() {
        return isWeapon;
    }

    public void use(Player p){

    }
}
