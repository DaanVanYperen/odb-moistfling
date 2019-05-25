package net.mostlyoriginal.game.system.map;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.maps.MapProperties;
import net.mostlyoriginal.game.EntityType;
import net.mostlyoriginal.game.component.inventory.Inventory;
import net.mostlyoriginal.game.component.map.MapEntityMarker;
import net.mostlyoriginal.game.system.future.FutureSpawnUtility;

/**
 * Converts between map spawns and future spawns.
 *
 * @author Daan van Yperen
 */
@All(MapEntityMarker.class)
public class MapEntitySpawnerSystem extends FluidIteratingSystem {

    @Override
    protected void process(E e) {
        MapEntityMarker marker = e.getMapEntityMarker();
        spawn(marker.mapX, marker.mapY, marker.properties);
        e.deleteFromWorld();
    }

    public boolean spawn(int gridX, int gridY, MapProperties properties) {

        String entity = (String) properties.get("entity");
        String type = (String) properties.get("type");
        if ("item".equals(entity)) {
            int stackSize = properties.containsKey("count") ? (int) properties.get("count") : 1;
            if (type != null && !"".equals(type)) {
                FutureSpawnUtility.item(type, stackSize, gridX, gridY);
            }
            return true;
        } else if ("slot".equals(entity)) {
            FutureSpawnUtility.slot(gridX, gridY, Inventory.Mode.valueOf(((String) properties.get("mode")).toUpperCase()), properties.get("accepts"), (int) properties.get("x"), (int) properties.get("y"));
            return false;
        } else if ("player".equals(entity)) {
            FutureSpawnUtility.of(EntityType.PLAYER, gridX, gridY);
            return true;
        } else if ("machine".equals(entity)) {
            FutureSpawnUtility.of(EntityType.ALTAR, gridX, gridY);
            return true;
        } else if ("shopperspawner".equals(entity)) {
            FutureSpawnUtility.of(EntityType.SHOPPER_SPAWNER, gridX, gridY);
            return true;
        } else if ("window".equals(entity)) {
            FutureSpawnUtility.of(EntityType.WINDOW, gridX, gridY);
            return false;
        } else if ("door".equals(entity)) {
            FutureSpawnUtility.of(EntityType.DOOR, gridX, gridY);
            return false;
        }

        return false;
    }
}
