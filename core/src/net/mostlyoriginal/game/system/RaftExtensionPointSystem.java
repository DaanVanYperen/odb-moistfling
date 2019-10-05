package net.mostlyoriginal.game.system;

import com.artemis.E;
import com.artemis.ESubscription;
import com.artemis.EntitySubscription;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.component.flags.ExtensionPoint;
import net.mostlyoriginal.game.component.inventory.Inventory;
import net.mostlyoriginal.game.component.map.TiledMapSingleton;
import net.mostlyoriginal.game.system.future.FutureSpawnUtility;

/**
 * @author Daan van Yperen
 */
@All(ExtensionPoint.class)
public class RaftExtensionPointSystem extends FluidIteratingSystem {

    private TiledMapSingleton map;

    private boolean dirty = true;
    private MapMask mask;

    private static final int[] xOffset = {1, -1, 0, 0};
    private static final int[] yOffset = {0, 0, 1, -1};

    @All(Inventory.class)
    ESubscription slots;

    @All(ExtensionPoint.class)
    ESubscription extensionPoints;

    @Override
    public void inserted(IntBag entities) {
        dirty = true;
        super.inserted(entities);
    }

    @Override
    public void removed(IntBag entities) {
        dirty = true;
        super.removed(entities);
    }

    @Override
    protected void begin() {
        if (dirty) {
            if ( mask == null ) {
                mask = map.createBlankMask();
            }
            purgeSlots();
            initOccupiedMask();
        }
    }

    private void initOccupiedMask() {
        mask.clear();
        for (E e : extensionPoints) {
            mask.set(e.getGridPos().x, e.getGridPos().y,true);
        }
    }

    private void purgeSlots() {
        for (E slot : slots) {
            slot.deleteFromWorld();
        }
    }

    @Override
    protected void end() {
        dirty = false;
    }

    @Override
    protected void process(E e) {
        if (dirty) {
            for (int i = 0; i < 4; i++) {
                int ox = e.gridPosX() + xOffset[i];
                int oy = e.gridPosY() + yOffset[i];
                if (!mask.atGrid(ox,oy,true)) {
                    mask.set(ox,oy,true);
                    FutureSpawnUtility.slot(ox, oy, Inventory.Mode.CONSTRUCT, "item_driftwood,item_net", 0, 0, "extension_point");
                }
            }
        }
    }
}

