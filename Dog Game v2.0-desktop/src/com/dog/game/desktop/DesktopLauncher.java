package com.dog.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dog.game.MainGame;

public class DesktopLauncher {
	
	static final int WIDTH = 1024;
	static final int HEIGHT = 1024;
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Dog Game";
		config.width = WIDTH;
		config.height = HEIGHT;
		config.useGL30 = true;
		
		new LwjglApplication(new MainGame(), config);
	}
}
