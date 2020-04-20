package net.mostlyoriginal.game;

import com.badlogic.gdx.Game;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.screen.LogoScreen;

import static com.badlogic.gdx.Input.Keys.G;
import static net.mostlyoriginal.game.GameRules.*;

public class GdxArtemisGame extends Game {

	private static GdxArtemisGame instance;
	public static int president=0;

	@Override
	public void create() {
		instance = this;
		restart();
	}

	public void restart() {
		GameRules.nextMap=DEBUG_ENABLED ? "map_debug.tmx" : "map1.tmx";
		setScreen(LOGO_ENABLED ? new LogoScreen() : new GameScreen());
	}

	public static GdxArtemisGame getInstance()
	{
		return instance;
	}
}
