package com.libgdx.shooter.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.libgdx.shooter.entities.Particle;
import com.libgdx.shooter.entities.Player;
import com.libgdx.shooter.entities.bullets.Bullet;
import com.libgdx.shooter.entities.bullets.Laser;
import com.libgdx.shooter.entities.bullets.Missile;
import com.libgdx.shooter.entities.enemies.BossEnemy;
import com.libgdx.shooter.entities.enemies.ParachuteBomber;
import com.libgdx.shooter.entities.enemies.ShooterEnemy;
import com.libgdx.shooter.entities.items.Item;
import com.libgdx.shooter.entities.items.ItemFactory;
import com.libgdx.shooter.entities.weapons.Weapon;
import com.libgdx.shooter.entities.weapons.WeaponType;
import com.libgdx.shooter.game.ShooterGame;
import com.libgdx.shooter.levels.Level;
import com.libgdx.shooter.managers.Animator;
import com.libgdx.shooter.managers.GameStateManager;

import java.util.ArrayList;

import static com.libgdx.shooter.game.ShooterGame.SCREEN_ASPECT_RATIO;
import static com.libgdx.shooter.game.ShooterGame.WORLD_HEIGHT;
import static com.libgdx.shooter.game.ShooterGame.WORLD_WIDTH;

/**
 * Created by Conal on 30/09/2015.
 */
public class GameState extends State implements InputProcessor {


    public static AssetManager assetManager;
    private final int TIME_UNTIL_NEXT_LEVEL = 4;
    /**
     * Array and pool containing the bullets
     **/
    private final Array<Bullet> activeBullets = new Array<Bullet>();
    private final Pool<Bullet> bulletPool = new Pool<Bullet>() {
        @Override
        protected Bullet newObject() {
            return new Bullet();
        }
    };
    /**
     * Array and pool containing the missiles
     **/
    private final Array<Missile> activeMissiles = new Array<Missile>();
    private final Pool<Missile> missilePool = new Pool<Missile>() {
        @Override
        protected Missile newObject() {
            return new Missile();
        }
    };
    /**
     * Array and pool containing the lasers
     **/
    private final Array<Laser> activeLasers = new Array<Laser>();
    private final Pool<Laser> laserPool = new Pool<Laser>() {
        @Override
        protected Laser newObject() {
            return new Laser();
        }
    };
    /**
     * Array and pool containing the parachute bombers
     **/
    private final Array<ParachuteBomber> activeParachuteBombers = new Array<ParachuteBomber>();
    private final Pool<ParachuteBomber> parachuteBomberPool = new Pool<ParachuteBomber>() {
        @Override
        protected ParachuteBomber newObject() {
            return new ParachuteBomber();
        }
    };
    /**
     * Array and pool containing the shooter enemies
     **/
    private final Array<ShooterEnemy> activeShooterEnemies = new Array<ShooterEnemy>();
    private final Pool<ShooterEnemy> shooterEnemyPool = new Pool<ShooterEnemy>() {
        @Override
        protected ShooterEnemy newObject() {
            return new ShooterEnemy();
        }
    };
    /**
     * Array and pool containing the shooter enemies
     **/
    private final Array<Animator> activeExplosions = new Array<Animator>();
    private final Pool<Animator> explosionPool = new Pool<Animator>() {
        @Override
        protected Animator newObject() {
            return new Animator("data/explosionBlue.png", 12, 1, false);
        }
    };
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private Player player;
    private float nextLevelTimer, gameOverTimer;
    private ArrayList<Level> levels;

    /**
     * TODO: refactor the shooting mechanics to a potential new handler class?
     */
    private int numberOfLevels = 2;
    private int level = ShooterGame.getCurrentContext().level, wave = 0;
    private Level currentLevel;
    private boolean levelSuccessFlag = false;
    private int NUM_WAVES_PER_LEVEL = (7 + level);
    private int NUM_LEVELS_UNTIL_BOSS = (6 + level);
    private int NUM_BUILD_UP_WAVES = (5 + level);
    private ItemFactory itemFactory;
    private ArrayList<Item> pickups;
    private ArrayList<Particle> particles;
    //touchpad and stage declaration
    private Stage stage;
    private BitmapFont font;
    private Label labelA, labelB, labelC, labelD;

