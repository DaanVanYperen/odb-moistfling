package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Lifter;
import net.mostlyoriginal.game.manager.ItemRepository;


/**
 * @author Daan van Yperen
 */
@All({Lifter.class, Pos.class})
public class PickupSystem extends FluidIteratingSystem {

    private static final int CARRIED_OBJECT_LIFTING_HEIGHT = 0;

    RenderBatchingSystem renderBatchingSystem;
    PickupManager pickupManager;
    private ItemRepository itemRepository;

    @Override
    protected void process(E e) {
        if (e.lifterAttemptLifting()) {
            if (!e.hasLifting()) {
                attemptPickup(e);
            }
        } else {
            if (e.hasLifting()) {
                attemptDrop(e);
            }
        }
        followCarrier(e);
    }

    private void followCarrier(E actor) {
        if (actor.hasLifting() && actor.getLifting().id != -1) {
            E lifting = E.E(actor.getLifting().id);
            lifting.posX(actor.getPos().xy.x);
            lifting.posY(actor.getPos().xy.y + CARRIED_OBJECT_LIFTING_HEIGHT);
        }
    }

    private void attemptDrop(E actor) {
        E item = E.E(actor.liftingId());
        if (!actor.isMoving()) {
            E itemOnFloor = pickupManager.getOverlapping(actor);

            actor.removeLifting();

            item.gridPos(actor.getGridPos()).removeLifted().renderLayer(GameRules.LAYER_ITEM);
            renderBatchingSystem.sortedDirty = true;

            if (itemOnFloor != null) {
                attemptPickup(actor, itemOnFloor);
                actor.lifterAttemptLifting(true); // we swapped something, ontinue lifting.
            }
        }
    }

    private void attemptPickup(E actor) {
        E item = pickupManager.getOverlapping(actor);
        attemptPickup(actor, item);
    }

    private void attemptPickup(E actor, E item) {
        if (item != null) {
            actor.liftingId(item.id());
            actor.getLifter().itemsLifted++;
            item
                    .removeGridPos()
                    .removeFloating()
                    .lifted()
                    .renderLayer(GameRules.LAYER_ITEM_CARRIED);
            renderBatchingSystem.sortedDirty = true;
        }
    }
}
