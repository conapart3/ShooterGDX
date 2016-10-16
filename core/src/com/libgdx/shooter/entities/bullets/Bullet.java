package com.libgdx.shooter.entities.bullets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;
import com.libgdx.shooter.entities.SpaceObject;
import com.libgdx.shooter.game.ShooterGame;
import com.libgdx.shooter.gamestates.GameState;


/**
 * Created by Conal on 22/10/2015.
 */
public class Bullet extends SpaceObject implements Pool.Poolable {

    protected int damage;
    private float xOffset, yOffset;
    protected float maxSpeed;
    private boolean isShotFromEnemy;
    protected Sound hitSound;
//    private Texture texture2;


    public Bullet() {
        this.alive = false;
        setTexture();
        setHitSound();
        xSpeed = 0;
        ySpeed = 0;
        width = texture.getWidth();
        height = texture.getHeight();
        bounds = new Rectangle(x, y, width, height);
        damage = 50;
        maxSpeed = 900f;
    }


    protected void setHitSound() {
        hitSound = GameState.assetManager.get("data/Sound/hitSoundBullet.wav");
    }


    protected void setTexture() {
//        texture = new Texture(Gdx.files.internal("data/laser.png"));
//        texture = new Texture(Gdx.files.internal("data/laserRed.png"));
        texture = new Texture(Gdx.files.internal("data/bullet.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

    }


    public void init(float startXPosition, float startYPosition, float dirX, float dirY, boolean isShotFromEnemy) {
        this.x = startXPosition;
        this.y = startYPosition;
        this.dirX = dirX;
        this.dirY = dirY;
        alive = true;
        this.xSpeed = dirX * maxSpeed;
        this.ySpeed = dirY * maxSpeed;
        this.isShotFromEnemy = isShotFromEnemy;
    }


    public void update(float dt) {
        rotation = (Math.atan2(dirY, dirX) * 180.0d / Math.PI) - 180.0f;

        x += dirX * maxSpeed * dt;
        y += dirY * maxSpeed * dt;

        bounds.x = x;
        bounds.y = y;

        if (x > 2000 || x < -50 || y < ShooterGame.GROUND_OFFSET || y > ShooterGame.CEILING_OFFSET + 250)
            alive = false;
    }


    public void render(SpriteBatch sb) {
        sb.draw(texture, x, y, width / 2, height / 2, width, height, 1, 1, (float) rotation,
                0, 0, width, height, false, false);
    }


    @Override
    public void reset() {
        x = -500;
        y = -500;
        alive = false;
        xSpeed = 0;
        ySpeed = 0;
        dx = 0;
        dy = 0;
        dirX = 0;
        dirY = 0;
    }


    @Override
    public void dispose() {
        super.dispose();
        hitSound.dispose();
//        texture2.dispose();
    }


    public void playHitSound() {
        hitSound.play(0.5f);
    }


    public int getDamage() {
        return damage;
    }


    public void setDamage(int damage) {
        this.damage = damage;
    }


    public boolean isShotFromEnemy() {
        return isShotFromEnemy;
    }


    public void setIsShotFromEnemy(boolean isShotFromEnemy) {
        this.isShotFromEnemy = isShotFromEnemy;
    }


}

