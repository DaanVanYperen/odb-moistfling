package net.mostlyoriginal.game.system.logic;

import com.artemis.E;
import com.artemis.annotations.All;
import net.mostlyoriginal.api.util.Cooldown;
import net.mostlyoriginal.game.component.Oxygen;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.system.common.FluidSystem;

/**
 * @author Daan van Yperen
 */
@All({Player.class, Oxygen.class})
public class BreathingSfxSystem extends FluidSystem {

    private static final float DEFAULT_COOLDOWN = 5f;
    float age=0;
    private Cooldown cooldown = Cooldown.withInterval(DEFAULT_COOLDOWN).autoReset(true);

    @Override
    protected void process(E e) {
        if ( e.isDead() ) return;
        age += world.delta;
        if ( age % DEFAULT_COOLDOWN <= 1.2f) {
            E.withTag("player").inhale();
        } else{
            E.withTag("player").removeInhale();
        }

        if ( cooldown.ready(world.delta) ) {
            float p = e.oxygenPercentage();
            if (p<=33) {
                E.E().playSound("breath-suffocating",0.3f);
            } else if (p<=66) {
                E.E().playSound("breath-laboured",0.3f);
            } else {
                E.E().playSound("breath-normal",0.05f);
            }
        }
    }
}
