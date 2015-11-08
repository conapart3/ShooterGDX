package com.libgdx.shooter.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.libgdx.shooter.entities.Bullet;
import com.libgdx.shooter.entities.ParachuteBomber;
import com.libgdx.shooter.entities.Player;
import com.libgdx.shooter.entities.ShooterEnemy;
import com.libgdx.shooter.managers.GameStateManager;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import static com.libgdx.shooter.game.ShooterGame.*;

/**
 * Created by Conal on 30/09/2015.
 */
public class GameState extends State {

    private SpriteBatch spriteBatch;
    private Player player;
    private int level;
    private float nextLevelTimer, gameOverTimer;
    private float rateOfFire, timeSinceLastFire;

    //array containing the active bullets, pool
    private final Array<Bullet> activeBullets = new Array<Bullet>();
    private final Pool<Bullet> bulletPool = new Pool<Bullet>(){
        @Override
        protected Bullet newObject(){
            return new Bullet();
        }
    };

    //array containing active parachute bombers, pool
    private final Array<ParachuteBomber> activeParachuteBombers = new Array<ParachuteBomber>();
    private final Pool<ParachuteBomber> parachuteBomberPool = new Pool<ParachuteBomber>() {
        @Override
        protected ParachuteBomber newObject() {
            return new ParachuteBomber();
        }
    };

    private final Array<ShooterEnemy> activeShooterEnemies = new Array<ShooterEnemy>();
    private final Pool<ShooterEnemy> shooterEnemyPool = new Pool<ShooterEnemy>(){
        @Override
        protected ShooterEnemy newObject(){
            return new ShooterEnemy();
        }
    };

    //touchpad and stage declaration
    private Stage stage;
    private Touchpad touchpad;
    private TextureRegion shootBtnUpTextureRegion, shootBtnDownTextureRegion;
    private Texture shootBtnUpTexture, shootBtnDownTexture;

    private BitmapFont font;
    private Label labelA, labelB, labelC;

    private ImageButton shootBtn;

    //camera and background
    private float lerp = 0.5f;
    private Texture bgGround, bgMountains, bgCity;
    private float srcY = 0,srcX = 0;
    private float groundX, mountainsX, cityX;

    private Sound shootSound, hitSound, explosionSound, laserSound;
//    private Music bgMusic;


    public GameState(GameStateManager gsm){
        super(gsm);

        //sets the game state and calls init
//        cam.setToOrtho(false, ShooterGame.WORLD_WIDTH, ShooterGame.WORLD_HEIGHT);
//        cam.setToOrtho(false, 1920, 1080);
    }

    @Override
    public void create() {
        cam.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
//        cam.translate(cam.viewportWidth/2, cam.viewportHeight/2);

        spriteBatch = new SpriteBatch();

//        bgGround = new Texture(Gdx.files.internal("data/BackgroundElements/starsbg.png"));
        bgGround = new Texture(Gdx.files.internal("data/BackgroundElements/bgFinalLayer1.png"));
        bgMountains = new Texture(Gdx.files.internal("data/BackgroundElements/bgFinalLayerBack.png"));
        bgCity = new Texture(Gdx.files.internal("data/BackgroundElements/bgFinalLayerCity.png"));
//        bgCity = new Texture(Gdx.files.internal("data/BackgroundElements/bgFinalLayerCity2.png"));

        initStage();

        stage = new Stage(viewport);
        stage.addActor(touchpad);
        stage.addActor(shootBtn);
        Gdx.input.setInputProcessor(stage);

        shootSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/laserFire.wav"));
//        laserSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/laserFire.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/mflop.mp3"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/explosion.wav"));
//        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("data/Sound/menuMusic.mp3"));
//        bgMusic.setLooping(true);
//        bgMusic.play();

        player = new Player();
        level = 0;
        rateOfFire = 0.2f;
        timeSinceLastFire = 0f;
        gameOverTimer = 0f;
    }

