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

import static net.mostlyoriginal.game.system.mechanics.MachineHopperDetectionSystem.MINIMUM_SLOTTED_DURATION_SECONDS;

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
            if (canBeSlotted(item) && item.getGridPos().overlaps(hopperE.getGridPos())) {
                hopper.slottedId = item.id();
                hopper.slottedDuration  += world.delta;
                if ( hopper.slottedDuration > MINIMUM_SLOTTED_DURATION_SECONDS && !item.hasFloating() && !item.hasPlayer() ) {
                    item.floating();
                }
                //item.angleRotation(MathUtils.random(0f,100f));
                return;
            }
        }

        hopper.slottedDuration = 0;
    }

    private boolean canBeSlotted(E item) {
        return !item.hasMoving() || item.hasFloating();
    }
}