    private Vector3 touchPoint;
    private boolean autoShoot;

    private BossEnemy boss;


    public GameState(GameStateManager gsm) {
        super(gsm);

    }


    @Override
    public void create() {
        /** Set camera default start position **/
        cam.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        assetManager = new AssetManager();

        /** One spritebatch used to draw all sprites **/
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        /** create the levels and set currentlevel, then create it. **/
        levels = new ArrayList<Level>();
        String bgfilepath = "data/bgelements/bg";
        for (int i = 0; i < numberOfLevels; i++) {
            Level l = new Level(bgfilepath + "back" + i + ".png", bgfilepath + "middle" + i + ".png", bgfilepath + "front" + i + ".png");
            l.create();
            levels.add(l);
        }
        currentLevel = levels.get(0);
//        currentLevel.create();

        /** Load in the shoot sounds into AssetManager **/
        assetManager.load("data/Sound/shootSoundMinigun.wav", Sound.class);
        assetManager.load("data/Sound/shootSoundMissile.wav", Sound.class);
        assetManager.load("data/Sound/shootSoundShotgun.wav", Sound.class);
        assetManager.load("data/Sound/shootSoundHeavyLaserCannon.wav", Sound.class);
        assetManager.load("data/Sound/shootSoundLightLaserCannon.wav", Sound.class);

        /** Load in the pickup sounds into AssetManager **/
        assetManager.load("data/Sound/pickupHeavyLaser.wav", Sound.class);
        assetManager.load("data/Sound/pickupLightLaser.wav", Sound.class);
        assetManager.load("data/Sound/pickupMinigun.wav", Sound.class);
        assetManager.load("data/Sound/pickupShotgun.wav", Sound.class);
        assetManager.load("data/Sound/pickupMissileLauncher.wav", Sound.class);
        assetManager.load("data/Sound/pickupHealth.wav", Sound.class);
        assetManager.load("data/Sound/pickupMedal.wav", Sound.class);
        assetManager.load("data/Sound/pickupShield.wav", Sound.class);

        /** load in the hit sounds for bullet types **/
        assetManager.load("data/Sound/hitSoundLaser.wav", Sound.class);
        assetManager.load("data/Sound/hitSoundBullet.wav", Sound.class);
        assetManager.load("data/Sound/hitSoundMissile.wav", Sound.class);

        /** Load in the explosion sounds into AssetManager **/
        assetManager.load("data/Sound/explosionParachute.wav", Sound.class);
        assetManager.load("data/Sound/explosionShooterEnemy.wav", Sound.class);
        assetManager.load("data/Sound/explosionPlayer.wav", Sound.class);
        assetManager.load("data/Sound/explosionBoss.wav", Sound.class);

//        assetManager.load("data/boss1.png", Texture.class);

        assetManager.finishLoading();

        /** Load in the stage **/
        initStage();
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(this);

        /** Load in the sounds **/
        /**
         * TODO: Refactor this so the object which is hit will have different explosion and hit sounds
         * **/
//        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("data/Sound/menuMusic.mp3"));
//        bgMusic.setLooping(true);
//        bgMusic.play();

        /** Create new player and other misc things **/
        player = new Player();
        gameOverTimer = 0f;
        touchPoint = new Vector3();
        autoShoot = false;

        itemFactory = new ItemFactory();
        pickups = new ArrayList<Item>();

        particles = new ArrayList<Particle>();
        boss = new BossEnemy();
    }

