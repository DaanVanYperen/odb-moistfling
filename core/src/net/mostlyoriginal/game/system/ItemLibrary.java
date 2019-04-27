package net.mostlyoriginal.game.system;

import net.mostlyoriginal.game.component.ItemData;
import net.mostlyoriginal.game.component.SpriteData;

import java.io.Serializable;

/**
 * @author Daan van Yperen
 */
public class ItemLibrary implements Serializable {
    public net.mostlyoriginal.game.component.ItemData[] items;

    public ItemLibrary() {
    }

    /**
     * Return concept, or <code>null</code> if empty.
     */
    public ItemData getById(String id) {
        for (ItemData item : items) {
            if (item.id != null && item.id.equals(id)) return item;
        }
        return null;
    }
}
