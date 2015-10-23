package com.libgdx.shooter.managers;

import com.libgdx.shooter.gamestates.GameOverState;
import com.libgdx.shooter.gamestates.GameState;
import com.libgdx.shooter.gamestates.State;

import java.util.Stack;

/**
 * Created by Conal on 30/09/2015.
 */
public class GameStateManager {
    public State currentState;

//    private Stack<State> states;

    public static final int MENU = 0;
    public static final int GAME = 1;
    public static final int GAME_OVER = 2;

    public GameStateManager(){
        setState(GAME);
    }

    public void setState(int state){
        if(currentState != null)
            currentState.dispose();

        if(state==GAME)
            currentState = new GameState(this);

        if(state==GAME_OVER)
            currentState = new GameOverState(this);
    }

    public void update(float dt) {
        currentState.update(dt);
    }

    public void render(){
        currentState.render();
    }
}
