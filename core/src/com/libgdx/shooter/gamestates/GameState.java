package com.libgdx.shooter.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.libgdx.shooter.entities.Bullet;
import com.libgdx.shooter.entities.ParachuteBomber;
import com.libgdx.shooter.entities.Player;
import com.libgdx.shooter.game.ShooterGame;
import com.libgdx.shooter.managers.GameStateManager;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import java.util.ArrayList;

/**
 * Created by Conal on 30/09/2015.
 */
public class GameState extends State {

    private SpriteBatch spriteBatch;
    private Player player;
    private int level;
    private float betweenLevelTimer;
    private float rateOfFire, timeSinceLastFire;
    private float dt;
    private float ratioX, ratioY;

    //array containing the active bullets, pool
    private final Array<Bullet> activeBullets = new Array<Bullet>();
    private final Pool<Bullet> bulletPool = new Pool<Bullet>(){
        @Override
        protected Bullet newObject(){
            return new Bullet();
        }
    };
    private Bullet bItem;
    private int bLen;
    private Bullet b;

    //array containing active parachute bombers, pool
    private final Array<ParachuteBomber> activeParachuteBombers = new Array<ParachuteBomber>();
    private final Pool<ParachuteBomber> parachuteBomberPool = new Pool<ParachuteBomber>() {
        @Override
        protected ParachuteBomber newObject() {
            return new ParachuteBomber();
        }
    };
    private ParachuteBomber pbItem;
    private int pbLen;
    private ParachuteBomber pb;

    //touchpad and stage declaration
    private Stage stage;
    private Touchpad touchpad;
    private Skin touchpadSkin;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Drawable touchBackground;
    private Drawable touchKnob;

    private BitmapFont font;

    private Texture shootBtnUpTexture, shootBtnDownTexture;
    private ImageButton shootBtn;
    private ImageButton.ImageButtonStyle shootBtnStyle;

//    button to enable touch screen user to shoot?
//    private TextButton shootButton;
//    private TextButton.TextButtonStyle textButtonStyle;
//    BitmapFont font;
//    Skin skin;
//    TextureAtlas buttonAtlas;

    //camera and background
    private Vector3 camPos;
    private float lerp = 0.5f;
    private Texture bg1;
    private int srcY = 0,srcX = 0;


    public GameState(GameStateManager gsm){
        super(gsm);

        //sets the game state and calls init
        cam.setToOrtho(false, 1920, 1080);
//        cam.setToOrtho(false, 1920, 1080);
    }

    @Override
    public void init() {
        ratioX = ShooterGame.SCALE_RATIO_X;
        ratioY = ShooterGame.SCALE_RATIO_Y;

        spriteBatch = new SpriteBatch();

        bg1 = new Texture(Gdx.files.internal("data/BackgroundElements/starsbg.png"));

        initTouchpad();

        initShootBtn();

        font = new BitmapFont();

        stage = new Stage();
        stage.addActor(touchpad);
        stage.addActor(shootBtn);
        Gdx.input.setInputProcessor(stage);

        player = new Player();
        level = 0;
        dt = 0;
        rateOfFire = 0.3f;
        timeSinceLastFire = 0f;
    }

