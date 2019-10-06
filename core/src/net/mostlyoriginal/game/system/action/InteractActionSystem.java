package net.mostlyoriginal.game.system.action;

import com.artemis.E;
import com.artemis.ESubscription;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.annotations.Exclude;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.Item;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.component.Shopper;
import net.mostlyoriginal.game.component.action.ActionInteract;
import net.mostlyoriginal.game.component.inventory.Inside;
import net.mostlyoriginal.game.component.inventory.Inventory;
import net.mostlyoriginal.game.system.control.PickupManager;
import net.mostlyoriginal.game.system.render.SlotManager;


/**
 * Handle user pressing interact button at current location.
 *
 * @author Daan van Yperen
 */
@All({ActionInteract.class})
public class InteractActionSystem extends FluidIteratingSystem {

    //private NightSystem nightSystem;
    private SlotManager slotManager;
    private PickupManager pickupManager;

    @All({Pos.class, Shopper.class})
    private ESubscription shoppers;


    @Override
    protected void process(E actor) {
            final E inventoryE = nearbyInventory(actor);

            if (actor.hasHolding()) {

                if ( E.E(actor.holdingId()).itemMetadataData().edible ) {
                    conderUse(actor);
                } else
                if (!considerTrade(actor)) {
                    considerDrop(actor, inventoryE);
                }
            } else {
                if (!considerTalk(actor)) {
                    considerTake(actor, inventoryE);
                }
            }

        actor.removeActionInteract();
    }

    private void conderUse(E actor) {
        actor.actionUseTarget(actor.holdingId());
    }

    private boolean considerTrade(E actor) {
        for (E shopper : shoppers) {
            if (shopper.gridPosOverlaps(actor.getGridPos())) {
                if (shopper.hasDesire() && pickupManager.isCarrying(actor, shopper.desireDesiredItem())) {
                    actor.actionTradeTarget(shopper.id());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean considerTalk(E actor) {
        for (E shopper : shoppers) {
            if (shopper.gridPosOverlaps(actor.getGridPos())) {
                if (shopper.hasWantsToDiscuss()) {
                    actor.actionTalkTarget(shopper.id());
                    return true;
                }
            }
        }
        return false;
    }

    private void considerDrop(E actor, E inventoryE) {
        if (inventoryE != null && inventoryE.getInventory().acceptsType(E.E(actor.holdingId()).itemType())) {
            if (inventoryE.inventoryMode() == Inventory.Mode.CONSTRUCT) {
                // attempt to construct item at inventory.
                actor.actionBuildTarget(actor.holdingId());
                actor.actionBuildInventory(inventoryE.id());
                return;
            } else {
                // attempt to drop item / put into inventory.
                actor.actionDropTarget(actor.holdingId());
                actor.actionDropInventory(inventoryE.id());
                return;
            }
        }

        actor.actionDropTarget(actor.holdingId());
        actor.actionDropInventory(-1);
    }

    private void considerTake(E actor, E inventoryE) {
        // take from inventory.
        E itemHere = null;
        if (inventoryE != null) {
            final Inventory inventory = inventoryE.getInventory();
            if (inventory != null && !inventory.contents.isEmpty()) {
                // attempt to pickup item here.
                itemHere = E.E(inventory.contents.get(0));
            }
        }

        // pickup from floor.
        if (itemHere == null) {
            Vector3 actorPos = actor.getPos().xy;
            itemHere = closestItem(actorPos.x, actorPos.y, 24 * 24);
        }

        if (itemHere != null) {
            actor.actionPickupTarget(itemHere.id());
        }
    }

    @All({Item.class, GridPos.class})
    @Exclude({Inside.class, Player.class})
    public ESubscription items;

    private E closestItem(float x, float y, float maxDistance) {
        float closestDistance = 0;
        E closest = null;
        for (E item : items) {
            float distance = item.getPos().xy.dst2(x, y, 0);
            if (distance < maxDistance && !item.isLocked() && (closest == null || distance < closestDistance)) {
                closestDistance = distance;
                closest = item;
            }
        }
        return closest;
    }

    private E nearbyInventory(E actor) {
        E inventorytAt = slotManager.getSlotAt(actor.getGridPos());
        if (inventorytAt == null && actor.hasPlayer())
            inventorytAt = slotManager.getSlotAt(actor.getGridPos(), actor.playerDx(), actor.playerDy());
        if (inventorytAt == null) inventorytAt = slotManager.getSlotAt(actor.getGridPos(), 1, 0);
        if (inventorytAt == null) inventorytAt = slotManager.getSlotAt(actor.getGridPos(), 0, 1);
        if (inventorytAt == null) inventorytAt = slotManager.getSlotAt(actor.getGridPos(), -1, 0);
        if (inventorytAt == null) inventorytAt = slotManager.getSlotAt(actor.getGridPos(), 0, -1);
        if (inventorytAt == null) inventorytAt = slotManager.getSlotAt(actor.getGridPos(), 1, 1);
        if (inventorytAt == null) inventorytAt = slotManager.getSlotAt(actor.getGridPos(), -1, 1);
        if (inventorytAt == null) inventorytAt = slotManager.getSlotAt(actor.getGridPos(), -1, -1);
        if (inventorytAt == null) inventorytAt = slotManager.getSlotAt(actor.getGridPos(), 1, -1);
        return inventorytAt;
    }
}
