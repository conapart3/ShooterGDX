package com.libgdx.shooter.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.libgdx.shooter.entities.bullets.Bullet;
import com.libgdx.shooter.entities.bullets.Laser;
import com.libgdx.shooter.entities.bullets.Missile;
import com.libgdx.shooter.entities.items.Item;
import com.libgdx.shooter.entities.enemies.ParachuteBomber;
import com.libgdx.shooter.entities.Player;
import com.libgdx.shooter.entities.enemies.ShooterEnemy;
import com.libgdx.shooter.entities.weapons.Weapon;
import com.libgdx.shooter.entities.weapons.WeaponType;
import com.libgdx.shooter.managers.GameStateManager;

import java.util.ArrayList;

import javax.xml.soap.Text;

import static com.libgdx.shooter.game.ShooterGame.*;

/**
 * Created by Conal on 30/09/2015.
 */
public class GameState extends State implements InputProcessor{

    private SpriteBatch spriteBatch;
    private Player player;
    private int level;
    private float nextLevelTimer, gameOverTimer;

    /** Array and pool containing the bullets **/
    private final Array<Bullet> activeBullets = new Array<Bullet>();
    private final Pool<Bullet> bulletPool = new Pool<Bullet>(){
        @Override
        protected Bullet newObject(){
            return new Bullet();
        }
    };

    /**
     * TODO: refactor the shooting mechanics to a potential new handler class?
     */
    /** Array and pool containing the missiles **/
    private final Array<Missile> activeMissiles = new Array<Missile>();
    private final Pool<Missile> missilePool = new Pool<Missile>(){
        @Override
        protected Missile newObject(){
            return new Missile();
        }
    };

    /** Array and pool containing the lasers **/
    private final Array<Laser> activeLasers = new Array<Laser>();
    private final Pool<Laser> laserPool = new Pool<Laser>(){
        @Override
        protected Laser newObject(){
            return new Laser();
        }
    };

    /** Array and pool containing the parachute bombers **/
    private final Array<ParachuteBomber> activeParachuteBombers = new Array<ParachuteBomber>();
    private final Pool<ParachuteBomber> parachuteBomberPool = new Pool<ParachuteBomber>() {
        @Override
        protected ParachuteBomber newObject() {
            return new ParachuteBomber();
        }
    };

    /** Array and pool containing the shooter enemies **/
    private final Array<ShooterEnemy> activeShooterEnemies = new Array<ShooterEnemy>();
    private final Pool<ShooterEnemy> shooterEnemyPool = new Pool<ShooterEnemy>(){
        @Override
        protected ShooterEnemy newObject(){
            return new ShooterEnemy();
        }
    };

    //touchpad and stage declaration
    private Stage stage;
//    private Touchpad touchpad;
//    private TextureRegion shootBtnUpTextureRegion, shootBtnDownTextureRegion;
//    private Texture shootBtnUpTexture, shootBtnDownTexture;

    private BitmapFont font;
    private Label labelA, labelB, labelC;

    //camera and background
    private float lerp = 0.5f;
    private Texture bgGround, bgMountains, bgCity;
    private float srcY = 0,srcX = 0;

//    private Music bgMusic;

    private Vector3 touchPoint;
    private boolean shouldShoot;

    private Texture bgGround2, bgMountains2, bgCity2;

    private ArrayList<Item> pickups;

    public static AssetManager assetManager;

    public GameState(GameStateManager gsm){
        super(gsm);

    }

