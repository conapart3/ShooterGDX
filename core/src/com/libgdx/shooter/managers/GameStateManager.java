package com.libgdx.shooter.managers;

import com.libgdx.shooter.gamestates.GameOverState;
import com.libgdx.shooter.gamestates.GameState;
import com.libgdx.shooter.gamestates.LevelCompleteState;
import com.libgdx.shooter.gamestates.MenuState;
import com.libgdx.shooter.gamestates.State;

/**
 * Created by Conal on 30/09/2015.
 */
public class GameStateManager {
    public static final int MENU = 0;

//    private Stack<State> states;
    public static final int GAME = 1;
    public static final int GAME_OVER = 2;
    public static final int LEVEL_COMPLETE_STATE = 3;
    public State currentState;

    public GameStateManager() {
        setState(MENU);
    }

    public void setState(int newState) {
        if (currentState != null)
            currentState.dispose();

        if (newState == MENU)
            currentState = new MenuState(this);

        if (newState == GAME)
            currentState = new GameState(this);

        if (newState == GAME_OVER)
            currentState = new GameOverState(this);

        if (newState == LEVEL_COMPLETE_STATE)
            currentState = new LevelCompleteState(this);

        currentState.create();
        currentState.update(0f);
    }

    public void create() {
        currentState.create();
    }

    public void update(float dt) {
        currentState.update(dt);
    }

    public void render() {
        currentState.render();
    }

    public void resize(int width, int height) {
        currentState.resize(width, height);
    }

    public void pause() {
        currentState.pause();
    }

    public void resume() {
        currentState.resume();
    }
}