    @Override
    public void update(float dt) {
        if (player.isAlive()) {
            handleInput();

            player.update(dt);

            if (autoShoot) {
                if (player.isReadyToShoot()) {
                    shoot(player.getWeapon(), player.getX() + player.getxOffset(), player.getY() + player.getyOffset(), 1f, 0f, false);//x and y offset, x and y normalised direction
                }
            }

            /** Update the active parachute bombers **/
            int pbLen = activeParachuteBombers.size;
            for (int i = pbLen; --i >= 0; ) {
                ParachuteBomber pbItem = activeParachuteBombers.get(i);
                if (pbItem.isAlive()) {
                    pbItem.update(dt, player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);
                } else {
//                    createParticles(pbItem.getX(), pbItem.getY());
                    activeParachuteBombers.removeIndex(i);
                    parachuteBomberPool.free(pbItem);
                }
            }

            /** Update the active shooter enemies **/
            int seLen = activeShooterEnemies.size;
            for (int i = seLen; --i >= 0; ) {
                ShooterEnemy seItem = activeShooterEnemies.get(i);
                if (seItem.isAlive()) {
                    seItem.update(dt, player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);
                    if (seItem.isShooting()) {
                        shoot(seItem.getWeapon(), seItem.getX() + seItem.getxOffset(), seItem.getY() + seItem.getyOffset(),
                                seItem.getDirX(), seItem.getDirY(), true);
                    }
                } else {
                    activeShooterEnemies.removeIndex(i);
                    shooterEnemyPool.free(seItem);
                }
            }

            /** Update the active bullets **/
            int bLen = activeBullets.size;
            for (int i = bLen; --i >= 0; ) {
                Bullet bItem = activeBullets.get(i);
                if (bItem.isAlive()) {
                    bItem.update(dt);
                } else {
                    activeBullets.removeIndex(i);
                    bulletPool.free(bItem);
                }
            }

            /** Update the active missiles **/
            int mLen = activeMissiles.size;
            for (int i = mLen; --i >= 0; ) {
                Missile mItem = activeMissiles.get(i);
                if (mItem.isAlive()) {
                    mItem.update(dt);
                } else {
                    activeMissiles.removeIndex(i);
                    missilePool.free(mItem);
                }
            }

            /** Update the active lasers **/
            int lLen = activeLasers.size;
            for (int i = lLen; --i >= 0; ) {
                Laser lItem = activeLasers.get(i);
                if (lItem.isAlive()) {
                    lItem.update(dt);
                } else {
                    activeLasers.removeIndex(i);
                    laserPool.free(lItem);
                }
            }

            /** Update the active explosions **/
            int expLen = activeExplosions.size;
            for (int i = expLen; --i >= 0; ) {
                Animator aItem = activeExplosions.get(i);
                if (aItem.isAlive()) {
                    aItem.update(dt);
                } else {
//                    createParticles(pbItem.getX(), pbItem.getY());
                    activeExplosions.removeIndex(i);
                    explosionPool.free(aItem);

                }
            }

            /** Update the pickups **/
            for (int i = 0; i < pickups.size(); i++) {
                Item i1 = pickups.get(i);
                if (i1.isAlive()) {
                    i1.update(dt);
                } else {
                    i1.dispose();
                    pickups.remove(i);
                }
            }

            /** Update the boss **/
            if (boss.isAlive()) {
                boss.update(dt, player.getX(), player.getY());
                if (boss.isShooting()) {
                    shoot(boss.getWeapon(), boss.getX() + boss.getxOffset(), boss.getY() + boss.getyOffset(),
                            boss.getDirX(), boss.getDirY(), true);
                }
                if (boss.isReleaseBomb()) {
                    releaseBomb();
                }
            }

            checkCollisions();

            /** Spawn new enemies, stop player shooting flag, start timer until next level starts **/
            if (activeParachuteBombers.size == 0 && activeShooterEnemies.size == 0 && !boss.isAlive()) {
                nextLevelTimer += dt;
                autoShoot = false;
                levelSuccessFlag = true;
                if (nextLevelTimer > TIME_UNTIL_NEXT_LEVEL) {
                    nextLevelTimer -= TIME_UNTIL_NEXT_LEVEL;
                    wave++;
//                    if(7%wave==0) {
//                        //nextlevel
//                        level++;
//                        currentLevel = levels.get(level);
//                        wave = 1;
//                    }
                    if (wave <= NUM_BUILD_UP_WAVES) {
//                    if(wave<=0+level) {
                        levelSuccessFlag = false;
                        spawnParachuteBombers();
                        spawnShooterEnemies();
                        spawnPickups();
                        player.addPoints(100 * (wave - 1));
                        autoShoot = true;
                    } else if (wave == NUM_LEVELS_UNTIL_BOSS) {
//                    } else if(wave == 1+level){
                        levelSuccessFlag = false;
                        spawnBoss();
                        player.addPoints(100 * (wave - 1));
                        autoShoot = true;
                    } else if (wave % (NUM_WAVES_PER_LEVEL) == 0) { //level is complete!
//                    } else if(wave%(2+level)==0){
                        currentLevel = levels.get(level % 2);//level starts at 0 therefore get curlevel-1
                        level++;
                        ShooterGame.getCurrentContext().level = level;
                        ShooterGame.getCurrentContext().score = player.getScore();
                        wave = 0;
                        gameStateManager.setState(GameStateManager.LEVEL_COMPLETE_STATE);
                    }
                }
            }

            for (int i = 0; i < particles.size(); i++) {
                if (particles.get(i).isAlive()) {
                    particles.get(i).update(dt);
                } else {
                    particles.remove(i);
                    i--;
                }
            }

            currentLevel.update(dt);

            /** update the stage and update camera. srcX is used to move the background **/
            stage.act(dt);
            cam.update();
        } else {
            /**
             * TODO: Use screen manager and load in screens properly
             * **/
            /** Game is over, timer until game over screen shows **/
            gameOverTimer += dt;
            if (gameOverTimer > 1) {
                gameOverTimer -= 1;
                ShooterGame.getCurrentContext().score = player.getScore();
                gameStateManager.setState(GameStateManager.GAME_OVER);
            }
        }
    }


