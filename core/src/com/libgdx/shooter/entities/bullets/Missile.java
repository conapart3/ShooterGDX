package com.libgdx.shooter.entities.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Conal on 12/11/2015.
 */
public class Missile extends Bullet{

    public Missile(){
        super();
        damage = damage * 2;
        maxSpeed = maxSpeed * 0.55f;
    }

    @Override
    protected void setTexture(){
        texture = new Texture(Gdx.files.internal("data/missile.png"));
    }

}
