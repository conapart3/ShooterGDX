package com.libgdx.shooter.entities.weapons;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.libgdx.shooter.entities.SoundToPlay;
import com.libgdx.shooter.entities.items.Item;

/**
 * Created by Conal on 08/11/2015.
 */
public abstract class Weapon extends Item {

    protected float timeSinceLastFire;
    protected float rateOfFire;
    protected float damage;

    public Weapon() {
        super();
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x, y, width, height);
        super.create();
        isWeapon = true;
        // default shoot sound
        soundToPlay = SoundToPlay.SHOOT_SOUND_LIGHTLASERCANNON;
    }

    public abstract WeaponType getType();

    public abstract void useWeapon(Vector2 target);

    public void update(float dt) {
        super.update(dt);
        timeSinceLastFire += dt;
        if (timeSinceLastFire > rateOfFire)
            timeSinceLastFire = rateOfFire;
    }

    public boolean isReadyToShoot() {
        if (timeSinceLastFire >= rateOfFire) {
            timeSinceLastFire -= rateOfFire;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