    @Override
    public void render() {
        spriteBatch.setProjectionMatrix(cam.combined);
        shapeRenderer.setProjectionMatrix(cam.combined);

        spriteBatch.enableBlending();
        spriteBatch.begin();

        /** Draw the backgrounds and have them scroll within themselves based on srcX **/
        currentLevel.render(spriteBatch);

        player.render(spriteBatch);

        /** Update all enemies, bullets and pickups **/
        for (int i = 0; i < activeParachuteBombers.size; i++) {
            activeParachuteBombers.get(i).render(spriteBatch);
        }

        for (int i = 0; i < activeShooterEnemies.size; i++) {
            activeShooterEnemies.get(i).render(spriteBatch);
        }

        for (int i = 0; i < pickups.size(); i++) {
            pickups.get(i).render(spriteBatch);
        }


        for (int i = activeExplosions.size; --i >= 0; ) {
            activeExplosions.get(i).render(spriteBatch);
        }

        if (boss.isAlive()) {
            boss.render(spriteBatch);
        }

        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).render(shapeRenderer);
        }

        for (int i = activeBullets.size; --i >= 0; ) {
            activeBullets.get(i).render(spriteBatch);
        }

        for (int i = activeMissiles.size; --i >= 0; ) {
            activeMissiles.get(i).render(spriteBatch);
        }

        for (int i = activeLasers.size; --i >= 0; ) {
            activeLasers.get(i).render(spriteBatch);
        }

        for (int i = 0; i < player.getItems().size(); i++) {
            spriteBatch.draw(player.getItems().get(i).getTexture(), 100 + (100 * i), 100f);
        }
        /** Draw all text on screen **/
//        font.draw(spriteBatch, "Level: "+level +". Lives Left: "+player.getLives(), 30, 30);
        labelA.setText("Level: " + level);
        labelA.draw(spriteBatch, 1);

        labelB.setText("Score: " + player.getScore());
        labelB.draw(spriteBatch, 1);

        labelC.setText("Health: " + player.getHealth());
        labelC.draw(spriteBatch, 1);

        labelD.setText("Wave: " + wave);
        labelD.draw(spriteBatch, 1);
