package com.libgdx.shooter.managers;

import com.libgdx.shooter.gamestates.GameState;
import com.libgdx.shooter.gamestates.State;

/**
 * Created by Conal on 30/09/2015.
 */
public class GameStateManager {
    public State currentState;

    public static final int menu = 0;
    public static final int game = 1;
    public static final int gameOver = 2;

    public GameStateManager(){
        setState(game);
    }

    public void setState(int state){
        if(currentState != null)
            currentState.dispose();

        if(state==1)
            currentState = new GameState(this);
    }

    public void update(float dt) {
        currentState.update(dt);
    }

    public void render(){
        currentState.render();
    }
}
