package net.mostlyoriginal.game.system.logic;

import com.artemis.E;
import com.artemis.annotations.All;
import com.badlogic.gdx.audio.Sound;
import net.mostlyoriginal.api.util.Cooldown;
import net.mostlyoriginal.game.component.Leak;
import net.mostlyoriginal.game.component.Oxygen;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.system.common.FluidSystem;
import net.mostlyoriginal.game.system.render.SoundPlaySystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
@All({Player.class})
public class LeakSfxSystem extends FluidSystem {

    SoundPlaySystem soundPlaySystem;

    private static final float DEFAULT_COOLDOWN = 5f;
    private Cooldown cooldown = Cooldown.withInterval(DEFAULT_COOLDOWN).autoReset(true);
    private Sound sfx;
    private boolean isPlaying=false;
    private long soundId;


    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void process(E e) {


        if ( e.hasLeak() && !e.isDead() ) {
            if ( !isPlaying ) {
                if ( sfx == null ) {
                    sfx = soundPlaySystem.getSfx("leak-loop.wav");
                }
                isPlaying=true;
                soundId = sfx.loop(0.05f * e.leakLeaks());
            }
            sfx.setVolume(soundId,0.05f * e.leakLeaks());
        } else {
            if ( isPlaying ) {
                isPlaying=false;
                sfx.stop();
            }
        }
    }
}
