package net.mostlyoriginal.game.system.render;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.game.component.Blinking;

/**
 * Check if player is over solid surface.s
 *
 * @author Daan van Yperen
 */
@All({Blinking.class})
public class BlinkingSystem extends FluidIteratingSystem {
    @Override
    protected void process(E e) {
        e.getBlinking().duration -= world.delta;
        if ( e.getBlinking().duration <= 0 ) {
            e.removeBlinking();
        }
    }
}
