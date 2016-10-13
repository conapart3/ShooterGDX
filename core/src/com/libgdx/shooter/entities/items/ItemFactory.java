package com.libgdx.shooter.entities.items;

import com.libgdx.shooter.entities.weapons.HeavyLaserCannon;
import com.libgdx.shooter.entities.weapons.LightLaserCannon;
import com.libgdx.shooter.entities.weapons.Minigun;
import com.libgdx.shooter.entities.weapons.MissileLauncher;
import com.libgdx.shooter.entities.weapons.Shotgun;

import java.util.Random;

/**
 * Created by Conal on 21/11/2015.
 */
public class ItemFactory {


    public ItemFactory() {

    }


    public static Item generateItem() {
        Random rand = new Random();
        int rng = rand.nextInt(100);
        if (rng >= 0 && rng < 50)
            return new HealthPickup();
        else if (rng >= 50 && rng < 65)
            return new DoublePointsPickup();
        else if (rng >= 65 && rng < 75)
            return new OneHitKillPickup();
        else if (rng >= 75 && rng < 90)
            return new ShieldPickup();
        else
            return new Medal();
    }


    public static Item generateWeapon() {
        Random rand = new Random();
        int rng = rand.nextInt(100);
        if (rng >= 0 && rng < 20)
            return new HeavyLaserCannon();
        else if (rng >= 20 && rng < 40)
            return new LightLaserCannon();
        else if (rng >= 40 && rng < 60)
            return new Minigun();
        else if (rng >= 60 && rng < 80)
            return new MissileLauncher();
        else
            return new Shotgun();
    }


    public static Item generateItemOrWeapon() {
        Random rand = new Random();
        int rng = rand.nextInt(100);

        if (rng >= 0 && rng < 10)
            return new HeavyLaserCannon();
        else if (rng >= 10 && rng < 20)
            return new LightLaserCannon();
        else if (rng >= 20 && rng < 30)
            return new Minigun();
        else if (rng >= 30 && rng < 40)
            return new MissileLauncher();
        else if (rng >= 40 && rng < 50)
            return new Shotgun();
        else if (rng >= 50 && rng < 60)
            return new ShieldPickup();
        else if (rng >= 60 && rng < 70)
            return new DoublePointsPickup();
        else if (rng >= 70 && rng < 80)
            return new OneHitKillPickup();
        else if (rng >= 80 && rng < 95)
            return new HealthPickup();
        else
            return new Medal();
    }

}
