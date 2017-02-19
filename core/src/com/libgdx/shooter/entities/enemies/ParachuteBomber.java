package com.libgdx.shooter.entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.libgdx.shooter.entities.SoundPicker;
import com.libgdx.shooter.entities.SoundToPlay;
import com.libgdx.shooter.entities.SpaceObject;

import java.util.Random;

import static com.libgdx.shooter.game.ShooterGame.CEILING_OFFSET;
import static com.libgdx.shooter.game.ShooterGame.GROUND_OFFSET;

/**
 * Created by Conal on 22/10/2015.
 */
public class ParachuteBomber extends SpaceObject implements Pool.Poolable, SoundPicker {

    private Random rand;
    private int points = 100;
    private int damage;
    private float lerp = 0.5f;
    private boolean fromBoss;
    private float dirLength;
    protected SoundToPlay soundToPlay;

    public ParachuteBomber() {
        this.alive = false;
        rand = new Random();

        texture = new Texture(Gdx.files.internal("data/newmine.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        health = 50;
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x, y, width, height);
        damage = 200;
        maxSpeed = 500f;

//        explosionSound = GameState.assetManager.get("data/Sound/explosionParachute.wav");
        soundToPlay = SoundToPlay.EXPLOSION_SOUND_PARACHUTEBOMBER;
    }

    public void create(int level) {
        //((max - min) + 1) + min
        x = rand.nextInt(3000 - 2000 + 1) + 2000;
        y = rand.nextInt(1000 - 250 + 1) + 250;
        xSpeed = (rand.nextInt(800 - 400 + 1) + 400) * -1;
        ySpeed = rand.nextInt(100) - 50;
        alive = true;
        dy = 0;
        dx = 0;
        health = 25 + (10 * level);
        damage = 200;
    }

    public void create(int level, float startX, float startY, float targetX, float targetY) {
        //((max - min) + 1) + min
        x = startX;
        y = startY;
        ySpeed = -50;
        alive = true;

        /** normalise dx and dy to be a unit vector to multiply by maxSpeed **/
        dx = targetX - x;
        dy = targetY - y;
        dirLength = (float) Math.sqrt(dx * dx + dy * dy);
        dx = dx / dirLength;
        dy = dy / dirLength;

        health = 25 + (10 * level);
        damage = 200;
    }

    public void update(float dt, float targetX, float targetY) {
        /**
         * todo: make this algorithm for moving toward the player better
         */
//        if (x - targetX < 400) {
//            dy = (y-targetY)/2;
//        } else {
//            dy = 0;
//        }

        //apply acceleration to speed (dy and dx are acceleration)
        xSpeed += maxSpeed * dx * dt;
        ySpeed += maxSpeed * dy * dt;

        //position differs by the velocity
        x += xSpeed * dt;
        y += ySpeed * dt;

        bounds.x = x;
        bounds.y = y;

        if (x < -200)
            alive = false;
        if (y >= CEILING_OFFSET || y <= GROUND_OFFSET)
            ySpeed *= -1;

    }

    public void render(SpriteBatch sb) {
        sb.draw(texture, x, y);
    }

    @Override
    public void reset() {
        x = -100;
        y = -100;
        alive = false;
        xSpeed = 0;
        ySpeed = 0;
        dx = 0;
        dy = 0;
        health = 0;
    }

    public int getDamage() {
        return damage;
    }


    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isFromBoss() {
        return fromBoss;
    }

    public void setFromBoss(boolean fromBoss) {
        this.fromBoss = fromBoss;
    }

    @Override
    public SoundToPlay pickSound() {
        return soundToPlay;
    }
}
