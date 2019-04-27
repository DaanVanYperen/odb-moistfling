package net.mostlyoriginal.game.system.mechanics;

import com.artemis.E;
import com.artemis.EBag;
import com.artemis.EntitySubscription;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.Hopper;
import net.mostlyoriginal.game.component.Item;
import net.mostlyoriginal.game.component.Machine;
import net.mostlyoriginal.game.system.common.FluidSystem;

/**
 * @author Daan van Yperen
 */
@All({Hopper.class, GridPos.class})
public class HopperDetectionSystem extends FluidSystem {

    @All({Item.class, GridPos.class})
    public EntitySubscription items;

    @Override
    protected void process(E hopperE) {
        updateSlottedItemInHopper(hopperE);
    }

    private void updateSlottedItemInHopper(E hopperE) {
        final Hopper hopper = hopperE.getHopper();

        // unslot.
        hopper.slottedId = -1;

        // find first item to slot.
        final IntBag itemEntities = items.getEntities();
        for (int i = 0, s = itemEntities.size(); i < s; i++) {
            final E item = E.E(itemEntities.get(i));
            if (!item.hasMoving() && item.getGridPos().overlaps(hopperE.getGridPos())) {
                hopper.slottedId = item.id();
                //item.angleRotation(MathUtils.random(0f,100f));
                break;
            }
        }
    }
}
