package net.mostlyoriginal.game.system;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.Interpolation;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.game.component.InvisibleDuringNight;

/**
 * @author Daan van Yperen
 */
@All(InvisibleDuringNight.class)
public class NightSystem extends FluidIteratingSystem {

    private static final int CYCLE_DURATION = 30;
    private boolean nighttime;
    private float cooldown=CYCLE_DURATION;

    private boolean flipped = false;

    @Override
    protected void begin() {
        super.begin();
        E player = E.withTag("player");
        nighttime = player.playerNighttime();
        cooldown -= world.delta;
        if ( cooldown <= 0 ) {
            cooldown += CYCLE_DURATION;
            flipped = true;
            nighttime = !nighttime;
            player.playerNighttime(nighttime);
            if ( !nighttime ) {
                player.getPlayer().day++;
            }
        } else flipped = false;
    }

    @Override
    protected void process(E e) {
        if ( flipped  ) {
            if ( nighttime ) {
                e.script(JamOperationFactory.tintBetween(Tint.WHITE, Tint.TRANSPARENT, 2f, Interpolation.fade));
            } else {
                e.script(JamOperationFactory.tintBetween(Tint.TRANSPARENT, Tint.WHITE, 2f, Interpolation.fade));
            }
        }
    }
}
