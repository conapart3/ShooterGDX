package com.libgdx.shooter.gamestates;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
public class GameState extends State implements InputProcessor{

    private SpriteBatch spriteBatch;
    private Player player;
    private ArrayList<ParachuteBomber> parachuteBombers;

    //array containing the active bullets
    private final Array<Bullet> activeBullets = new Array<Bullet>();

    //array containing the bullet pool
    private final Pool<Bullet> bulletPool = new Pool<Bullet>(){
        @Override
        protected Bullet newObject(){
            return new Bullet();
        }
    };
    private Bullet item;
    private int len;

    //touchpad and stage declaration
    private Stage stage;
    private Touchpad touchpad;
    private Skin touchpadSkin;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Drawable touchBackground;
    private Drawable touchKnob;

//    private TextButton shootButton;
//    private TextButton.TextButtonStyle textButtonStyle;
//    BitmapFont font;
//    Skin skin;
//    TextureAtlas buttonAtlas;

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
        spriteBatch = new SpriteBatch();

        bg1 = new Texture(Gdx.files.internal("data/BackgroundElements/starsbg.png"));
//        bg1 = new Texture(Gdx.files.internal("data/BackgroundElements/bgrepeat1_1.png"));

        player = new Player();
        parachuteBombers = new ArrayList<ParachuteBomber>();
        spawnParachuteBombers();

        initTouchpad();
//        font = new BitmapFont();
//        skin = new Skin();
//        buttonAtlas = new TextureAtlas(Gdx.files.internal("data/shootButton"))

//        stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, spriteBatch);
        stage = new Stage();
        stage.addActor(touchpad);
//        stage.addActor()
        Gdx.input.setInputProcessor(stage);
//        Gdx.input.setInputProcessor(this);
    }

    private void spawnParachuteBombers() {
        //use pool for parachutebombers too!
        parachuteBombers.add(new ParachuteBomber());
    }

    @Override
    public void update(float dt) {
        handleInput();

        player.update(dt);

        for(int i=0; i<parachuteBombers.size(); i++) {
            parachuteBombers.get(i).update(dt);
        }

        len = activeBullets.size;
        for(int i = len; --i >= 0; ) {
            item = activeBullets.get(i);
            if(item.isAlive() == false){
                activeBullets.removeIndex(i);
                bulletPool.free(item);
            }
            item.update(dt);
        }

        stage.act(dt);

        updateCamera(dt);
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

        for(int i=0; i<parachuteBombers.size(); i++) {
            parachuteBombers.get(i).render(spriteBatch);
        }

        for(int i = activeBullets.size; --i >= 0; ){
            activeBullets.get(i).render(spriteBatch);
        }

        spriteBatch.end();

        stage.draw();
    }

    @Override
    public void handleInput() {
        player.setKnobPosition(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            item = bulletPool.obtain();
            item.init(player.getX(), player.getY(), 700f, 0f);
            activeBullets.add(item);
        }


    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
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
        touchpad.setBounds(ShooterGame.WIDTH-350f,60f,220f,220f);
    }

    private void updateCamera(float dt){

        //update the camera position based on player position
        camPos = cam.position;
//        if(player.getX()-camPos.x > 100)
//        camPos.x += (player.getX() - camPos.x) * lerp * dt;
//        if(player.getY()-camPos.y > 100)
//        camPos.y += (player.getY() - camPos.y) * lerp * dt;
//        cam.position.set(camPos);

//        if(player.getX() > 50)
//            srcX += (player.getX() - srcX) * lerp * dt;

//        srcX += lerp* dt * srcX;

        cam.update();

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        item = bulletPool.obtain();
        item.init(player.getX(), player.getY(), 700f, 0f);
        activeBullets.add(item);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
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
