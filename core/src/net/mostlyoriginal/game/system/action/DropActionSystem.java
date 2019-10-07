package net.mostlyoriginal.game.system.action;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.api.component.graphics.RendererSingleton;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.action.ActionDrop;
import net.mostlyoriginal.game.component.inventory.Inventory;
import net.mostlyoriginal.game.system.map.MapSwimmingSystem;


/**
 * Drop target at current location.
 *
 * @author Daan van Yperen
 */
@All(ActionDrop.class)
public class DropActionSystem extends FluidIteratingSystem {

    private RendererSingleton rendererSingleton;
    private MapSwimmingSystem mapSwimmingSystem;

    @Override
    protected void process(E actor) {
        ActionDrop action = actor.getActionDrop();
        if (action.target != -1 && actor.hasHolding() && !actor.isMoving()) {
            if ( action.inventory != -1 ) {
                putItemInside(actor, E.E(action.target), E.E(action.inventory));
            } else {
                dropItem(actor, E.E(action.target));
            }
        }
        actor.removeActionDrop();
    }

    private void dropItem(E actor, E item) {
        stopHolding(actor, item);
        item.gridPos(actor.getGridPos());
                item.posY(actor.getPos().xy.y);
        ;
        if (mapSwimmingSystem.isOnWater(item)) {
            item.submerged();
            E.E().playSound("water2");
        }
    }

    private void putItemInside(E actor, E itemE, E inventoryE) {
        final Inventory inventory = inventoryE.getInventory();
        final E mergeStack = InventoryUtils.getFirstStackOf(inventory, itemE.itemType());
        if (mergeStack != null) {
            System.out.println("Merge " + itemE + " with stack " + mergeStack);
            mergeStack.getItem().count++;
            itemE.deleteFromWorld();
            stopHolding(actor, itemE);
        } else {
            if (!inventory.isFull()) {
                System.out.println("Put  " + itemE + " into " + inventoryE);

                // Remove item from source inventory.
                InventoryUtils.removeFromInventory(itemE);

                inventory.contents.add(itemE.id());
                itemE.insideId(inventoryE.id());
                stopHolding(actor,itemE);

                if ( inventory.mode == Inventory.Mode.HOPPER ) {
                    itemE.floating();
                }

                // @todo separate this out to something else.
                itemE
                        .scale(1f)
                        .tint(Tint.WHITE)
                        .gridPos(inventoryE.getGridPos())
                        .renderLayer(GameRules.LAYER_ITEM);

                rendererSingleton.sortedDirty = true;
            }
        }
    }

    private void stopHolding(E actor, E item) {
        actor.removeHolding();
        if (actor.hasPlayer())
            switch (item.getItem().type) {
                case "item_dog": E.E().playSound("LD45_dogwhine"); break;
                case "item_wife": E.E().playSound("LD45_mermaid"); break;
                default: E.E().playSound("sfx_pickup");
            }
    }

}
