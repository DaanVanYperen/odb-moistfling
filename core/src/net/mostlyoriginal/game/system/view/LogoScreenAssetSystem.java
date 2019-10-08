package net.mostlyoriginal.game.system.view;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

	public static final int LOGO_WIDTH = 127;
	public static final int LOGO_HEIGHT = 116;
	private SpriteLibrary spriteLibrary;

	public LogoScreenAssetSystem() {
		super("tileset.png");
	}

	@Override
	protected void processSystem() {
		super.processSystem();
	}

	@Override
	protected void initialize() {
		super.initialize();
		loadSprites();
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
