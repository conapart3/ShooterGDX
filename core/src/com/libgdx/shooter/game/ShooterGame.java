package com.libgdx.shooter.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.libgdx.shooter.Context;
import com.libgdx.shooter.managers.GameStateManager;
import com.libgdx.shooter.managers.StyleManager;


public class ShooterGame extends Game {

    public static int SCREEN_WIDTH, SCREEN_HEIGHT;
    public static int WORLD_WIDTH, WORLD_HEIGHT;
    public static int TARGET_WIDTH = 1920, TARGET_HEIGHT = 1080; // Target display todo: sort out camera and viewport
//    public static float SCALE_RATIO_X, SCALE_RATIO_Y;
    public static float SCREEN_ASPECT_RATIO, WORLD_ASPECT_RATIO;
    public static int GROUND_OFFSET, CEILING_OFFSET, PLAYER_CEILING_OFFSET;
    public static Context context;
    public FPSLogger fpsLogger;

    //	public static OrthographicCamera cam;
//	public Viewport viewport;
    private GameStateManager gsm;
    private float dt;

    public static Context getCurrentContext() {
        if (context == null) {
            context = new Context();
        }
        return context;
    }

    /*create all images sounds and media for game*/
    @Override
    public void create() {
        context = getCurrentContext();
        context.level = 1;
        context.score = 0;

        // Initialize the style manager static members
        StyleManager.init();

        //put all images your going to render in a spritebatch.
        //this batches up all images and renders at once. WAY MORE EFFICIENT

        //this sets the size of the world. Changing this will change how much fits in the screen and
        // will enlarge size of objects. This should be set to 1920 x 1080 which is the maximum
        // resolution, it will be scaled down on smaller screens
        WORLD_WIDTH = 256000;
        WORLD_HEIGHT = 1080;

        //this is the actual screen width.
        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();

        //this calculates the ratio between actual screen and world width/height. Use as a multiplier
        //for onscreen displays to show in the correct positions?
        // i.e. in enemy class enemy_spawn_position_X = pixmap_dot_position * WORLD_WIDTH
//        SCALE_RATIO_X = (float) SCREEN_WIDTH / (float) WORLD_WIDTH;
//        SCALE_RATIO_Y = (float) SCREEN_HEIGHT / (float) WORLD_HEIGHT;

//        SCREEN_ASPECT_RATIO = (float) SCREEN_HEIGHT / (float) SCREEN_WIDTH;
//        WORLD_ASPECT_RATIO = (float) WORLD_HEIGHT / (float) WORLD_WIDTH;

        GROUND_OFFSET = 145;
        CEILING_OFFSET = 900;
        PLAYER_CEILING_OFFSET = 1000;

        fpsLogger = new FPSLogger();

        gsm = new GameStateManager();

//		cam = new OrthographicCamera(108*aspectRatio, 108);
//		viewport = new FillViewport(1920, 1080, cam);
//		viewport = new StretchViewport(1920, 1080, cam);
//		viewport = new FitViewport(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, cam);
//		viewport.apply();
//		cam.translate(SCREEN_WIDTH/2, SCREEN_HEIGHT/2);
//		cam.translate(SCREEN_WIDTH, SCREEN_HEIGHT);
//		cam.translate(0,0);
//		cam.position.set(WORLD_WIDTH/2,WORLD_HEIGHT/2,0);
//		cam.update();

//		gsm.create();

    }

    /*called every 0.025 seconds by the game loop
    * draw the media element of the game
    * move images, update animations, etc*/
    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);//r g b and transparency
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //fps onscreen logging
        fpsLogger.log();

        dt = Gdx.graphics.getDeltaTime();
        gsm.update(dt);
        gsm.render();
    }

    @Override
    public void resize(int width, int height) {
        // todo: make the viewport instantiation in the ShooterGame class?
//		viewport.update(width,height);
//		cam.position.set(cam.viewportWidth/2,cam.viewportHeight/2,0);
//		gsm.resize(width, height);
    }

    @Override
    public void pause() {
        gsm.pause();
    }

    @Override
    public void resume() {
        gsm.resume();
    }

}
