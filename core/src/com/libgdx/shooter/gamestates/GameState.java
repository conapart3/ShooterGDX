package com.libgdx.shooter.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.libgdx.shooter.entities.Bullet;
import com.libgdx.shooter.entities.ParachuteBomber;
import com.libgdx.shooter.entities.Player;
import com.libgdx.shooter.game.ShooterGame;
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
    private float nextLevelTimer;
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

    //touchpad and stage declaration
    private Stage stage;
    private Touchpad touchpad;
    private TextureRegion shootBtnUpTextureRegion, shootBtnDownTextureRegion;
    private Texture shootBtnUpTexture, shootBtnDownTexture;

    private BitmapFont font;
    private Label labelA, labelB, labelC;

    private ImageButton shootBtn;

//    button to enable touch screen user to shoot?
//    private TextButton shootButton;
//    private TextButton.TextButtonStyle textButtonStyle;
//    BitmapFont font;
//    Skin skin;
//    TextureAtlas buttonAtlas;

    //camera and background
    private float lerp = 0.5f;
    private Texture bg1;
    private int srcY = 0,srcX = 0;

    private Sound shootSound, hitSound, explosionSound;
    private Music bgMusic;


    public GameState(GameStateManager gsm){
        super(gsm);

        //sets the game state and calls init
//        cam.setToOrtho(false, ShooterGame.WORLD_WIDTH, ShooterGame.WORLD_HEIGHT);
//        cam.setToOrtho(false, 1920, 1080);
    }

    @Override
    public void create() {
        cam.position.set(WORLD_WIDTH/2, WORLD_HEIGHT/2, 0);
//        cam.translate(cam.viewportWidth/2, cam.viewportHeight/2);

        spriteBatch = new SpriteBatch();

        bg1 = new Texture(Gdx.files.internal("data/BackgroundElements/starsbg.png"));

        initStage();

        stage = new Stage(viewport);
        stage.addActor(touchpad);
        stage.addActor(shootBtn);
        Gdx.input.setInputProcessor(stage);

        shootSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/laserFire.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/mflop.mp3"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("data/Sound/explosion.wav"));
//        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("data/Sound/menuMusic.mp3"));
//        bgMusic.setLooping(true);
//        bgMusic.play();

        player = new Player();
        level = 0;
        rateOfFire = 0.3f;
        timeSinceLastFire = 0f;
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
                    activeParachuteBombers.removeIndex(i);
                    parachuteBomberPool.free(pbItem);
                }
                pbItem.update(dt);
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
            if (activeParachuteBombers.size == 0) {
                nextLevelTimer += dt;
                if (nextLevelTimer > 4) {
                    nextLevelTimer -= 4;
                    level++;
                    spawnParachuteBombers();
                    player.addPoints(100 * (level-1));
                }
            }

            stage.act(dt);