    @Override
    public void create() {
        /** Set camera default start position **/
        cam.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        assetManager = new AssetManager();

        /** One spritebatch used to draw all sprites **/
        spriteBatch = new SpriteBatch();

        /** Load in the background textures **/
        bgGround = new Texture(Gdx.files.internal("data/BackgroundElements/bgFinalLayer2.png"));
        bgMountains = new Texture(Gdx.files.internal("data/BackgroundElements/bgFinalLayerBack.png"));
        bgCity = new Texture(Gdx.files.internal("data/BackgroundElements/bgFinalLayerCity.png"));

        /** Load in the background textures into AssetManager **/
//        assetManager.load("data/BackgroundElements/bgFinalLayer2.png", Texture.class);
//        assetManager.load("data/BackgroundElements/bgFinalLayerBack.png", Texture.class);
//        assetManager.load("data/BackgroundElements/bgFinalLayerCity.png", Texture.class);
//        assetManager.load("data/BackgroundElements/bgFinalLayerDesert.png", Texture.class);
//        assetManager.load("data/BackgroundElements/bgFinalLayerBack_desert.png", Texture.class);
//        assetManager.load("data/BackgroundElements/bgFinalLayerCity_Desert.png", Texture.class);

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

        while(!assetManager.update()){

        }
        /** Load in the sprites into AssetManager **/
//        assetManager.load("", Texture.class);

        /**
         * TODO: Use loader to load in the new backgrounds for the next maplevel
         * **/
        bgGround2 = new Texture(Gdx.files.internal("data/BackgroundElements/bgFinalLayer1Desert.png"));
        bgMountains2 = new Texture(Gdx.files.internal("data/BackgroundElements/bgFinalLayerBack_desert.png"));
        bgCity2 = new Texture(Gdx.files.internal("data/BackgroundElements/bgFinalLayerCity_Desert.png"));

        /** Load in the stage **/
        initStage();
        stage = new Stage(viewport);
//        stage.addActor(touchpad);
//        stage.addActor(shootBtn);
//        Gdx.input.setInputProcessor(stage);
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
        level = 0;
        gameOverTimer = 0f;
        touchPoint = new Vector3();
        shouldShoot = false;

        pickups = new ArrayList<Item>();
    }

    @Override
    public void update(float dt) {
        if(assetManager.update()) {
            if (player.isAlive()) {
                handleInput();

                player.update(dt);

                if (shouldShoot) {
                    if (player.isReadyToShoot()) {
                        shoot(player.getWeapon(), player.getX() + player.getxOffset(), player.getY() + player.getyOffset(), 1f, 0f, false);//x and y offset, x and y normalised direction
//                    player.getWeapon().playShootSound();
                    }
                }

                /** Update the active parachute bombers **/
                int pbLen = activeParachuteBombers.size;
                for (int i = pbLen; --i >= 0; ) {
                    ParachuteBomber pbItem = activeParachuteBombers.get(i);
                    if (!pbItem.isAlive()) {
//                    explosionSound.play();
                        activeParachuteBombers.removeIndex(i);
                        parachuteBomberPool.free(pbItem);
                    }
                    pbItem.update(dt, player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);
                }

                /** Update the active shooter enemies **/
                int seLen = activeShooterEnemies.size;
                for (int i = seLen; --i >= 0; ) {
                    ShooterEnemy seItem = activeShooterEnemies.get(i);
                    if (!seItem.isAlive()) {
                        activeShooterEnemies.removeIndex(i);
                        shooterEnemyPool.free(seItem);
                    }
                    seItem.update(dt, player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2);
                    if (seItem.isShooting()) {
                        shoot(seItem.getWeapon(), seItem.getX() + seItem.getxOffset(), seItem.getY() + seItem.getyOffset(),
                                seItem.getDirX(), seItem.getDirY(), true);
//                    seItem.getWeapon().playShootSound();
                    }
                }

                /** Update the active bullets **/
                int bLen = activeBullets.size;
                for (int i = bLen; --i >= 0; ) {
                    Bullet bItem = activeBullets.get(i);
                    if (!bItem.isAlive()) {
                        activeBullets.removeIndex(i);
                        bulletPool.free(bItem);
                    }
                    bItem.update(dt);
                }

                /** Update the active missiles **/
                int mLen = activeMissiles.size;
                for (int i = mLen; --i >= 0; ) {
                    Missile mItem = activeMissiles.get(i);
                    if (!mItem.isAlive()) {
                        activeMissiles.removeIndex(i);
                        missilePool.free(mItem);
                    }
                    mItem.update(dt);
                }

                /** Update the active lasers **/
                int lLen = activeLasers.size;
                for (int i = lLen; --i >= 0; ) {
                    Laser lItem = activeLasers.get(i);
                    if (!lItem.isAlive()) {
                        activeLasers.removeIndex(i);
                        laserPool.free(lItem);
                    }
                    lItem.update(dt);
                }

                /** Update the pickups **/
                for (int i = 0; i < pickups.size(); i++) {
                    Item i1 = pickups.get(i);
                    if (!i1.isAlive()) {
                        i1.dispose();
                        pickups.remove(i);
                    }
                    i1.update(dt);
                }

                checkCollisions();

                /** Spawn new enemies, stop player shooting flag, start timer until next level starts **/
                if (activeParachuteBombers.size == 0 && activeShooterEnemies.size == 0) {
                    nextLevelTimer += dt;
                    shouldShoot = false;
                    if (nextLevelTimer > 4) {
                        nextLevelTimer -= 4;
                        level++;
                        if (level == 7) {
                            bgGround = bgGround2;
                            bgMountains = bgMountains2;
                            bgCity = bgCity2;
                        }
                        spawnParachuteBombers();
                        spawnShooterEnemies();
                        spawnPickups();
                        player.addPoints(100 * (level - 1));
                        shouldShoot = true;
                    }
                }

                /** update the stage and update camera. srcX is used to move the background **/
                stage.act(dt);
//            updateCamera(dt);
                cam.update();
                srcX += 1;
            } else {
                /**
                 * TODO: Use screen manager and load in screens properly
                 * **/
                /** Game is over, timer until game over screen shows **/
                gameOverTimer += dt;
                if (gameOverTimer > 1) {
                    gameOverTimer -= 1;
                    gameStateManager.setState(GameStateManager.GAME_OVER, player.getScore());
                }
            }
        }
    }

