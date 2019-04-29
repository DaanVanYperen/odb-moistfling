package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.Slot;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.Lifter;
import net.mostlyoriginal.game.manager.ItemRepository;
import net.mostlyoriginal.game.system.SlotHighlightingSystem;
import net.mostlyoriginal.game.system.map.MapSpawnerSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;


/**
 * @author Daan van Yperen
 */
@All({Lifter.class, Pos.class})
public class PickupSystem extends FluidIteratingSystem {

    private static final int CARRIED_OBJECT_PLAYER_LIFTING_HEIGHT = 32;
    private static final int CARRIED_OBJECT_SHOPPER_LIFTING_HEIGHT = 4;

    RenderBatchingSystem renderBatchingSystem;
    PickupManager pickupManager;
    private ItemRepository itemRepository;
    private MapSpawnerSystem mapSpawnerSystem;

    private GameScreenAssetSystem gameScreenAssetSystem;
    private SlotHighlightingSystem slotHighlightingSystem;

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

    Tint carriedItemTint = new Tint(0.8f, 0.8f, 1f, 0.8f);

    private void followCarrier(E actor) {
        if (actor.hasLifting() && actor.getLifting().id != -1) {
            E lifting = E.E(actor.getLifting().id);
            if (actor.hasShopper()) {
                lifting.scale(1f);
                lifting.posX(actor.getPos().xy.x + 8);
                lifting.posY(actor.getPos().xy.y + CARRIED_OBJECT_SHOPPER_LIFTING_HEIGHT - 8);
            } else {
                lifting.scale(1f);
                lifting.tint(Tint.WHITE);
                lifting.posX(actor.getPos().xy.x);
                lifting.posY(actor.getPos().xy.y + CARRIED_OBJECT_PLAYER_LIFTING_HEIGHT);
            }
        }
    }

    private void attemptDrop(E actor) {
        E item = E.E(actor.liftingId());
        if (!actor.isMoving()) {
            E slot = bestDropSlot(actor);

            if (slot != null && slotHighlightingSystem.acceptsItemType(slot, item.itemType())) {
                if (slot.slotMode() == Slot.Mode.STORE) {
                    storeItemHere(actor, item, slot);
                }
                if (slot.slotMode() == Slot.Mode.EXPAND) {
                    deployItemHere(actor, item);
                }
            } else actor.lifterAttemptLifting(true); // failed, continue lifting.
        } else actor.lifterAttemptLifting(true); // failed, continue lifting.
    }

    private E bestDropSlot(E actor) {
        E slotAt = slotHighlightingSystem.getSlotAt(actor.getGridPos());
        if (slotAt == null) slotAt = slotHighlightingSystem.getSlotAt(actor.getGridPos(), 1, 0);
        if (slotAt == null) slotAt = slotHighlightingSystem.getSlotAt(actor.getGridPos(), 0, 1);
        if (slotAt == null) slotAt = slotHighlightingSystem.getSlotAt(actor.getGridPos(), -1, 0);
        if (slotAt == null) slotAt = slotHighlightingSystem.getSlotAt(actor.getGridPos(), 0, -1);
        return slotAt;
    }

    DeploySystem deploySystem;

    private void deployItemHere(E actor, E item) {
        deploySystem.deploy(item, actor.getGridPos());
        item.deleteFromWorld();
        actor.removeLifting();
    }

    private void storeItemHere(E actor, E item, E slot) {
        E itemOnFloor = pickupManager.getOverlapping(slot);

        // can't drop the wrong thing on a stack and can't swap a stack.
        boolean matchesFloorItem = itemOnFloor != null && itemOnFloor.itemType().equals(item.itemType());
        if (itemOnFloor != null && !matchesFloorItem && itemOnFloor.getItem().count > 1) {
            return;
        }

        actor.removeLifting();
        item.scale(1f);
        item.tint(Tint.WHITE);
//        item.castsShadow();
        item.gridPos(slot.getGridPos()).removeLifted().renderLayer(GameRules.LAYER_ITEM);
        renderBatchingSystem.sortedDirty = true;

        if (itemOnFloor != null) {
            if (matchesFloorItem) {
                // merge items if identical.
                itemOnFloor.getItem().count++;
                item.deleteFromWorld();
                if (actor.hasPlayer())
                    gameScreenAssetSystem.playSfx("sfx_putdown");
            } else {
                attemptPickup(actor, itemOnFloor);
                actor.lifterAttemptLifting(true); // we swapped something, ontinue lifting.
            }
        } else {
            if (actor.hasPlayer())
                gameScreenAssetSystem.playSfx("sfx_putdown");
        }
    }

    public void forceDropAll(E actor) {
        E item = E.E(actor.liftingId());
        actor.removeLifting();
        item.scale(1f);
        item.tint(Tint.WHITE);
//        item.castsShadow();
        item.gridPos(actor.getGridPos()).removeLifted().renderLayer(GameRules.LAYER_ITEM);
        renderBatchingSystem.sortedDirty = true;
    }

    private void attemptPickup(E actor) {
        attemptPickup(actor, bestItemToPickup(actor));
    }

    private E bestItemToPickup(E actor) {
        E item = pickupManager.getOverlapping(actor);
        if (item == null) item = pickupManager.getOverlapping(actor, 1, 0);
        if (item == null) item = pickupManager.getOverlapping(actor, 0, 1);
        if (item == null) item = pickupManager.getOverlapping(actor, -1, 0);
        if (item == null) item = pickupManager.getOverlapping(actor, 0, -1);
        return item;
    }

    public void attemptPickup(E actor, E item) {
        if (item != null) {
            if (item.itemCount() > 1 && !actor.hasShopper()) { // shoppers grab the whole stack.
                // take from stack.
                item.getItem().count--;
                E clonedItem = mapSpawnerSystem.spawnItem(0, 0, item.itemType())
                        .removeGridPos()
                        .removeFloating()
                        //.removeCastsShadow()
                        .lifted()
                        .renderLayer(GameRules.LAYER_ITEM_CARRIED);
                actor.liftingId(clonedItem.id());
                actor.lifterAttemptLifting(true);

            } else {
                // pick up final item.
                actor.liftingId(item.id());
                actor.lifterAttemptLifting(true);
                item
                        .removeGridPos()
                        .removeFloating()
                        //.removeCastsShadow()
                        .lifted()
                        .renderLayer(GameRules.LAYER_ITEM_CARRIED);
                renderBatchingSystem.sortedDirty = true;
            }
            actor.getLifter().itemsLifted++;
            if (actor.hasPlayer())
                gameScreenAssetSystem.playSfx("sfx_pickup");
        }  else actor.lifterAttemptLifting(false);
    }
}
