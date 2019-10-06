package net.mostlyoriginal.game.system;

import com.artemis.E;
import com.artemis.ESubscription;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.component.ItemMetadata;
import net.mostlyoriginal.game.component.flags.ExtensionPoint;
import net.mostlyoriginal.game.component.flags.LockedInside;
import net.mostlyoriginal.game.component.flags.LockedOnTop;
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
    private MapMask maskOnTop;
    private MapMask maskInside;

    private static final int[] xOffset = {1, -1, 0, 0};
    private static final int[] yOffset = {0, 0, 1, -1};

    @All(Inventory.class)
    ESubscription slots;

    @All(ExtensionPoint.class)
    ESubscription extensionPoints;

    @All(LockedOnTop.class)
    ESubscription lockedOnTop;

    @All(LockedInside.class)
    ESubscription lockedInside;

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
            purgeSlots();
            if ( mask == null ) {
                mask = map.createBlankMask();
                maskOnTop = map.createBlankMask();
                maskInside = map.createBlankMask();
            }
            initOccupiedMask();
        }
    }

    private void initOccupiedMask() {
        mask.clear();
        for (E e : extensionPoints) {
            mask.set(e.getGridPos().x, e.getGridPos().y,true);
        }
        maskOnTop.clear();
        for (E e : lockedOnTop) {
            maskOnTop.set(e.getGridPos().x, e.getGridPos().y,true);
        }
        maskInside.clear();
        for (E e : lockedInside) {
            maskInside.set(e.getGridPos().x, e.getGridPos().y,true);
        }
    }

    private void purgeSlots() {
        for (E slot : slots) {
            slot.deleteFromWorld();
        }
    }

    @Override
    protected void end() {

        // make sure the player can start the raft.
        int ox = 20;
        int oy = 13;
        if (!mask.atGrid(ox, oy, true)) {
            mask.set(ox, oy, true);
            FutureSpawnUtility.slot(ox, oy, Inventory.Mode.CONSTRUCT, "item_driftwood", 0, 0, "extension_point");
        }
        dirty = false;
    }

    @Override
    protected void process(E e) {
        if (dirty) {
            ItemMetadata itemMeta = e.getItemMetadata();
            if ( itemMeta.data.extensionAcceptsAround != null ) {
                for (int i = 0; i < 4; i++) {
                    int ox = e.gridPosX() + xOffset[i];
                    int oy = e.gridPosY() + yOffset[i];
                    if (!mask.atGrid(ox, oy, true)) {
                        mask.set(ox, oy, true);
                        FutureSpawnUtility.slot(ox, oy, Inventory.Mode.CONSTRUCT, itemMeta.data.extensionAcceptsAround, 0, 0, "extension_point");
                    }
                }
            }

            if ( itemMeta.data.extensionAcceptsOnTop != null ) {
                //if (!mask.atGrid( e.gridPosX() , e.gridPosY(), true)) {
//                    mask.set( e.gridPosX() , e.gridPosY(), true);
                if (!maskOnTop.atGrid(e.gridPosX(), e.gridPosY(), true)) {
                    maskOnTop.set(e.gridPosX(), e.gridPosY(), true);
                    FutureSpawnUtility.slot(e.gridPosX(), e.gridPosY(), Inventory.Mode.CONSTRUCT, itemMeta.data.extensionAcceptsOnTop, 0, 0, "extension_point_top");
                }
                //}
            }

            if ( itemMeta.data.extensionAcceptsInside != null ) {
                //if (!mask.atGrid( e.gridPosX() , e.gridPosY(), true)) {
//                    mask.set( e.gridPosX() , e.gridPosY(), true);
                //}
                if (!maskInside.atGrid(e.gridPosX(), e.gridPosY(), true)) {
                    maskInside.set(e.gridPosX(), e.gridPosY(), true);
                    FutureSpawnUtility.slot( e.gridPosX() , e.gridPosY(), Inventory.Mode.CONSTRUCT, itemMeta.data.extensionAcceptsInside, 0, 0, "extension_point_inside");
                }
            }
        }
    }
}

