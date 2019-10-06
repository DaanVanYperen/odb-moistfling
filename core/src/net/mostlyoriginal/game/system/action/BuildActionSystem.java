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
                case "item_tiki1":
                    return "item_tiki1_placed";
                case "item_tiki2":
                    return "item_tiki2_placed";
                case "item_tiki3":
                    return "item_tiki3_placed";
                case "item_lampion":
                    return "item_lampion_placed";
                case "item_dog":
                    return "item_dog_placed";
                case "item_skull":
                    return "item_skull_placed";
                case "item_ducky":
                    return "item_ducky_placed";
                case "item_flamingo":
                    return "item_flamingo_placed";
                case "item_chest":
                    return "item_chest_placed";
                case "item_starfish":
                    return "item_starfish_placed";
                case "item_seashell":
                    return "item_seashell_placed";
                case "item_wife":
                    return "item_wife_placed";
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
