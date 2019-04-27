package net.mostlyoriginal.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.GdxArtemisGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GameRules.SCREEN_WIDTH;
		config.height = GameRules.SCREEN_HEIGHT;
		config.title = "Orange Duty";
		new LwjglApplication(new GdxArtemisGame(), config);
		}
		}