//            updateCamera(dt);
            cam.update();
        } else {
            gameStateManager.setState(GameStateManager.GAME_OVER);
        }
    }

    @Override
    public void render() {
        spriteBatch.setProjectionMatrix(cam.combined);

        spriteBatch.enableBlending();
        spriteBatch.begin();

//        spriteBatch.draw(bg1, cam.position.x-(cam.viewportWidth/2), 0);
        bg1.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        spriteBatch.draw(bg1, 0, 0, srcX, srcY, bg1.getWidth(), bg1.getHeight());
//        srcX+=0.1;

        player.render(spriteBatch);

        for(int i=0; i<activeParachuteBombers.size; i++) {
            activeParachuteBombers.get(i).render(spriteBatch);
        }

        for(int i = activeBullets.size; --i >= 0; ){
            activeBullets.get(i).render(spriteBatch);
        }

//        font.draw(spriteBatch, "Level: "+level +". Lives Left: "+player.getLives(), 30, 30);
        labelA.setText("Level: " + level);
        labelA.draw(spriteBatch, 1);

        labelB.setText("Score: " + player.getScore());
        labelB.draw(spriteBatch, 1);

        labelC.setText("Lives: " + player.getLives());
        labelC.draw(spriteBatch, 1);

        spriteBatch.end();

        stage.draw();
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void handleInput() {
        player.setKnobPosition(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());

        if (shootBtn.isPressed()){
            shoot();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            shoot();
        }

    }

    @Override
    public void dispose() {
        bg1.dispose();
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
//        bgMusic.dispose();
        hitSound.dispose();
        shootSound.dispose();
        explosionSound.dispose();
    }

    private void checkCollisions(){
        //check bullets with parachutebomber collisions
        for(int i=0; i<activeBullets.size; i++){
            Bullet b = activeBullets.get(i);
            for(int j=0; j<activeParachuteBombers.size; j++){
                ParachuteBomber pb = activeParachuteBombers.get(j);
                if(pb.collides(b.getBounds()) || b.collides(pb.getBounds())){
                    b.setAlive(false);
                    pb.setAlive(false);
                    player.addPoints(50);
                    hitSound.play();
                }
            }
        }

        //check player with parachutebomber collisions
        for(int i=0; i<activeParachuteBombers.size; i++){
            ParachuteBomber pb=activeParachuteBombers.get(i);
            if(player.collides(pb.getBounds()) || pb.collides(player.getBounds())){
                player.removeLife();
                pb.setAlive(false);
                explosionSound.play();
            }
        }
    }

    private void initStage(){
        //init labels
        font = new BitmapFont();
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
        touchpad.setBounds(150f,60f,300f,300f);
    }

    private void initShootBtn(){
        shootBtnUpTexture = new Texture(Gdx.files.internal("data/Buttons/btnDefault.png"));
        shootBtnDownTexture = new Texture(Gdx.files.internal("data/Buttons/btnPressed.png"));

        shootBtnUpTextureRegion = new TextureRegion(shootBtnUpTexture);
        shootBtnDownTextureRegion = new TextureRegion(shootBtnDownTexture);

        ImageButton.ImageButtonStyle shootBtnStyle = new ImageButton.ImageButtonStyle();
        shootBtnStyle.imageUp = new TextureRegionDrawable(shootBtnUpTextureRegion);
        shootBtnStyle.imageDown = new TextureRegionDrawable(shootBtnDownTextureRegion);

        shootBtn = new ImageButton(shootBtnStyle);
//        shootBtn.setBounds(1400f*ratioX,50*ratioY,500*ratioX,500*ratioY);
        shootBtn.setBounds(1400f,50,500,500);
    }

    private void spawnParachuteBombers() {
        for(int i = 0; i<3+(5*level/2); i++) {
            ParachuteBomber pbItem = parachuteBomberPool.obtain();
            pbItem.init();
            activeParachuteBombers.add(pbItem);
        }
    }

    private void shoot(){
        if(timeSinceLastFire >= rateOfFire){
            timeSinceLastFire -= rateOfFire;
            Bullet bItem = bulletPool.obtain();
            bItem.init(player.getX()+75, player.getY()+25, 700f, 0f);
            activeBullets.add(bItem);
            shootSound.play();
        }
    }
//
//    private void updateCamera(float dt){
//
//        //update the camera position based on player position
//        camPos = cam.position;
////        if(player.getX()-camPos.x > 100)
////        camPos.x += (player.getX() - camPos.x) * lerp * dt;
////        if(player.getY()-camPos.y > 100)
////        camPos.y += (player.getY() - camPos.y) * lerp * dt;
////        cam.position.set(camPos);
//
////        if(player.getX() > 50)
////            srcX += (player.getX() - srcX) * lerp * dt;
//
////        srcX += lerp* dt * srcX;
//
//        cam.update();
//
//    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height);
        cam.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        stage.getViewport().update(WORLD_WIDTH, (int)(WORLD_WIDTH * ASPECT_RATIO), false);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
