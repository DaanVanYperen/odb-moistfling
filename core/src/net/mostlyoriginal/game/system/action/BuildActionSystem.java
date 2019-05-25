package net.mostlyoriginal.game.system.action;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.ItemData;
import net.mostlyoriginal.game.component.action.ActionBuild;
import net.mostlyoriginal.game.system.MyParticleEffectStrategy;
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
            actor.removeHolding();
            deploy(E.E(action.target), actor.getGridPos());
        }
        actor.removeActionBuild();

    }

    public void deploy(E item, GridPos gridPos) {


        E standingOnSlot = slotManager.getSlotAt(gridPos);

        ItemData itemData = item.getItemMetadata().data;

        int originGridX = gridPos.x;
        int originGridY = gridPos.y;
        int slotOffsetX = standingOnSlot.inventoryX();
        int slotOffsetY = standingOnSlot.inventoryY();

        // spawn machine
        E.E().gridPos(
                (originGridX - slotOffsetX),
                (originGridY - slotOffsetY))
                .anim(itemData.machine).renderLayer(GameRules.LAYER_MACHINES);

        // add spawners on each cell.
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                int gx = (originGridX - slotOffsetX) + x;
                int gy = (originGridY - slotOffsetY) + y;
                E.E().gridPos(
                        gx,
                        gy)
                        .passiveSpawnerItems(itemData.machineProducts);

                E.E().particleEffect(MyParticleEffectStrategy.EFFECT_POOF).pos(
                        gx * GameRules.CELL_SIZE + 16,
                        gy * GameRules.CELL_SIZE + 16);

            }
        }

        occupySlots(gridPos, standingOnSlot);
        item.deleteFromWorld();
    }

    private void occupySlots(GridPos gridPos, E standingOnSlot) {
        int originGridX = gridPos.x;
        int originGridY = gridPos.y;
        int slotOffsetX = standingOnSlot.inventoryX();
        int slotOffsetY = standingOnSlot.inventoryY();
        GridPos tmpSlotPos = new GridPos();
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
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
