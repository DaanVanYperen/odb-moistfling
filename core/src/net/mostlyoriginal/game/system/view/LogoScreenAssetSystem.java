package net.mostlyoriginal.game.system.view;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.SpriteData;
import net.mostlyoriginal.game.system.render.SpriteLibrary;

/**
 * @author Daan van Yperen
 */
@Wire
public class LogoScreenAssetSystem extends AbstractAssetSystem {

	public static final int LOGO_WIDTH = 738;
	public static final int LOGO_HEIGHT = 182;
	private SpriteLibrary spriteLibrary;

	public LogoScreenAssetSystem() {
		super("tileset.png");
	}

	@Override
	protected void initialize() {
		super.initialize();
		loadSprites();
		//add("logo", 359,665, LOGO_WIDTH, LOGO_HEIGHT, 1);
//		add("transition_red", 0,312, 1, 13, 1);
//		add("trans//		ition_white", 1,312, 1, 13, 1);
		playMusic("sfx/music_title.mp3");
	}

	public void playMusic(String mp3) {
		if (GameRules.music != null) {
			GameRules.music.stop();
			GameRules.music.dispose();
		}

		GameRules.music = Gdx.audio.newMusic(Gdx.files.internal(
				mp3));
		GameRules.music.stop();
		GameRules.music.setLooping(true);
		if (GameRules.musicOn ) {
			GameRules.music.play();
		}
		GameRules.music.setPan(0, 0.1f);
	}



	private void loadSprites() {
		final Json json = new Json();
		spriteLibrary = json.fromJson(SpriteLibrary.class, Gdx.files.internal("sprites.json"));
		for (SpriteData sprite : spriteLibrary.sprites) {
			Animation animation = add(sprite.id, sprite.x, sprite.y, sprite.width, sprite.height, sprite.countX, sprite.countY, this.tileset, sprite.milliseconds * 0.001f);
			if (!sprite.repeat) {
				animation.setPlayMode(Animation.PlayMode.NORMAL);
			} else animation.setPlayMode(Animation.PlayMode.LOOP);
		}
	}

	@Override
	public Animation get(String identifier) {
		return super.get(identifier);
	}

}