    @Override
    public void update(float dt) {
        if(player.isAlive()) {
            this.dt = dt;
            handleInput();
            player.update(dt);

            timeSinceLastFire+=dt;
            if(timeSinceLastFire > rateOfFire)
                timeSinceLastFire = rateOfFire;

            //update the active parachute bombers
            pbLen = activeParachuteBombers.size;
            for (int i = pbLen; --i >= 0; ) {
                pbItem = activeParachuteBombers.get(i);
                if (!pbItem.isAlive()) {
                    activeParachuteBombers.removeIndex(i);
                    parachuteBomberPool.free(pbItem);
                }
                pbItem.update(dt);
            }

            //update the active bullets
            bLen = activeBullets.size;
            for (int i = bLen; --i >= 0; ) {
                bItem = activeBullets.get(i);
                if (!bItem.isAlive()) {
                    activeBullets.removeIndex(i);
                    bulletPool.free(bItem);
                }
                bItem.update(dt);
            }

            checkCollisions();

            //spawns enemies based on level.
            if (activeParachuteBombers.size == 0) {
                betweenLevelTimer += dt;
                if (betweenLevelTimer > 4) {
                    betweenLevelTimer -= 4;
                    level++;
                    System.out.println("LEVEL " + level);
                    spawnParachuteBombers();
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

        font.draw(spriteBatch, "Level: "+level +". Lives Left: "+(int)player.getLives(), 30, 30);

        spriteBatch.end();

        stage.draw();
    }

    @Override
    public void handleInput() {
        player.setKnobPosition(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());

        if(shootBtn.isPressed()){
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
        player.dispose();
        for(Bullet b : activeBullets)
            b.dispose();
        for(ParachuteBomber pb : activeParachuteBombers)
            pb.dispose();
    }

    private void checkCollisions(){
        //check bullets with parachutebomber collisions
        for(int i=0; i<activeBullets.size; i++){
            b = activeBullets.get(i);
            for(int j=0; j<activeParachuteBombers.size; j++){
                pb = activeParachuteBombers.get(j);
                if(pb.collides(b.getBounds()) || b.collides(pb.getBounds())){
                    b.setAlive(false);
                    pb.setAlive(false);
                }
            }
        }

        //check player with parachutebomber collisions
        for(int i=0; i<activeParachuteBombers.size; i++){
            pb=activeParachuteBombers.get(i);
            if(player.collides(pb.getBounds()) || pb.collides(player.getBounds())){
                player.removeLife();
                pb.setAlive(false);
            }
        }
    }

    private void initTouchpad(){
        //touchpad and stage initialising
        touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("data/touchBackground.png"));
        touchpadSkin.add("touchKnob", new Texture("data/touchKnob.png"));
        touchpadStyle = new Touchpad.TouchpadStyle();

        //pixmap background instead of png background? allows transparency
        Pixmap bg = new Pixmap(200, 200, Pixmap.Format.RGBA8888);
        bg.setBlending(Pixmap.Blending.None);
        bg.setColor(1, 1, 1, 0.2f);
        bg.fillCircle(100, 100, 100);
        touchpadStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(bg)));

        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
//        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;

        touchpad = new Touchpad(2f, touchpadStyle);
        touchpad.setBounds(150f*ratioX,60f*ratioY,300f*ratioX,300f*ratioY);
    }

    private void initShootBtn(){
        shootBtnUpTexture = new Texture(Gdx.files.internal("data/Buttons/btnDefault.png"));
        shootBtnDownTexture = new Texture(Gdx.files.internal("data/Buttons/btnPressed.png"));

        TextureRegion shootBtnUpTextureRegion = new TextureRegion(shootBtnUpTexture);
        TextureRegion shootBtnDownTextureRegion = new TextureRegion(shootBtnDownTexture);

        shootBtnStyle = new ImageButton.ImageButtonStyle();
        shootBtnStyle.imageUp = new TextureRegionDrawable(shootBtnUpTextureRegion);
        shootBtnStyle.imageDown = new TextureRegionDrawable(shootBtnDownTextureRegion);

        shootBtn = new ImageButton(shootBtnStyle);
        shootBtn.setBounds(1400f*ratioX,50*ratioY,500*ratioX,500*ratioY);
    }

    private void spawnParachuteBombers() {
        for(int i = 0; i<3+(5*level/2); i++) {
            pbItem = parachuteBomberPool.obtain();
            pbItem.init();
            activeParachuteBombers.add(pbItem);
        }
    }

    private void shoot(){
        if(timeSinceLastFire >= rateOfFire){
            timeSinceLastFire -= rateOfFire;
            bItem = bulletPool.obtain();
            bItem.init(player.getX()+75, player.getY()+25, 700f, 0f);
            activeBullets.add(bItem);
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

}
