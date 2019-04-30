package net.mostlyoriginal.game.system.control;

import com.artemis.BaseSystem;
import com.artemis.E;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.ItemData;
import net.mostlyoriginal.game.system.ItemRepository;
import net.mostlyoriginal.game.system.ParticleSystem;
import net.mostlyoriginal.game.system.SlotHighlightingSystem;

/**
 * @author Daan van Yperen
 */
public class DeploySystem extends BaseSystem {

    ItemRepository itemRepository;
    SlotHighlightingSystem slotHighlightingSystem;
    private ParticleSystem particleSystem;

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
                int gx = (originGridX - slotOffsetX) + x;
                int gy = (originGridY - slotOffsetY) + y;
                E.E().gridPos(
                        gx,
                        gy)
                        .passiveSpawnerItems(itemData.machineProducts);

                particleSystem.poof(
                        gx * GameRules.CELL_SIZE + 16,
                        gy * GameRules.CELL_SIZE + 16, 40, 40, ParticleSystem.COLOR_WHITE_TRANSPARENT);

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
