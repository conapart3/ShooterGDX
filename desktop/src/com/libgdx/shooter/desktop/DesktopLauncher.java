package com.libgdx.shooter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.libgdx.shooter.game.ShooterGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "ShooterGDX";
		config.width = 1280;
		config.height = 720;
//		config.width = 1920;
//		config.height = 1080;
		new LwjglApplication(new ShooterGame(), config);
	}
}
