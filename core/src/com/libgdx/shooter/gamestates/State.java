package com.libgdx.shooter.gamestates;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.libgdx.shooter.managers.GameStateManager;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Conal on 30/09/2015.
 */
public abstract class State {

    protected GameStateManager gameStateManager;
    public OrthographicCamera cam;

    public State (GameStateManager gsm){
        this.gameStateManager = gsm;
        cam = new OrthographicCamera();
        init();
    }

    public abstract void init();
    public abstract void update(float delta);
    public abstract void render();
    public abstract void handleInput();
    public abstract void dispose();
}
