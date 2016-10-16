package com.libgdx.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.libgdx.shooter.entities.items.Item;
import com.libgdx.shooter.entities.weapons.LightLaserCannon;
import com.libgdx.shooter.entities.weapons.Weapon;
import com.libgdx.shooter.gamestates.GameState;

import java.util.ArrayList;

import static com.libgdx.shooter.game.ShooterGame.GROUND_OFFSET;
import static com.libgdx.shooter.game.ShooterGame.PLAYER_CEILING_OFFSET;
import static com.libgdx.shooter.game.ShooterGame.SCALE_RATIO_X;
import static com.libgdx.shooter.game.ShooterGame.SCALE_RATIO_Y;

/**
 * Created by Conal on 26/09/2015.
 */
public class Player extends SpaceObject {

    public static float STARTING_POINT_X, STARTING_POINT_Y;

    private TextureRegion[] playerFrames;
    private TextureRegion currentFrame;
    private Animation playerAnimation;
    private float stateTime;
    private float maxSpeed;
    private int score;
    private Weapon weapon;
    private boolean isReadyToShoot;
    private Sound explosionSound;
    private boolean upPressed, downPressed;
    private ArrayList<Item> items;
    private float pointsMultiplier = 1f;

    public Player() {
        create();
    }

    public void create() {
        int FRAME_COLS = 8;
        int FRAME_ROWS = 1;

        texture = new Texture(Gdx.files.internal("data/shipAnimation.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        explosionSound = GameState.assetManager.get("data/Sound/explosionPlayer.wav");
        TextureRegion[][] textureRegions = TextureRegion.split(texture, texture.getWidth() / FRAME_COLS, texture.getHeight() / FRAME_ROWS);
        playerFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];

        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                playerFrames[index++] = textureRegions[i][j];
            }
        }
        playerAnimation = new Animation(0.065f, playerFrames);

        width = texture.getWidth() / FRAME_COLS;
        height = texture.getHeight() / FRAME_ROWS;
        bounds = new Rectangle(x, y, width, height);

        //starting position
        STARTING_POINT_X = 150 * SCALE_RATIO_X;
        STARTING_POINT_Y = 800 * SCALE_RATIO_Y;
        x = STARTING_POINT_X;
        y = STARTING_POINT_Y;

        maxSpeed = 600f;
        health = 2000;
        alive = true;

        xOffset = width / 2 + dirX * 30;
        yOffset = height / 2 + dirY * 30;

        items = new ArrayList<Item>();
        weapon = new LightLaserCannon();
    }

    public void update(float dt) {
        if (health < 1)
            alive = false;

//        if (upPressed)
//            dy = 50;
//        else if (downPressed)
//            dy = -50;
//        else
//            dy = 0;

        ySpeed = maxSpeed * dy * dt;
        y += ySpeed * dt;

        xSpeed = maxSpeed * dx * dt;
        x += xSpeed * dt;

        if (y < GROUND_OFFSET)
            y = GROUND_OFFSET;
        if (x < 0)
            x = 0;
        if (x > 1850)
            x = 1850;
        if (y > PLAYER_CEILING_OFFSET)
            y = PLAYER_CEILING_OFFSET;

        bounds.x = x;
        bounds.y = y;

        stateTime += dt;
        currentFrame = playerAnimation.getKeyFrame(stateTime, true);

        weapon.update(dt);
        isReadyToShoot = weapon.isReadyToShoot();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(currentFrame, x, y);
    }

    public void setKnobPosition(float percentX, float percentY) {
        dx = percentX*50;
        dy = percentY*50;
    }

    @Override
    public void dispose() {
        super.dispose();
        currentFrame.getTexture().dispose();
        for (int i = 0; i < playerFrames.length; i++) {
            playerFrames[i].getTexture().dispose();
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addPoints(float points) {
        this.score += points;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void addItem(Item item) {
        if (item.isWeapon()) {
            setWeapon((Weapon) item);
        } else {
            items.add(item);
            item.use(this);
        }
    }

    public boolean isReadyToShoot() {
        return isReadyToShoot;
    }

    public void playExplosion() {
        explosionSound.play();
    }

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void addShield() {
        this.health += 1000;
    }

    public void setOneHitKill() {
    }

    public void addHealth(int hp) {
        this.health += hp;
    }

    public void addMedal() {
        addPoints(1000);
    }

    public void setDoublePoints() {
        pointsMultiplier = 1.1f;
        /**
         * todo: x2 for a period of time
         */
    }

    public float getPointsMultiplier() {
        return pointsMultiplier;
    }

    public void setPointsMultiplier(float pointsMultiplier) {
        this.pointsMultiplier = pointsMultiplier;
    }
}
