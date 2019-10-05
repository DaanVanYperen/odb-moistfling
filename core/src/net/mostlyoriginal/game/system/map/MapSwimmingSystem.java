package net.mostlyoriginal.game.system.map;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.component.map.TiledMapSingleton;

/**
 * Check if player is over solid surface.s
 *
 * @author Daan van Yperen
 */
public class MapSwimmingSystem extends FluidIteratingSystem {

    public static boolean DEBUG = false;

    private TiledMapSingleton map;

    private boolean initialized;
    private MapMask walkingMask;

    public MapSwimmingSystem() {
        super(Aspect.all(Physics.class, Pos.class, Bounds.class));
    }

    @Override
    protected void begin() {
        if (!initialized) {
            initialized = true;
            walkingMask = map.createMask("walkable");
            walkingMask.refresh();
        }
    }

    @Override
    protected void end() {
    }

    @Override
    protected void process(E e ) {
        e.swimming(!walkingMask.atScreen(e.getPos().xy.x, e.getPos().xy.y, false));
    }
}
