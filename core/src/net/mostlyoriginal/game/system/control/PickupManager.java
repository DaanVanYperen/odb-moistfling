package net.mostlyoriginal.game.system.control;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.EntitySubscription;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import net.mostlyoriginal.game.component.CanPickup;
import net.mostlyoriginal.game.component.GridPos;

/**
 * @author Daan van Yperen
 */
public class PickupManager extends BaseSystem{

    @All({CanPickup.class, GridPos.class})
    private EntitySubscription pickupables;

    @Override
    protected void processSystem() {
    }

    public boolean canPickup(E item) {
        return !item.isMoving() || item.hasFloating();
    }

    public E getOverlapping(E actor) {
        return getOverlapping(actor,0,0);
    }

    public E getOverlapping(E actor, int offsetX, int offsetY) {
        IntBag pickupEntities = pickupables.getEntities();
        E overlaps = null;
        final int gridX = actor.getGridPos().x + offsetX;
        final int gridY = actor.getGridPos().y + offsetY;
        for (int i = 0, s = pickupEntities.size(); i < s; i++) {
            E item = E.E(pickupEntities.get(i));
            if (!item.hasLifted() && canPickup(item) && item.getGridPos().overlaps(gridX, gridY)) {
                overlaps = item;
            }
        }
        return overlaps;
    }

    public boolean isCarrying(E e, String type) {
        if ( type != null && e.hasLifting() && e.liftingId() != -1 ) {
            if ( type.equals(E.E(e.liftingId()).itemType())) return true;
        }
        return false;
    }
}