    @Override
    public void update(float dt) {
        if(player.isAlive()) {
            handleInput();

            player.update(dt);

            timeSinceLastFire+=dt;
            if(timeSinceLastFire > rateOfFire)
                timeSinceLastFire = rateOfFire;

            //update the active parachute bombers
            int pbLen = activeParachuteBombers.size;
            for (int i = pbLen; --i >= 0; ) {
                ParachuteBomber pbItem = activeParachuteBombers.get(i);
                if (!pbItem.isAlive()) {
//                    explosionSound.play();
                    activeParachuteBombers.removeIndex(i);
                    parachuteBomberPool.free(pbItem);
                }
                pbItem.update(dt, player.getX()+player.getWidth()/2, player.getY()+player.getHeight()/2);
            }

            //update active shooter enemies
            int seLen = activeShooterEnemies.size;
            for (int i = seLen; --i >= 0; ) {
                ShooterEnemy seItem = activeShooterEnemies.get(i);
                if (!seItem.isAlive()) {
                    activeShooterEnemies.removeIndex(i);
                    shooterEnemyPool.free(seItem);
                }
                seItem.update(dt, player.getX()+player.getWidth()/2, player.getY()+player.getHeight()/2);
                if(seItem.isShooting()){
                    shoot(seItem.getX()+seItem.getxOffset(), seItem.getY()+seItem.getyOffset(),
                            seItem.getDirX(), seItem.getDirY(), true);
                    shootSound.play();
                }
            }

            //update the active bullets
            int bLen = activeBullets.size;
            for (int i = bLen; --i >= 0; ) {
                Bullet bItem = activeBullets.get(i);
                if (!bItem.isAlive()) {
                    activeBullets.removeIndex(i);
                    bulletPool.free(bItem);
                }
                bItem.update(dt);
            }

            checkCollisions();

            //spawns enemies based on level.
            if (activeParachuteBombers.size == 0 && activeShooterEnemies.size == 0) {
                nextLevelTimer += dt;
                if (nextLevelTimer > 4) {
                    nextLevelTimer -= 4;
                    level++;
                    spawnParachuteBombers();
                    spawnShooterEnemies();
                    player.addPoints(100 * (level-1));
                }
            }

            stage.act(dt);

//            updateCamera(dt);
            cam.update();
            srcX+=1;
        } else {
            gameOverTimer += dt;
            if (gameOverTimer > 1) {
                gameOverTimer -= 1;
                gameStateManager.setState(GameStateManager.GAME_OVER, player.getScore());
            }
        }
    }

    @Override
    public void render() {
        spriteBatch.setProjectionMatrix(cam.combined);

        spriteBatch.enableBlending();
        spriteBatch.begin();

        bgMountains.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        spriteBatch.draw(bgMountains, 0, 0, (int) (srcX / 3), (int) srcY, bgMountains.getWidth(), bgMountains.getHeight());
        bgCity.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        spriteBatch.draw(bgCity, 0, -100, (int) (srcX), (int) srcY, bgCity.getWidth(), bgCity.getHeight());
        bgGround.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        spriteBatch.draw(bgGround, 0, -75, (int) (srcX * 4), (int) srcY, bgGround.getWidth(), bgGround.getHeight());
//        srcX+=1;


        player.render(spriteBatch);

        for(int i=0; i<activeParachuteBombers.size; i++) {
            activeParachuteBombers.get(i).render(spriteBatch);
        }

        for(int i=0; i<activeShooterEnemies.size; i++) {
            activeShooterEnemies.get(i).render(spriteBatch);
        }

        for(int i = activeBullets.size; --i >= 0; ){
            activeBullets.get(i).render(spriteBatch);
        }

//        font.draw(spriteBatch, "Level: "+level +". Lives Left: "+player.getLives(), 30, 30);
        labelA.setText("Level: " + level);
        labelA.draw(spriteBatch, 1);

        labelB.setText("Score: " + player.getScore());
        labelB.draw(spriteBatch, 1);

        labelC.setText("Health: " + player.getHealth());
        labelC.draw(spriteBatch, 1);

        spriteBatch.end();

        stage.draw();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void handleInput() {
        player.setKnobPosition(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());

//        if(Gdx.input.)

        //toggle shoot button if space pressed
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            shootBtn.toggle();

        if (shootBtn.isPressed())
            shootBtn.toggle();

        if(shootBtn.isChecked()){
            if(timeSinceLastFire >= rateOfFire){
                timeSinceLastFire -= rateOfFire;
                shoot(player.getX()+player.getxOffset(), player.getY()+player.getyOffset(), 1f, 0f, false);//x and y offset, x and y normalised direction
                shootSound.play();
            }
        }

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
        shootBtnUpTexture.dispose();
        shootBtnDownTexture.dispose();
        shootBtnUpTextureRegion.getTexture().dispose();
        shootBtnDownTextureRegion.getTexture().dispose();
        player.dispose();
        for(Bullet b : activeBullets)
            b.dispose();
        for(ParachuteBomber pb : activeParachuteBombers)
            pb.dispose();
        for(ShooterEnemy se : activeShooterEnemies)
                se.dispose();
//        bgMusic.dispose();
        hitSound.dispose();
        shootSound.dispose();
        explosionSound.dispose();
    }

    private void checkCollisions(){
        //check bullets collisions with parachute bombers and shooter enemies
        for(int i=0; i<activeBullets.size; i++){
            Bullet b = activeBullets.get(i);
            if(!b.isShotFromEnemy()) {
                for (int j = 0; j < activeParachuteBombers.size; j++) {
                    ParachuteBomber pb = activeParachuteBombers.get(j);
                    if (pb.collides(b.getBounds()) || b.collides(pb.getBounds())) {
                        b.setAlive(false);
                        pb.takeDamage(b.getDamage());
                        player.addPoints(b.getDamage());
                        explosionSound.play();
                    }
                }
                for (int k = 0; k < activeShooterEnemies.size; k++) {
                    ShooterEnemy se = activeShooterEnemies.get(k);
                    if (se.collides(b.getBounds()) || b.collides(se.getBounds())) {
                        b.setAlive(false);
                        se.takeDamage(b.getDamage());
                        player.addPoints(b.getDamage());
                        explosionSound.play();
                    }
                }
            } else {
                if (player.collides(b.getBounds()) || b.collides(player.getBounds())) {
                    b.setAlive(false);
                    player.takeDamage(b.getDamage());
                    explosionSound.play();
                }
            }
        }

        //check player with parachutebomber collisions
        for(int i=0; i<activeParachuteBombers.size; i++){
            ParachuteBomber pb=activeParachuteBombers.get(i);
            if(player.collides(pb.getBounds()) || pb.collides(player.getBounds())){
                player.takeDamage(pb.getDamage());
                pb.setAlive(false);
                explosionSound.play();
            }
        }
    }

    private void initStage(){
        //init font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/Fonts/Montserrat-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.minFilter = Texture.TextureFilter.Nearest;
        parameter.magFilter = Texture.TextureFilter.MipMapLinearNearest;
        parameter.size = 14;
        font = generator.generateFont(parameter);
        generator.dispose();

        //init labels
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

        initTouchpad();
        initShootBtn();
    }

    private void initTouchpad(){
        //touchpad and stage initialising
        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("data/touchBackground.png"));
        touchpadSkin.add("touchKnob", new Texture("data/touchKnob.png"));
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();

        //pixmap background instead of png background? allows transparency
        Pixmap bg = new Pixmap(200, 200, Pixmap.Format.RGBA8888);
        bg.setBlending(Pixmap.Blending.None);
        bg.setColor(1, 1, 1, 0.2f);
        bg.fillCircle(100, 100, 100);
        touchpadStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(bg)));

