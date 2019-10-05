package net.mostlyoriginal.game.system;

import com.artemis.BaseSystem;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.component.inventory.Inventory;
import net.mostlyoriginal.game.component.map.TiledMapSingleton;
import net.mostlyoriginal.game.system.future.FutureSpawnUtility;

/**
 * @author Daan van Yperen
 */
public class RaftExtensionPointSystem extends BaseSystem {

    private TiledMapSingleton map;

    private boolean initialized;
    private MapMask mask;

    private static final int[] xOffset = {1,-1, 0, 0};
    private static final int[] yOffset = {0, 0, 1,-1};


    @Override
    protected void processSystem() {
        if (!initialized) {
            initialized = true;
            mask = map.createMask("extensionpoint");
            mask.refresh();

            for (int x = 2; x < mask.width - 2; x++) {
                for (int y = 2; y < mask.height - 2; y++) {
                    if (mask.atGrid(x, y, false)) {
                        for (int i = 0; i < 4; i++) {
                            int ox           = x + xOffset[i];
                            int oy = y + yOffset[i];
                            if (!mask.atGrid(ox, oy, false)) {
                                FutureSpawnUtility.slot(ox,oy, Inventory.Mode.CONSTRUCT, "item_driftwood",0,0, "item_pallet");
                            }
                        }
                    }
                }
            }
        }
    }
}

