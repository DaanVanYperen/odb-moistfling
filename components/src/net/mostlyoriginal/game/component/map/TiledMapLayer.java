package net.mostlyoriginal.game.component.map;

import com.artemis.Component;
import com.artemis.annotations.Transient;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * TiledMap tile layer.
 *
 * @author Daan van Yperen
 */
@Transient
public class TiledMapLayer extends Component {
    public TiledMapTileLayer layer;
}