//            if(levelSuccessFlag) {
//                labelD.setText("Level " + level + " completed!");
//                labelD.draw(spriteBatch, 1);
//            }


        spriteBatch.end();

        /** Draw stage **/
        stage.draw();

    }


    @Override
    public void handleInput() {
    }


    @Override
    public void dispose() {
        stage.dispose();
        spriteBatch.dispose();
        font.dispose();
        player.dispose();
        for (Bullet b : activeBullets)
            b.dispose();
        for (ParachuteBomber pb : activeParachuteBombers)
            pb.dispose();
        for (ShooterEnemy se : activeShooterEnemies)
            se.dispose();
        for (int i = 0; i < pickups.size(); i++) {
            pickups.get(i).dispose();
        }
        shapeRenderer.dispose();
        boss.dispose();
    }


    private void checkCollisions() {
        /**
         * TODO: REFACTOR SO IT DOESNT RUN THROUGH ALL THESE FOR LOOPS
         * TODO: ADD EXPLOSION ANIMATION
         */
        /**Check collisions between bullets and player/enemies**/
        for (int i = 0; i < activeBullets.size; i++) {
            Bullet b = activeBullets.get(i);
            if (!b.isShotFromEnemy()) {
                for (int j = 0; j < activeParachuteBombers.size; j++) {
                    ParachuteBomber pb = activeParachuteBombers.get(j);
                    if (pb.collides(b.getBounds()) || b.collides(pb.getBounds())) {
                        b.setAlive(false);
                        pb.takeDamage(b.getDamage());
                        player.addPoints(b.getDamage() * player.getPointsMultiplier());
                        if (!pb.isAlive()) {
                            pb.playExplosion();
                            spawnExplosion(pb.getX(), pb.getY());
//                            createParticles(pb.getX(), pb.getY());
                        } else {
                            b.playHitSound();
                        }
                    }
                }
                for (int k = 0; k < activeShooterEnemies.size; k++) {
                    ShooterEnemy se = activeShooterEnemies.get(k);
                    if (se.collides(b.getBounds()) || b.collides(se.getBounds())) {
                        b.setAlive(false);
                        se.takeDamage(b.getDamage());
                        player.addPoints(b.getDamage() * player.getPointsMultiplier());
                        if (!se.isAlive()) {
                            se.playExplosion();
                            spawnExplosion(se.getX(), se.getY());
                        } else {
                            b.playHitSound();
                        }
                    }
                }
                if (boss.isAlive()) {
                    if (boss.collides(b.getBounds()) || b.collides(boss.getBounds())) {
                        b.setAlive(false);
                        boss.takeDamage(b.getDamage());
                        player.addPoints(b.getDamage() * player.getPointsMultiplier());
                        if (!boss.isAlive()) {
                            boss.playExplosion();
                            spawnExplosion(boss.getX(), boss.getY());
                        } else {
                            b.playHitSound();
                        }
                    }
                }
            } else {
                if (player.collides(b.getBounds()) || b.collides(player.getBounds())) {
                    b.setAlive(false);
                    player.takeDamage(b.getDamage());
                    if (!player.isAlive()) {
                        player.playExplosion();
                    } else {
                        b.playHitSound();
                    }
                }
            }
        }

        /**Check collisions between lasers and player/enemies**/
        for (int i = 0; i < activeLasers.size; i++) {
            Laser b = activeLasers.get(i);
            if (!b.isShotFromEnemy()) {
                for (int j = 0; j < activeParachuteBombers.size; j++) {
                    ParachuteBomber pb = activeParachuteBombers.get(j);
                    if (pb.collides(b.getBounds()) || b.collides(pb.getBounds())) {
                        b.setAlive(false);
                        pb.takeDamage(b.getDamage());
                        player.addPoints(b.getDamage() * player.getPointsMultiplier());
                        if (!pb.isAlive()) {
                            pb.playExplosion();
                            spawnExplosion(pb.getX(), pb.getY());
                        } else {
                            b.playHitSound();
                        }
                    }
                }
                for (int k = 0; k < activeShooterEnemies.size; k++) {
                    ShooterEnemy se = activeShooterEnemies.get(k);
                    if (se.collides(b.getBounds()) || b.collides(se.getBounds())) {
                        b.setAlive(false);
                        se.takeDamage(b.getDamage());
                        player.addPoints(b.getDamage() * player.getPointsMultiplier());
                        if (!se.isAlive()) {
                            se.playExplosion();
                            spawnExplosion(se.getX(), se.getY());
                        } else {
                            b.playHitSound();
                        }
                    }
                }
                if (boss.isAlive()) {
                    if (boss.collides(b.getBounds()) || b.collides(boss.getBounds())) {
                        b.setAlive(false);
                        boss.takeDamage(b.getDamage());
                        player.addPoints(b.getDamage() * player.getPointsMultiplier());
                        if (!boss.isAlive()) {
                            boss.playExplosion();
                            spawnExplosion(boss.getX(), boss.getY());
                        } else {
                            b.playHitSound();
                        }
                    }
                }
            } else {
                if (player.collides(b.getBounds()) || b.collides(player.getBounds())) {
                    b.setAlive(false);
                    player.takeDamage(b.getDamage());
                    if (!player.isAlive()) {
                        player.playExplosion();
                    } else {
                        b.playHitSound();
                    }
                }
            }
        }

        /**Check collisions between missiles and player/enemies**/
        for (int i = 0; i < activeMissiles.size; i++) {
            Missile b = activeMissiles.get(i);
            if (!b.isShotFromEnemy()) {
                for (int j = 0; j < activeParachuteBombers.size; j++) {
                    ParachuteBomber pb = activeParachuteBombers.get(j);
                    if (pb.collides(b.getBounds()) || b.collides(pb.getBounds())) {
                        b.setAlive(false);
                        pb.takeDamage(b.getDamage());
                        player.addPoints(b.getDamage() * player.getPointsMultiplier());
                        if (!pb.isAlive()) {
                            pb.playExplosion();
                            spawnExplosion(pb.getX(), pb.getY());
                        } else {
                            b.playHitSound();
                        }
                    }
                }
                for (int k = 0; k < activeShooterEnemies.size; k++) {
                    ShooterEnemy se = activeShooterEnemies.get(k);
                    if (se.collides(b.getBounds()) || b.collides(se.getBounds())) {
                        b.setAlive(false);
                        se.takeDamage(b.getDamage());
                        player.addPoints(b.getDamage() * player.getPointsMultiplier());
                        if (!se.isAlive()) {
                            se.playExplosion();
                            spawnExplosion(se.getX(), se.getY());
                        } else {
                            b.playHitSound();
                        }
                    }
                }
                if (boss.isAlive()) {
                    if (boss.collides(b.getBounds()) || b.collides(boss.getBounds())) {
                        b.setAlive(false);
                        boss.takeDamage(b.getDamage());
                        player.addPoints(b.getDamage() * player.getPointsMultiplier());
                        if (!boss.isAlive()) {
                            boss.playExplosion();
                            spawnExplosion(boss.getX(), boss.getY());
                        } else {
                            b.playHitSound();
                        }
                    }
                }
            } else {
                if (player.collides(b.getBounds()) || b.collides(player.getBounds())) {
                    b.setAlive(false);
                    player.takeDamage(b.getDamage());
                    if (!player.isAlive()) {
                        player.playExplosion();
                    } else {
                        b.playHitSound();
                    }
                }
            }
        }

        /**check player with parachutebomber collisions**/
        for (int i = 0; i < activeParachuteBombers.size; i++) {
            ParachuteBomber pb = activeParachuteBombers.get(i);
            if (player.collides(pb.getBounds()) || pb.collides(player.getBounds())) {
                player.takeDamage(pb.getDamage());
                pb.setAlive(false);
                if (!player.isAlive()) {
                    player.playExplosion();
                    spawnExplosion(pb.getX(), pb.getY());
                } else {
                    pb.playExplosion();
                    spawnExplosion(pb.getX(), pb.getY());
                }
            }
        }

        /**check player collisions with pickups**/
        for (int i = 0; i < pickups.size(); i++) {
            Item pi = pickups.get(i);
            if (player.collides(pi.getBounds()) || pi.collides(player.getBounds())) {
                player.addItem(pi);
                pi.playPickupSound();
                pi.setAlive(false);
            }
        }
    }


    private void releaseBomb() {
        ParachuteBomber pb = parachuteBomberPool.obtain();
        pb.create(level, boss.getX() + (boss.getWidth() / 2), boss.getY(), player.getX() + player.getxOffset(), player.getY() + player.getyOffset());
        activeParachuteBombers.add(pb);
    }

    private void spawnExplosion(float x, float y) {
        Animator explosion = explosionPool.obtain();
        explosion.create(x, y);
        activeExplosions.add(explosion);
    }

    private void initStage() {
        /** Create the font and relevant styles associated for labels **/
//        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/Fonts/Montserrat-Regular.ttf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.minFilter = Texture.TextureFilter.Nearest;
//        parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
//        parameter.size = 14;
//        font = generator.generateFont(parameter);
//        generator.dispose();

        font = new BitmapFont(Gdx.files.internal("data/Fonts/nextwave.fnt"), Gdx.files.internal("data/Fonts/nextwave_0.png"), false);

        //create labels
//        font = new BitmapFont();
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        labelA = new Label("Level: ", labelStyle);
        labelA.setPosition(50, 1000);

        labelB = new Label("Score: ", labelStyle);
        labelB.setPosition(1500, 1000);

        labelC = new Label("Health: ", labelStyle);
        labelC.setPosition(800, 1000);

        labelD = new Label(" ", labelStyle);
//        labelD.setFontScale(8);
        labelD.setPosition(350, 1000);


    }