//        Drawable touchBackground = touchpadSkin.getDrawable("touchBackground");
        Drawable touchKnob = touchpadSkin.getDrawable("touchKnob");
//        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;

        touchpad = new Touchpad(2f, touchpadStyle);
//        touchpad.setBounds(150f*ratioX,60f*ratioY,300f*ratioX,300f*ratioY);
        touchpad.setBounds(1400f, 60f, 300f, 300f);
    }

    private void initShootBtn(){
        shootBtnUpTexture = new Texture(Gdx.files.internal("data/Buttons/btnDefault.png"));
        shootBtnDownTexture = new Texture(Gdx.files.internal("data/Buttons/btnPressed.png"));

        shootBtnUpTextureRegion = new TextureRegion(shootBtnUpTexture);
        shootBtnDownTextureRegion = new TextureRegion(shootBtnDownTexture);

        ImageButton.ImageButtonStyle shootBtnStyle = new ImageButton.ImageButtonStyle();
        shootBtnStyle.imageUp = new TextureRegionDrawable(shootBtnUpTextureRegion);
        shootBtnStyle.imageDown = new TextureRegionDrawable(shootBtnDownTextureRegion);
        shootBtnStyle.imageChecked = shootBtnStyle.imageDown;

        shootBtn = new ImageButton(shootBtnStyle);
//        shootBtn.setBounds(1400f*ratioX,50*ratioY,500*ratioX,500*ratioY);
        shootBtn.setBounds(150f,-350,500,1000);
    }

    private void spawnParachuteBombers() {
//        for(int i = 0; i<3+(5*level/2); i++) {
        for(int i = 0; i<level*2; i++) {
            ParachuteBomber pbItem = parachuteBomberPool.obtain();
            pbItem.init(level);
            activeParachuteBombers.add(pbItem);
        }
    }

    private void spawnShooterEnemies() {
        for(int i = 0; i<level; i++) {
            ShooterEnemy seItem = shooterEnemyPool.obtain();
            seItem.init(level);
            activeShooterEnemies.add(seItem);
        }
    }

    private void shoot(float startX, float startY, float dirX, float dirY, boolean isShotFromEnemy){
        Bullet bItem = bulletPool.obtain();
        bItem.init(startX, startY, dirX, dirY, isShotFromEnemy);
        activeBullets.add(bItem);
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
}
