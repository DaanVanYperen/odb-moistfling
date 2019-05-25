package net.mostlyoriginal.game.system.action;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.api.component.graphics.RendererSingleton;
import net.mostlyoriginal.api.event.common.EventSystem;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.action.ActionPickup;
import net.mostlyoriginal.game.events.EventItemPickup;
import net.mostlyoriginal.game.system.future.FutureSpawnUtility;


/**
 * Attempt to pickup target.
 *
 * @author Daan van Yperen
 */
@All(ActionPickup.class)
public class PickupActionSystem extends FluidIteratingSystem {

    private RendererSingleton rendererSingleton;
    private EventSystem es;

    @Override
    protected void process(E actor) {
        final ActionPickup action = actor.getActionPickup();
        if (action.target != -1 && !actor.hasHolding() && !actor.isMoving()) {
            final E item = E.E(action.target);

            if (actor.hasPlayer()) {
                es.dispatch(new EventItemPickup(item.id()));
            }

            // @todo remove shopper as criteria here.
            if (item.itemCount() > 1 && !actor.hasShopper()) { // shoppers grab the whole stack.
                System.out.println(actor +" takes from stack " + item);
                // take from stack.
                item.getItem().count--;

                final E futureItem = FutureSpawnUtility.item(item.itemType(), 1, item.gridPosX(), item.gridPosY());

                // pickup the split on next cycle.
                actor.removeActionPickup();
                actor.actionPickupTarget(futureItem.id());
            } else {
                // pick up final item.
                if ( item.hasInside()) {
                    System.out.println(actor + " taking from inventory " + item.getInside().id);
                } else {
                    System.out.println(actor + " taking from floor " + item);
                }

                InventoryUtils.removeFromInventory(item);

                actor.removeActionPickup();
                actor.holdingId(item.id());

                item
                        .removeInside()
                        .removeGridPos()
                        .removeFloating()
                        .renderLayer(GameRules.LAYER_ITEM_CARRIED);
                rendererSingleton.sortedDirty = true;
            }

            // @todo get rid of this.
            actor.getLifter().itemsLifted++;

            // @todo decouple
            if (actor.hasPlayer())
                E.E().playSound("sfx_pickup");

        } else
            actor.removeActionPickup();
    }
}
