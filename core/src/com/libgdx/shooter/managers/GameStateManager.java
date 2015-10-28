package com.libgdx.shooter.managers;

import com.libgdx.shooter.gamestates.GameOverState;
import com.libgdx.shooter.gamestates.GameState;
import com.libgdx.shooter.gamestates.MenuState;
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
        setState(MENU,0);
    }

    public void setState(int state, int score){
        if(currentState != null)
            currentState.dispose();

        if(state==MENU)
            currentState = new MenuState(this);

        if(state==GAME)
            currentState = new GameState(this);

        if(state==GAME_OVER)
            currentState = new GameOverState(this, score);

        currentState.create();
        currentState.update(0f);
    }

    public void create(){
        currentState.create();
    }

    public void update(float dt) {
        currentState.update(dt);
    }

    public void render(){
        currentState.render();
    }

    public void resize(int width, int height){
        currentState.resize(width, height);
    }

    public void pause () {
        currentState.pause();
    }

    public void resume () {
        currentState.resume();
    }
}
