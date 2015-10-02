package com.libgdx.shooter.gamestates;

import com.libgdx.shooter.managers.GameStateManager;

/**
 * Created by Conal on 30/09/2015.
 */
public abstract class State {

    public GameStateManager gameStateManager;

    public State (GameStateManager gsm){
        this.gameStateManager = gsm;
        init();
    }

    public abstract void init();
    public abstract void update(float delta);
    public abstract void render();
    public abstract void handleInput();
    public abstract void dispose();
}
