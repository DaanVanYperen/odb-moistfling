package net.mostlyoriginal.game.system.action;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.action.ActionBuild;
import net.mostlyoriginal.game.system.MyParticleEffectStrategy;
import net.mostlyoriginal.game.system.future.FutureSpawnUtility;
import net.mostlyoriginal.game.system.render.SlotManager;


/**
 * Build target at current location.
 *
 * @author Daan van Yperen
 */
@All(ActionBuild.class)
public class BuildActionSystem extends FluidIteratingSystem {

    private SlotManager slotManager;

    @Override
    protected void process(E actor) {
        ActionBuild action = actor.getActionBuild();
        if (action.inventory != -1 && action.target != -1 && actor.hasHolding() && !actor.isMoving()) {
            deploy(E.E(action.target), actor.getGridPos(), actor);
        }
        actor.removeActionBuild();

    }

    public void deploy(E item, GridPos gridPos, E actor) {


        E standingOnSlot = slotManager.getSlotAt(gridPos);
        if ( standingOnSlot == null ) return;

        actor.removeHolding();

        int originGridX = gridPos.x;
        int originGridY = gridPos.y;
        int slotOffsetX = standingOnSlot.inventoryX();
        int slotOffsetY = standingOnSlot.inventoryY();

        // spawn machine
        E locked = FutureSpawnUtility.item(getIntendedMachine(item, standingOnSlot), 1, (originGridX - slotOffsetX),
                (originGridY - slotOffsetY), false).locked();
        if ( "extension_point_top".equals(standingOnSlot.getInventory().transform))
            locked.lockedOnTop();
        if ( "extension_point_inside".equals(standingOnSlot.getInventory().transform))
            locked.lockedInside();


                // add spawners on each cell.
        int gx = (originGridX - slotOffsetX);
        int gy = (originGridY - slotOffsetY);
//        E.E().gridPos(
//                gx,
//                gy)
//                .passiveSpawnerItems(itemData.machineProducts);
//

        E.E().particleEffect(MyParticleEffectStrategy.EFFECT_POOF).pos(
                gx * GameRules.CELL_SIZE + 16,
                gy * GameRules.CELL_SIZE + 16);


        occupySlots(gridPos, standingOnSlot, 1,1);
        item.deleteFromWorld();
    }

    private String getIntendedMachine(E item, E standingOnSlot) {
        if ( "extension_point".equals(standingOnSlot.getInventory().transform)) {
            switch (item.getItemMetadata().data.id) {
                case "item_driftwood":
                    return "item_pallet";
                case "item_net":
                    return "item_net_placed";
            }
        }
        if ( "extension_point_top".equals(standingOnSlot.getInventory().transform)) {
            switch (item.getItemMetadata().data.id) {
                case "item_barrel":
                    return "item_planter";
            }
        }
        if ( "extension_point_inside".equals(standingOnSlot.getInventory().transform)) {
            switch (item.getItemMetadata().data.id) {
                case "item_citrus":
                    return "item_citrus_plant_sapling";
                case "item_coconut":
                    return "item_palm_sapling";
            }
        }
        throw new RuntimeException();
    }

    private void occupySlots(GridPos gridPos, E standingOnSlot, int h, int w) {
        int originGridX = gridPos.x;
        int originGridY = gridPos.y;
        int slotOffsetX = standingOnSlot.inventoryX();
        int slotOffsetY = standingOnSlot.inventoryY();
        GridPos tmpSlotPos = new GridPos();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                tmpSlotPos.x = (originGridX - slotOffsetX) + x;
                tmpSlotPos.y = (originGridY - slotOffsetY) + y;
                E slot = slotManager.getSlotAt(tmpSlotPos);
                if (slot != null) {
                    slot.deleteFromWorld();
                }
            }
        }
    }

}
