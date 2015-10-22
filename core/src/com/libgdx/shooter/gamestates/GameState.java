package com.libgdx.shooter.gamestates;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.libgdx.shooter.entities.Player;
import com.libgdx.shooter.game.ShooterGame;
import com.libgdx.shooter.managers.GameStateManager;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

/**
 * Created by Conal on 30/09/2015.
 */
public class GameState extends State{

    private SpriteBatch spriteBatch;
    private Player player;

    //touchpad declaration
    private Stage stage;
    private Touchpad touchpad;
    private Skin touchpadSkin;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private float px,py;

    private Vector3 camPos;
    private float lerp = 0.5f;
    private Texture bg1;
    private int srcY = 0,srcX = 0;
//    private Sprite sprite1;

    public GameState(GameStateManager gsm){
        super(gsm);
        //sets the game state and calls init
    }

    @Override
    public void init() {
        spriteBatch = new SpriteBatch();

        bg1 = new Texture(Gdx.files.internal("data/BackgroundElements/cactibackground.jpg"));
//        bg1 = new Texture(Gdx.files.internal("data/BackgroundElements/bgLayer1.png"));


//        sprite1 = new Sprite(bg1);
//        sprite1.setOrigin(0,0);
//        sprite1.setPosition(-sprite1.getWidth()/2, -sprite1.getHeight()/2);

        player = new Player();

        initTouchpad();

//        stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, spriteBatch);
        stage = new Stage();
        stage.addActor(touchpad);
        Gdx.input.setInputProcessor(stage);


    }

    @Override
    public void update(float dt) {

        handleInput();

        player.update(dt);

        updateCamera(dt);

    }

    @Override
    public void render() {
        spriteBatch.setProjectionMatrix(ShooterGame.cam.combined);

        spriteBatch.begin();

        bg1.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        spriteBatch.draw(bg1, 0, 0, srcX, srcY, bg1.getWidth(), bg1.getHeight());
//        srcX+=1;

        player.render(spriteBatch, Gdx.graphics.getDeltaTime());

        spriteBatch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void handleInput() {
//        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
//            player.setLeftPressed(Gdx.input.isKeyPressed(Input.Keys.LEFT));
//            player.setRightPressed(Gdx.input.isKeyPressed(Input.Keys.RIGHT));
//            player.setUpPressed(Gdx.input.isKeyPressed(Input.Keys.UP));
//            player.setDownPressed(Gdx.input.isKeyPressed(Input.Keys.DOWN));

//            float px = player.getX() + touchpad.getKnobPercentX() * player.getSpeed();
//            float py = player.getY() + touchpad.getKnobPercentY() * player.getSpeed();
//            player.setPosition(px, px);
        player.setKnobPosition(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());

        double degrees = Math.atan2(touchpad.getKnobPercentY(), touchpad.getKnobPercentX());
        if(degrees!=0) {
            float rotation = (float) degrees;
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
//        Pixmap bg = new Pixmap(200, 200, Pixmap.Format.RGBA8888);
//        bg.setBlending(Pixmap.Blending.None);
//        bg.setColor(1, 1, 1, 0.6f);
//        bg.fillCircle(100, 100, 100);
//        touchpadStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(bg)));

        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        touchpad = new Touchpad(10f, touchpadStyle);
        touchpad.setBounds(240f,60f,300f,300f);
    }

    private void updateCamera(float dt){

        //update the camera position based on player position
        camPos = ShooterGame.cam.position;
//        if(player.getX()-camPos.x > 100)
        camPos.x += (player.getX() - camPos.x) * lerp * dt;
//        if(player.getY()-camPos.y > 100)
        camPos.y += (player.getY() - camPos.y) * lerp * dt;
        ShooterGame.cam.position.set(camPos);

        ShooterGame.cam.update();

    }
}
