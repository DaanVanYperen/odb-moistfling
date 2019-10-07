package net.mostlyoriginal.game.system.map;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.ESubscription;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.component.flags.DryLand;
import net.mostlyoriginal.game.component.flags.Walkable;
import net.mostlyoriginal.game.component.map.TiledMapSingleton;

/**
 * Check if player is over solid surface.s
 *
 * @author Daan van Yperen
 */
@All({Physics.class, Pos.class, Bounds.class})
public class MapSwimmingSystem extends FluidIteratingSystem {

    public static boolean DEBUG = false;

    private TiledMapSingleton map;

    private int lastDryLands=-1;
    private MapMask drylandMask;

    @All(Walkable.class)
    ESubscription drylands;

    @Override
    protected void begin() {
        if (lastDryLands != drylands.size()) {
            lastDryLands = drylands.size();
            if ( drylandMask == null ) {
                drylandMask = map.createBlankMask();
            }
            drylandMask.clear();
            for (E e : drylands) {
                drylandMask.set(e.getGridPos().x, e.getGridPos().y,true);
            }
        }
    }

    @Override
    protected void end() {
    }

    @Override
    protected void process(E e ) {
        boolean shouldBeSwimming = !drylandMask.atScreen(e.getPos().xy.x + e.getBounds().cx(), e.getPos().xy.y, false);
        if ( !e.hasSwimming() && shouldBeSwimming )
            E.E().playSound("water1");
        if ( e.hasSwimming() && !shouldBeSwimming )
            E.E().playSound("water2");
        e.swimming(shouldBeSwimming);
    }

    public boolean isOnWater(E item) {
        return !drylandMask.atGrid(item.getGridPos().x, item.getGridPos().y, false);
    }
}
