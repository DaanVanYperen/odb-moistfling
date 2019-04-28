package net.mostlyoriginal.game.system.view;

import com.artemis.E;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.api.manager.AbstractAssetSystem;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.SpriteData;
import net.mostlyoriginal.game.system.render.SpriteLibrary;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
@Wire
public class GameScreenAssetSystem extends AbstractAssetSystem {
    public static final int DANCING_MAN_HEIGHT = 36;
    public static final int UNIT = 32;
    public static final int ENTRANCE_WIDTH = UNIT * 3;
    public static final int TOILET_WIDTH = UNIT;
    public static final int SINK_WIDTH = UNIT;
    public static final int URINAL_WIDTH = UNIT;
    public static final int TIPS_WIDTH = UNIT;
    public static final int SUPPLY_CLOSET_WIDTH = UNIT * 2;
    public static final int VISITOR_WIDTH = 24;
    public static final int VISITOR_HEIGHT = 38;

    public static final int LAYER_BACKGROUND = 1;
    public static final int LAYER_BEHIND_ACTORS = 5;
    public static final int LAYER_CLOCK = 20;
    public static final int LAYER_TOILET_DOOR = 100;
    public static final int LAYER_PRESIDENT = 1800;
    public static final int LAYER_PRESIDENT_HEAD = 1850;
    public static final int LAYER_CAR = 1850;
    public static final int LAYER_ACTORS = 2000;
    public static final int LAYER_PLAYER = 1000;
    public static final int LAYER_ICONS = 2100;
    public static final int LAYER_PARTICLES = 3000;
    public static final int LAYER_ACTORS_BUSY = 90;

    public static final int DEFAULT_MODULE_HEIGHT = UNIT * 5 + 16;
    public static final int PLAYER_WIDTH = 24;
    public static final int PLAYER_HEIGHT = 36;
    public static final int MAIN_DOOR_WIDTH = 24;
    public static final float WALK_FRAME_DURATION = 0.03f * (150f / GameRules.WALKING_SPEED_VISITORS);
    public static final float PLAYER_IDLE_FRAME_DURATION = 0.2f;
    public static final float PLAYER_USE_FRAME_DURATION = 0.2f;
    public static final float PLAYER_WALK_FRAME_DURATION = 0.06f;
    public static final int BUILDING_WIDTH = 128;
    public static final int BUILDING_HEIGHT = 360;

    private static final float WASH_FRAME_DURATION = 0.6f;
    public static final float LOW_VOLUME = 0.01f;
    private Music music;
    private SpriteLibrary spriteLibrary;

    public GameScreenAssetSystem() {
        super("tileset.png");
    }

    @Override
    protected void initialize() {
        super.initialize();

        loadSprites();

        sfxVolume = 0.3f;

        loadSounds(new String[]{
                "sfx_interact_6",
                "sfx_magic_1",
                "sfx_magic_2",
                "sfx_magic_3",
                "sfx_money_1",
                "sfx_pickup",
                "sfx_putdown"
        });

        sfxVolume = 0.15f;

        playMusic("sfx/music_shop.mp3");
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


    public void playSfx(String... names) {
        playSfx(names[MathUtils.random(0, names.length - 1)], sfxVolume);
    }

    public void playSfx(float volume, String... names) {
        playSfx(names[MathUtils.random(0, names.length - 1)]);
    }


    public void playSfx(String name, float volume) {
        if (volume > 0 && GameRules.sfxOn) {
            Sound sfx = getSfx(name);
            if (sfx != null) {
                sfx.stop();
                sfx.play(volume, MathUtils.random(1f, 1.04f), 0);
            }
        }
    }

    public void playSfx(String name) {
        if ( GameRules.sfxOn) {
            super.playSfx(name);
        }
    }
}
