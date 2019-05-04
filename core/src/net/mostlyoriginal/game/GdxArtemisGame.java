package net.mostlyoriginal.game;

import com.badlogic.gdx.Game;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.screen.LogoScreen;

import static net.mostlyoriginal.game.GameRules.LOGO_ENABLED;

public class GdxArtemisGame extends Game {

	private static GdxArtemisGame instance;
	public static int president=0;

	@Override
	public void create() {
		instance = this;
		restart();
	}

	public void restart() {
		setScreen(LOGO_ENABLED ? new LogoScreen() : new GameScreen());
	}

	public static GdxArtemisGame getInstance()
	{
		return instance;
	}
}
