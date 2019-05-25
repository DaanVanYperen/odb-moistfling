package net.mostlyoriginal.game.system.action;

import com.artemis.E;
import com.artemis.EBag;
import net.mostlyoriginal.game.component.inventory.Inventory;

/**
 * @author Daan van Yperen
 */
public class InventoryUtils {
    private InventoryUtils() {
    }

    public static E getFirstStackOf(Inventory inventory, String itemType) {
        for (E item : new EBag(inventory.contents)) {
            if (item.itemType().equals(itemType)) return item;
        }
        return null;
    }

    public static void removeFromInventory(E item) {
        if (item.hasInside() && item.insideId() != -1) {
            if ( !E.E(item.insideId()).getInventory().contents.contains(item.id()) ) {
                throw new RuntimeException();
            }
            E.E(item.insideId()).getInventory().contents.removeValue(item.id());
        }
    }
}
