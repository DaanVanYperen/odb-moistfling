package net.mostlyoriginal.game.system.map;

import com.artemis.E;
import com.artemis.ESubscription;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.component.Blinking;
import net.mostlyoriginal.game.component.flags.Walkable;
import net.mostlyoriginal.game.component.map.TiledMapSingleton;

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