    @Override
    public void render() {
        if(assetManager.update()) {
            spriteBatch.setProjectionMatrix(cam.combined);

            spriteBatch.enableBlending();
            spriteBatch.begin();

            /** Draw the backgrounds and have them scroll within themselves based on srcX **/
            bgMountains.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            spriteBatch.draw(bgMountains, 0, 0, (int) (srcX / 3), (int) srcY, bgMountains.getWidth(), bgMountains.getHeight());
            bgCity.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            spriteBatch.draw(bgCity, 0, -100, (int) (srcX), (int) srcY, bgCity.getWidth(), bgCity.getHeight());
            bgGround.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            spriteBatch.draw(bgGround, 0, -75, (int) (srcX * 4), (int) srcY, bgGround.getWidth(), bgGround.getHeight());
//        srcX+=1;

            player.render(spriteBatch);

            /** Update all enemies, bullets and pickups **/
            for (int i = 0; i < activeParachuteBombers.size; i++) {
                activeParachuteBombers.get(i).render(spriteBatch);
            }

            for (int i = 0; i < activeShooterEnemies.size; i++) {
                activeShooterEnemies.get(i).render(spriteBatch);
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

            for (int i = 0; i < pickups.size(); i++) {
                pickups.get(i).render(spriteBatch);
            }

            /** Draw all text on screen **/
//        font.draw(spriteBatch, "Level: "+level +". Lives Left: "+player.getLives(), 30, 30);
            labelA.setText("Level: " + level);
            labelA.draw(spriteBatch, 1);

            labelB.setText("Score: " + player.getScore());
            labelB.draw(spriteBatch, 1);

            labelC.setText("Health: " + player.getHealth());
            labelC.draw(spriteBatch, 1);

            spriteBatch.end();

            /** Draw stage **/
            stage.draw();
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void handleInput() {
//        player.setKnobPosition(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());

//        if(Gdx.input.)

        //toggle shoot button if space pressed
//        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
//            shootBtn.toggle();

//        if (shootBtn.isPressed())
//            shootBtn.toggle();

//        if(shootBtn.isChecked()){
//            if(timeSinceLastFire >= rateOfFire){
//                timeSinceLastFire -= rateOfFire;
//                shoot(player.getX()+player.getxOffset(), player.getY()+player.getyOffset(), 1f, 0f, false);//x and y offset, x and y normalised direction
//                shootSound.play();
//            }
//        }

//        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
//            player.setUpPressed(true);
//        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
//            player.setDownPressed(true);
//        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
//            player.setLeftPressed(true);
//        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
//            player.setRightPressed(true);




    }

    @Override
    public void dispose() {
        bgGround.dispose();
        bgMountains.dispose();
        bgCity.dispose();
        stage.dispose();
        spriteBatch.dispose();
        font.dispose();
//        shootBtnUpTexture.dispose();
//        shootBtnDownTexture.dispose();
//        shootBtnUpTextureRegion.getTexture().dispose();
//        shootBtnDownTextureRegion.getTexture().dispose();
        player.dispose();
        for(Bullet b : activeBullets)
            b.dispose();
        for(ParachuteBomber pb : activeParachuteBombers)
            pb.dispose();
        for(ShooterEnemy se : activeShooterEnemies)
                se.dispose();
//        bgMusic.dispose();
        for(int i=0; i<pickups.size(); i++){
            pickups.get(i).dispose();
        }
    }

    private void checkCollisions(){

        /**
         * TODO: REFACTOR SO IT DOESNT RUN THROUGH ALL THESE FOR LOOPS
         * TODO: ADD EXPLOSION ANIMATION
         */
        /**Check collisions between bullets and player/enemies**/
        for(int i=0; i<activeBullets.size; i++){
            Bullet b = activeBullets.get(i);
            if(!b.isShotFromEnemy()) {
                for (int j = 0; j < activeParachuteBombers.size; j++) {
                    ParachuteBomber pb = activeParachuteBombers.get(j);
                    if (pb.collides(b.getBounds()) || b.collides(pb.getBounds())) {
                        b.setAlive(false);
                        pb.takeDamage(b.getDamage());
                        player.addPoints(b.getDamage());
                        if(!pb.isAlive()) {
                            pb.playExplosion();
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
                        player.addPoints(b.getDamage());
                        if(!se.isAlive()) {
                            se.playExplosion();
                        } else {
                            b.playHitSound();
                        }
                    }
                }
            } else {
                if (player.collides(b.getBounds()) || b.collides(player.getBounds())) {
                    b.setAlive(false);
                    player.takeDamage(b.getDamage());
                    if(!player.isAlive()) {
                        player.playExplosion();
                    } else {
                        b.playHitSound();
                    }
                }
            }
        }

        /**Check collisions between lasers and player/enemies**/
        for(int i=0; i<activeLasers.size; i++){
            Laser l = activeLasers.get(i);
            if(!l.isShotFromEnemy()) {
                for (int j = 0; j < activeParachuteBombers.size; j++) {
                    ParachuteBomber pb = activeParachuteBombers.get(j);
                    if (pb.collides(l.getBounds()) || l.collides(pb.getBounds())) {
                        l.setAlive(false);
                        pb.takeDamage(l.getDamage());
                        player.addPoints(l.getDamage());
                        if(!pb.isAlive()) {
                            pb.playExplosion();
                        } else {
                            l.playHitSound();
                        }
                    }
                }
                for (int k = 0; k < activeShooterEnemies.size; k++) {
                    ShooterEnemy se = activeShooterEnemies.get(k);
                    if (se.collides(l.getBounds()) || l.collides(se.getBounds())) {
                        l.setAlive(false);
                        se.takeDamage(l.getDamage());
                        player.addPoints(l.getDamage());
                        if(!se.isAlive()) {
                            se.playExplosion();
                        } else {
                            l.playHitSound();
                        }
                    }
                }
            } else {
                if (player.collides(l.getBounds()) || l.collides(player.getBounds())) {
                    l.setAlive(false);
                    player.takeDamage(l.getDamage());
                    if(!player.isAlive()) {
                        player.playExplosion();
                    } else {
                        l.playHitSound();
                    }
                }
            }
        }

        /**Check collisions between missiles and player/enemies**/
        for(int i=0; i<activeMissiles.size; i++){
            Missile m = activeMissiles.get(i);
            if(!m.isShotFromEnemy()) {
                for (int j = 0; j < activeParachuteBombers.size; j++) {
                    ParachuteBomber pb = activeParachuteBombers.get(j);
                    if (pb.collides(m.getBounds()) || m.collides(pb.getBounds())) {
                        m.setAlive(false);
                        pb.takeDamage(m.getDamage());
                        player.addPoints(m.getDamage());
                        if(!pb.isAlive()) {
                            pb.playExplosion();
                        } else {
                            m.playHitSound();
                        }
                    }
                }
                for (int k = 0; k < activeShooterEnemies.size; k++) {
                    ShooterEnemy se = activeShooterEnemies.get(k);
                    if (se.collides(m.getBounds()) || m.collides(se.getBounds())) {
                        m.setAlive(false);
                        se.takeDamage(m.getDamage());
                        player.addPoints(m.getDamage());
                        if(!se.isAlive()) {
                            se.playExplosion();
                        } else {
                            m.playHitSound();
                        }
                    }
                }
            } else {
                if (player.collides(m.getBounds()) || m.collides(player.getBounds())) {
                    m.setAlive(false);
                    player.takeDamage(m.getDamage());
                    if(!player.isAlive()) {
                        player.playExplosion();
                    } else {
                        m.playHitSound();
                    }
                }
            }
        }

        /**check player with parachutebomber collisions**/
        for(int i=0; i<activeParachuteBombers.size; i++){
            ParachuteBomber pb=activeParachuteBombers.get(i);
            if(player.collides(pb.getBounds()) || pb.collides(player.getBounds())){
                player.takeDamage(pb.getDamage());
                pb.setAlive(false);
                if(!player.isAlive()) {
                    player.playExplosion();
                } else {
                    pb.playExplosion();
                }
            }
        }

        /**check player collisions with pickups**/
        for(int i=0; i<pickups.size(); i++){
            Item pi = pickups.get(i);
            if(player.collides(pi.getBounds()) || pi.collides(player.getBounds())){
//                player.addItem(pi);
                pi.attachToPlayer(player);
                pi.setAlive(false);
            }
        }
    }

    private void initStage(){
        /** Create the font and relevant styles associated for labels **/
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/Fonts/Montserrat-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.minFilter = Texture.TextureFilter.Nearest;
        parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
        parameter.size = 14;
        font = generator.generateFont(parameter);
        generator.dispose();

        //create labels
//        font = new BitmapFont();
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        labelA = new Label("Level: ", labelStyle);
        labelA.setFontScale(3);
        labelA.setPosition(100, 1000);

        labelB = new Label("Score: ", labelStyle);
        labelB.setFontScale(3);
        labelB.setPosition(1500, 1000);

        labelC = new Label("Lives: ", labelStyle);
        labelC.setFontScale(3);
        labelC.setPosition(600, 1000);

//        initTouchpad();
//        initShootBtn();
    }
//
//    private void initTouchpad(){
//        //touchpad and stage initialising
//        Skin touchpadSkin = new Skin();
//        touchpadSkin.add("touchBackground", new Texture("data/touchBackground.png"));
//        touchpadSkin.add("touchKnob", new Texture("data/touchKnob.png"));
//        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
//
//        //pixmap background instead of png background? allows transparency
//        Pixmap bg = new Pixmap(200, 200, Pixmap.Format.RGBA8888);
//        bg.setBlending(Pixmap.Blending.None);
//        bg.setColor(1, 1, 1, 0.2f);
//        bg.fillCircle(100, 100, 100);
//        touchpadStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(bg)));
//
////        Drawable touchBackground = touchpadSkin.getDrawable("touchBackground");
//        Drawable touchKnob = touchpadSkin.getDrawable("touchKnob");
////        touchpadStyle.background = touchBackground;
//        touchpadStyle.knob = touchKnob;
//
//        touchpad = new Touchpad(2f, touchpadStyle);
////        touchpad.setBounds(150f*ratioX,60f*ratioY,300f*ratioX,300f*ratioY);
//        touchpad.setBounds(1400f, 60f, 300f, 300f);
//    }
//
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


    /** Methods to spawn enemies and pickups **/
    private void spawnParachuteBombers() {
//        for(int i = 0; i<3+(5*level/2); i++) {
        for(int i = 0; i<level+level/2; i++) {
            ParachuteBomber pbItem = parachuteBomberPool.obtain();
            pbItem.create(level);
            activeParachuteBombers.add(pbItem);
        }
    }

    private void spawnShooterEnemies() {
        for(int i = 0; i<level/2; i++) {
            ShooterEnemy seItem = shooterEnemyPool.obtain();
            seItem.create(level);
            activeShooterEnemies.add(seItem);
        }
    }

    private void spawnPickups() {
        for(int j=0; j<level; j++) {
            Item i = Item.generateItem();
            pickups.add(i);
        }
    }

    /**
     * Method to shoot. This method works out which bullet should be generated, and pulls the correct bullet
     * out of the appropriate pool. It is also responsible for playing the correct weapon shoot sound.
     * It also need the starting positions of the bullets and the target position in the form of startX,
     * startY, dirX, dirY.
     * @param weapon
     * @param startX
     * @param startY
     * @param dirX
     * @param dirY
     * @param isShotFromEnemy
     */
    private void shoot(Weapon weapon, float startX, float startY, float dirX, float dirY, boolean isShotFromEnemy){
        if(weapon.getType() == WeaponType.LIGHT_LASER_CANNON) {
            Bullet bItem = bulletPool.obtain();
            bItem.init(startX, startY, dirX, dirY, isShotFromEnemy);
            activeBullets.add(bItem);
            weapon.playShootSound();
        } else if(weapon.getType() == WeaponType.MISSILE_LAUNCHER) {
            Missile mItem = missilePool.obtain();
            mItem.init(startX,startY,dirX,dirY,isShotFromEnemy);
            activeMissiles.add(mItem);
            weapon.playShootSound();
        } else if(weapon.getType() == WeaponType.HEAVY_LASER_CANNON){
            Laser lItem = laserPool.obtain();
            lItem.init(startX, startY, dirX, dirY, isShotFromEnemy);
            activeLasers.add(lItem);
            weapon.playShootSound();
        } else if(weapon.getType() == WeaponType.MINIGUN){
            Bullet bItem = bulletPool.obtain();
            bItem.init(startX, startY, dirX, dirY, isShotFromEnemy);
            activeBullets.add(bItem);
            weapon.playShootSound();
        }else if(weapon.getType() == WeaponType.SHOTGUN){
            Bullet bItem1 = bulletPool.obtain();
            Bullet bItem2 = bulletPool.obtain();
            Bullet bItem3 = bulletPool.obtain();
            bItem1.init(startX, startY, dirX+0.1f, dirY+0.1f, isShotFromEnemy);
            bItem2.init(startX, startY, dirX, dirY, isShotFromEnemy);
            bItem3.init(startX, startY, dirX-0.1f, dirY-0.1f, isShotFromEnemy);
            activeBullets.add(bItem1);
            activeBullets.add(bItem2);
            activeBullets.add(bItem3);
            weapon.playShootSound();
        }

    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height);
        cam.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        stage.getViewport().update(WORLD_WIDTH, (int)(WORLD_WIDTH * SCREEN_ASPECT_RATIO), false);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public boolean keyDown(int keycode) {
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
            player.setKnobPosition(0,1);
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
            player.setKnobPosition(0,-1);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        player.setKnobPosition(0,0);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        cam.unproject(touchPoint.set(screenX,screenY,0));
        if(touchPoint.x < WORLD_WIDTH/2)
            player.setKnobPosition(0,-1);
        else if(touchPoint.x > WORLD_WIDTH/2)
            player.setKnobPosition(0,1);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        player.setKnobPosition(0,0);
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
