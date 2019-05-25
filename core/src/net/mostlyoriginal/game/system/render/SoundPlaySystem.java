package net.mostlyoriginal.game.system.render;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.PlaySound;

import java.util.HashMap;

/**
 * Sfx
 *
 * @author Daan van Yperen
 */
@All(PlaySound.class)
public class SoundPlaySystem extends FluidIteratingSystem {

    private final String[] soundFiles;

    public SoundPlaySystem(String ... soundFiles) {
        this.soundFiles = soundFiles;
    }

    @Override
    protected void initialize() {
        super.initialize();
        loadSounds(soundFiles);
    }

    @Override
    protected void process(E e) {
        playSfx(e.playSoundId(), e.playSoundVolume());
        e.deleteFromWorld();
    }


    public HashMap<String, Sound> sounds = new HashMap<>();
    protected float sfxVolume = 0.2f;


    protected void loadSounds(String[] soundnames) {
        for (String identifier : soundnames) {
            sounds.put(identifier, Gdx.audio.newSound(Gdx.files.internal("sfx/" + identifier + ".mp3")));
        }
    }

    public void playSfx(String... names) {
        playSfx(names[MathUtils.random(0, names.length - 1)], sfxVolume);
    }

    public void playSfx(float volume, String... names) {
        playSfx(names[MathUtils.random(0, names.length - 1)]);
    }

    public Sound getSfx(final String identifier) {
        return sounds.get(identifier);
    }


    public void playSfx(String name, float volume) {
        if (volume > 0 && GameRules.sfxOn) {
            play(name, volume);
        }
    }

    private void play(String name, float volume) {
        Sound sfx = getSfx(name);
        if (sfx != null) {
            sfx.stop();
            sfx.play(volume, MathUtils.random(1f, 1.04f), 0);
        }
    }

    public void playSfx(String name) {
        if ( GameRules.sfxOn) {
            if (sfxVolume > 0) {
                play(name, sfxVolume);
            }
        }
    }

}
