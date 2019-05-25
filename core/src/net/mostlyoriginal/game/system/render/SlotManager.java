package net.mostlyoriginal.game.system.render;

import com.artemis.BaseEntitySystem;
import com.artemis.E;
import com.artemis.annotations.All;
import com.artemis.annotations.Exclude;
import com.artemis.utils.IntBag;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.component.inventory.Inventory;

/**
 * @author Daan van Yperen
 */
@All(Inventory.class)
@Exclude(Player.class)
public class SlotManager extends BaseEntitySystem {

    private String liftingType;

    public E getSlotAt(GridPos gridPos) {
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();
        for (int i = 0, s = actives.size(); s > i; i++) {
            E slot = E.E(ids[i]);
            if (slot.gridPosOverlaps(gridPos)) return slot;
        }
        return null;
    }

    public E getSlotAt(GridPos gridPos, int x, int y) {
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();
        final int gridX = gridPos.x + x;
        final int gridY = gridPos.y + y;
        for (int i = 0, s = actives.size(); s > i; i++) {
            E slot = E.E(ids[i]);
            if (slot.gridPosOverlaps(gridX, gridY)) return slot;
        }
        return null;
    }

    @Override
    protected void processSystem() {
    }
}
