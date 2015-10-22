package com.libgdx.shooter.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.libgdx.shooter.managers.GameStateManager;


public class ShooterGame extends ApplicationAdapter{

	public static int WIDTH;
	public static int HEIGHT;
	public static int WORLD_WIDTH = 192, WORLD_HEIGHT = 108;
	public static float aspectRatio;

	public static OrthographicCamera cam;
	public Viewport viewport;

	private GameStateManager gsm;

	/*create all images sounds and media for game*/
	@Override
	public void create () {
		//put all images your going to render in a spritebatch.
		//this batches up all images and renders at once. WAY MORE EFFICIENT

		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		aspectRatio = (float)HEIGHT / (float)WIDTH;

		cam = new OrthographicCamera(108*aspectRatio, 108);
		viewport = new FillViewport(1920, 1080, cam);
//		viewport = new StretchViewport(1920, 1080, cam);
//		viewport = new FitViewport(WIDTH/2, HEIGHT/2, cam);
		viewport.apply();
//		cam.translate(WIDTH/2, HEIGHT/2);
//		cam.translate(WIDTH, HEIGHT);
//		cam.translate(0,0);
		cam.position.set(WORLD_WIDTH/2,WORLD_HEIGHT/2,0);
		cam.update();

		gsm = new GameStateManager();

	}

	/*called every 0.25 seconds by the game loop
	* draw the media element of the game
	* move images, update animations, etc*/
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);//r g b and transparency
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
	}

	@Override
	public void resize(int width, int height){
		viewport.update(width,height);
		cam.position.set(cam.viewportWidth/2,cam.viewportHeight/2,0);
	}
}