//    private void initShootBtn(){
//        shootBtnUpTexture = new Texture(Gdx.files.internal("data/Buttons/btnDefault.png"));
//        shootBtnDownTexture = new Texture(Gdx.files.internal("data/Buttons/btnPressed.png"));
//
//        shootBtnUpTextureRegion = new TextureRegion(shootBtnUpTexture);
//        shootBtnDownTextureRegion = new TextureRegion(shootBtnDownTexture);
//
//        ImageButton.ImageButtonStyle shootBtnStyle = new ImageButton.ImageButtonStyle();
//        shootBtnStyle.imageUp = new TextureRegionDrawable(shootBtnUpTextureRegion);
//        shootBtnStyle.imageDown = new TextureRegionDrawable(shootBtnDownTextureRegion);
//        shootBtnStyle.imageChecked = shootBtnStyle.imageDown;
//
//        shootBtn = new ImageButton(shootBtnStyle);
////        shootBtn.setBounds(1400f*ratioX,50*ratioY,500*ratioX,500*ratioY);
//        shootBtn.setBounds(150f,-350,500,1000);
//    }


    /**
     * Methods to spawn enemies and pickups
     **/
    private void spawnParachuteBombers() {
//        for(int i = 0; i<3+(5*wave/2); i++) {
        for (int i = 0; i < wave + wave / 2; i++) {
            ParachuteBomber pbItem = parachuteBomberPool.obtain();
            pbItem.create(wave);
            activeParachuteBombers.add(pbItem);
        }
    }


    private void spawnShooterEnemies() {
        for (int i = 0; i < wave / 2; i++) {
            ShooterEnemy seItem = shooterEnemyPool.obtain();
            seItem.create(wave);
            activeShooterEnemies.add(seItem);
        }
    }


    private void spawnPickups() {
        for (int j = 0; j < level; j++) {
            Item i = itemFactory.generateItemOrWeapon();
            pickups.add(i);
        }
    }


    private void createParticles(float x, float y) {
        for (int i = 0; i < 6; i++) {
            particles.add(new Particle(x, y));
        }
    }

    private void spawnBoss() {
        boss.dispose();
        boss = new BossEnemy("data/bigboss1.png", level);
        boss.create();
    }

    /**
     * Method to shoot. This method works out which bullet should be generated, and pulls the correct bullet
     * out of the appropriate pool. It is also responsible for playing the correct weapon shoot sound.
     * It also need the starting positions of the bullets and the target position in the form of startX,
     * startY, dirX, dirY.
     *
     * @param weapon
     * @param startX
     * @param startY
     * @param dirX
     * @param dirY
     * @param isShotFromEnemy
     */
    private void shoot(Weapon weapon, float startX, float startY, float dirX, float dirY, boolean isShotFromEnemy) {
        if (weapon.getType() == WeaponType.LIGHT_LASER_CANNON) {
            Bullet bItem = bulletPool.obtain();
            bItem.init(startX, startY, dirX, dirY, isShotFromEnemy);
            activeBullets.add(bItem);
            weapon.playShootSound();
        } else if (weapon.getType() == WeaponType.MISSILE_LAUNCHER) {
            Missile mItem = missilePool.obtain();
            mItem.init(startX, startY, dirX, dirY, isShotFromEnemy);
            activeMissiles.add(mItem);
            weapon.playShootSound();
        } else if (weapon.getType() == WeaponType.HEAVY_LASER_CANNON) {
            Laser lItem = laserPool.obtain();
            lItem.init(startX, startY, dirX, dirY, isShotFromEnemy);
            activeLasers.add(lItem);
            weapon.playShootSound();
        } else if (weapon.getType() == WeaponType.MINIGUN) {
            Bullet bItem = bulletPool.obtain();
            bItem.init(startX, startY, dirX, dirY, isShotFromEnemy);
            activeBullets.add(bItem);
            weapon.playShootSound();
        } else if (weapon.getType() == WeaponType.SHOTGUN) {
            Bullet bItem1 = bulletPool.obtain();
            Bullet bItem2 = bulletPool.obtain();
            Bullet bItem3 = bulletPool.obtain();
            bItem1.init(startX, startY, dirX + 0.1f, dirY + 0.1f, isShotFromEnemy);
            bItem2.init(startX, startY, dirX, dirY, isShotFromEnemy);
            bItem3.init(startX, startY, dirX - 0.1f, dirY - 0.1f, isShotFromEnemy);
            activeBullets.add(bItem1);
            activeBullets.add(bItem2);
            activeBullets.add(bItem3);
            weapon.playShootSound();
        }
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        cam.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        stage.getViewport().update(WORLD_WIDTH, (int) (WORLD_WIDTH * SCREEN_ASPECT_RATIO), false);
    }


    @Override
    public void pause() {

    }


    @Override
    public void resume() {

    }


    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.UP)
            player.setUpPressed(true);

        if (keycode == Input.Keys.DOWN)
            player.setDownPressed(true);

        return true;
    }


    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Input.Keys.UP)
            player.setUpPressed(false);

        if (keycode == Input.Keys.DOWN)
            player.setDownPressed(false);

        return true;
    }


    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        cam.unproject(touchPoint.set(screenX, screenY, 0));
        if (touchPoint.x > WORLD_WIDTH / 2)
            player.setUpPressed(true);
        else if (touchPoint.x < WORLD_WIDTH / 2)
            player.setDownPressed(true);
        return true;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        cam.unproject(touchPoint.set(screenX, screenY, 0));
        if (touchPoint.x > WORLD_WIDTH / 2)
            player.setUpPressed(false);
        else if (touchPoint.x < WORLD_WIDTH / 2)
            player.setDownPressed(false);
        return true;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }


    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
