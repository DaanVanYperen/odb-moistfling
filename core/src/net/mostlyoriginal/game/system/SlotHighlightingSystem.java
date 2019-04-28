package net.mostlyoriginal.game.system;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import net.mostlyoriginal.game.Slot;
import net.mostlyoriginal.game.component.GridPos;

/**
 * @author Daan van Yperen
 */
@All(Slot.class)
public class SlotHighlightingSystem extends FluidIteratingSystem {

    private String liftingType;

    @Override
    protected void begin() {
        super.begin();
        E player = E.withTag("player");
        liftingType = player.hasLifting() && player.liftingId() != -1 ? E.E(player.liftingId()).itemType() : null;
    }

    @Override
    protected void process(E e) {
        boolean highlight = acceptsItemType(e,liftingType);
        if (highlight) {
            e.anim("hopper_highlight");
        } else if (e.hasAnim()) e.removeAnim();
    }

    public boolean acceptsItemType(E e, String type) {
        boolean result = false;
        if (type != null) {
            for (String s : e.slotAccepts()) {
                if (type.equals(s) || s.equals("any")) {
                    result = true;
                }
            }
        }
        return result;
    }

    public E getSlotAt(GridPos gridPos) {
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();
        for (int i = 0, s = actives.size(); s > i; i++) {
            E slot = E.E(ids[i]);
            if (slot.gridPosOverlaps(gridPos)) return slot;
        }
        return null;
    }
}
