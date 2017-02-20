package com.libgdx.shooter.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.libgdx.shooter.entities.Soundable;
import com.libgdx.shooter.entities.SoundToPlay;
import com.libgdx.shooter.entities.SpaceObject;
import com.libgdx.shooter.entities.weapons.HeavyLaserCannon;
import com.libgdx.shooter.entities.weapons.Weapon;

import java.util.Random;

import static com.libgdx.shooter.game.ShooterGame.CEILING_OFFSET;
import static com.libgdx.shooter.game.ShooterGame.GROUND_OFFSET;

/**
 * Created by Conal on 28/10/2015.
 */
public class ShooterEnemy extends SpaceObject implements Pool.Poolable, Soundable {

    private Random rand;
    private boolean isShooting;
    private float dirX, dirY, dirLength;
    private double rotation;
    private Weapon weapon;
    private float strafeChangeTime = 4f, strafeTimer = 0f;
    protected SoundToPlay soundToPlay;

    public ShooterEnemy() {
        this.alive = false;
        rand = new Random();

//        texture = new Texture(Gdx.files.internal("data/shooterEnemy.png"));
//        texture = new Texture(Gdx.files.internal("data/crab3d.png"));
        texture = new Texture(Gdx.files.internal("data/pearlEnemyFinal.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        health = 200;
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x, y, width, height);
        isShooting = false;
        dirX = 0f;
        dirY = 0f;
        weapon = new HeavyLaserCannon();
        maxSpeed = 400;
        strafe = false;
        soundToPlay = SoundToPlay.EXPLOSION_SOUND_SHOOTERENEMY;
    }

    //pass in the level - health = 200 + (200*level/2)
    public void create(int level) {
        //max-min +1 +min
        x = rand.nextInt(3000 - 2000 + 1) + 2000;
        y = rand.nextInt(1000 - 250 + 1) + 250;
//        xSpeed = (rand.nextInt(300-100 + 1)+100)*-1;
        xSpeed = -maxSpeed;
        ySpeed = rand.nextInt(50) - 25;
        alive = true;
        dy = 0;
        dx = -50;
        health = 100 + (50 * level);
        isShooting = false;
        dirX = 0f;
        dirY = 0f;
        strafe = false;
    }

    public void update(float dt, float targetX, float targetY) {
        //this could be the problem why some bullets shot at player from behind player.
        if (alive)
            weapon.update(dt);

        if (x < 1900) {
            dirX = (targetX - x - width / 2);
            dirY = (targetY - y - height / 2);
            dirLength = (float) Math.sqrt(dirX * dirX + dirY * dirY);
            dirX = dirX / dirLength;
            dirY = dirY / dirLength;
//            if(difference between targetY and y is only 50)
//                then move out of fire line
            isShooting = weapon.isReadyToShoot();
        }
        rotation = (Math.atan2(dirY, dirX) * 180.0d / Math.PI) - 180.0f;
        xOffset = width / 2 + dirX * 30;
        yOffset = height / 2 + dirY * 30;

        strafeTimer += dt;

        if (x < -200)
            alive = false;

        if (x < 1200) {
            dx = 300f;
        } else {
            dx = 0f;
        }

        if (x < 1500 && x > 1300 && y > 400 && y < 600) {
            if (strafeTimer > strafeChangeTime) {
                strafeTimer = strafeTimer - strafeChangeTime;
                randomStrafe();
            }
        } else {
            strafe = false;
        }

//        if(strafe){
        if (x > 1500)
            dx = -300f;
        if (x < 1300 && xSpeed < 0)
            dx = 300f;
        if (y > 600 && ySpeed > 0)
            dy = -300f;
        if (y < 400 && ySpeed < 0)
            dy = 300f;
//            if(y<700 && y> 350){
//                if(ySpeed>0)
//                    dy = -20;
//                if(ySpeed<0)
//                    dy=20;
//            }
//        }

        if (y >= CEILING_OFFSET || y <= GROUND_OFFSET)
            ySpeed = 0;

        super.speedLimit();
        super.move(dt);

    }

    private void randomStrafe() {
        strafe = true;
        radians = MathUtils.random(2 * 3.1415f);
        dx = MathUtils.cos(radians) * 50;
        dy = MathUtils.sin(radians) * 50;
    }

    @Override
    public void render(SpriteBatch sb) {
//        sb.draw(texture,x,y,width/2,height/2,width,height,1,1,(float)rotation);
        sb.draw(texture, x, y, width / 2, height / 2, width, height, 1, 1, (float) rotation, 0, 0, width, height, false, false);
    }

    @Override
    public void reset() {
        alive = false;
        isShooting = false;
        x = -100;
        y = -100;
        xSpeed = 0;
        ySpeed = 0;
        dx = 0;
        dy = 0;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public boolean isShooting() {
        return isShooting;
    }

    public void setIsShooting(boolean isShooting) {
        this.isShooting = isShooting;
    }

    public float getDirX() {
        return dirX;
    }

    public void setDirX(float dirX) {
        this.dirX = dirX;
    }

    public float getDirY() {
        return dirY;
    }

    public void setDirY(float dirY) {
        this.dirY = dirY;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public SoundToPlay getSound() {
        return soundToPlay;
    }
}
