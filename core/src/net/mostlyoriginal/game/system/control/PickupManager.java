package net.mostlyoriginal.game.system.control;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.EntitySubscription;
import com.artemis.annotations.All;
import com.artemis.annotations.Exclude;
import com.artemis.utils.IntBag;
import net.mostlyoriginal.game.component.CanPickup;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.Moving;

/**
 * @author Daan van Yperen
 */
public class PickupManager extends BaseSystem{

    @All({CanPickup.class, GridPos.class})
    @Exclude(Moving.class)
    private EntitySubscription pickupables;

    @Override
    protected void processSystem() {
    }

    public E getOverlapping(E actor) {
        IntBag pickupEntities = pickupables.getEntities();
        E overlaps = null;
        for (int i = 0, s = pickupEntities.size(); i < s; i++) {
            E item = E.E(pickupEntities.get(i));
            if (!item.hasLifted() && item.getGridPos().overlaps(actor.getGridPos())) {
                overlaps = item;
            }
        }
        return overlaps;
    }
}
