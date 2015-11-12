package com.libgdx.shooter.entities.items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.libgdx.shooter.entities.Player;
import com.libgdx.shooter.entities.SpaceObject;
import com.libgdx.shooter.entities.weapons.*;
import com.libgdx.shooter.entities.weapons.HeavyLaserCannon;
import com.libgdx.shooter.entities.weapons.LightLaserCannon;

import java.util.Random;

import static com.libgdx.shooter.game.ShooterGame.CEILING_OFFSET;
import static com.libgdx.shooter.game.ShooterGame.GROUND_OFFSET;

/**
 * Created by Conal on 08/11/2015.
 */
public class Item extends SpaceObject {

    protected Sound pickupSound;

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
        if(y== CEILING_OFFSET || y< GROUND_OFFSET)
            ySpeed *= -1;

    }


    public void render(SpriteBatch sb){
        super.render(sb);
    }

    public void attachToPlayer(Player player){
        pickupSound.play();
    }

    public static Item generateItem(){
        Random rand = new Random();
        int rng = rand.nextInt(8);
        if(rng == 1)
            return new Minigun();
        else if(rng == 2)
            return new HeavyLaserCannon();
        else if(rng == 3)
            return new LightLaserCannon();
        else if(rng == 4)
            return new ShieldPickup();
        else if(rng == 5)
            return new Medal();
        else if(rng == 6)
            return new Shotgun();
        else
            return new HealthPickup();
    }

    public void dispose(){
        texture.dispose();
        pickupSound.dispose();
    }
}
