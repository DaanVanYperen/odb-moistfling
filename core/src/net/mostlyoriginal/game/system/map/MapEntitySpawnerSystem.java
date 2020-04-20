package net.mostlyoriginal.game.system.map;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.maps.MapProperties;
import net.mostlyoriginal.game.EntityType;
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
        String type = (String) properties.get("anim");
        if ("player".equals(entity)) {
            FutureSpawnUtility.of(EntityType.PLAYER, gridX, gridY);
            return true;
        } else if ("debris".equals(entity)) {
            FutureSpawnUtility.of(EntityType.DEBRIS, gridX, gridY).futureEntitySubType(type);
            return true;
        } else if ("pickup".equals(entity)) {
            FutureSpawnUtility.of(EntityType.PICKUP, gridX, gridY).futureEntitySubType(type);
            return true;
        } else if ("exit".equals(entity)) {
            FutureSpawnUtility.of(EntityType.EXIT, gridX, gridY).futureEntitySubType(type);
            return true;
        } else if ("blinker".equals(entity)) {
            FutureSpawnUtility.of(EntityType.BLINKER, gridX, gridY).futureEntitySubType(type);
            return true;
        } else if ("tutorial".equals(entity)) {
            FutureSpawnUtility.of(EntityType.TUTORIAL, gridX, gridY).futureEntitySubType(type);
            return true;
        } else if ("none".equals(entity)) {
            return false;
        }

        return false;
    }
}
