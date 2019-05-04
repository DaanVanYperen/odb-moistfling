package net.mostlyoriginal.game.component.map;

import com.artemis.Component;
import com.artemis.annotations.Transient;
import com.badlogic.gdx.maps.MapProperties;

/**
 * Marks an entity as supplied on the map.
 *
 * @author Daan van Yperen
 */
@Transient
public class MapEntityMarker extends Component {
    public int mapX;
    public int mapY;
    public MapProperties properties;
}
