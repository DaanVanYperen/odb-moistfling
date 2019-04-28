package net.mostlyoriginal.game.system.control;

import com.artemis.BaseSystem;
import com.artemis.E;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.ItemData;
import net.mostlyoriginal.game.manager.ItemRepository;
import net.mostlyoriginal.game.system.SlotHighlightingSystem;

/**
 * @author Daan van Yperen
 */
public class DeploySystem extends BaseSystem {

    ItemRepository itemRepository;
    SlotHighlightingSystem slotHighlightingSystem;

    @Override
    protected void processSystem() {

    }

    public void deploy(E item, GridPos gridPos) {
        E standingOnSlot = slotHighlightingSystem.getSlotAt(gridPos);
        ItemData itemData = itemRepository.get(item.itemType());


        int originGridX = gridPos.x;
        int originGridY = gridPos.y;
        int slotOffsetX = standingOnSlot.slotX();
        int slotOffsetY = standingOnSlot.slotY();

        // spawn machine
        E.E().gridPos(
                (originGridX - slotOffsetX),
                (originGridY - slotOffsetY))
                .anim(itemData.machine).renderLayer(GameRules.LAYER_MACHINES);

        // add spawners on each cell.
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                E.E().gridPos(
                        (originGridX - slotOffsetX) + x,
                        (originGridY - slotOffsetY) + y)
                        .passiveSpawnerItems(itemData.machineProducts);
            }
        }

        removeUsedSlots(gridPos, standingOnSlot);
    }

    private void removeUsedSlots(GridPos gridPos, E standingOnSlot) {
        int originGridX = gridPos.x;
        int originGridY = gridPos.y;
        int slotOffsetX = standingOnSlot.slotX();
        int slotOffsetY = standingOnSlot.slotY();
        GridPos tmpSlotPos = new GridPos();
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {


                tmpSlotPos.x = (originGridX - slotOffsetX) + x;
                tmpSlotPos.y = (originGridY - slotOffsetY) + y;

                E slot = slotHighlightingSystem.getSlotAt(tmpSlotPos);
                if (slot != null) {
                    slot.deleteFromWorld();
                }
            }
        }
    }
}
