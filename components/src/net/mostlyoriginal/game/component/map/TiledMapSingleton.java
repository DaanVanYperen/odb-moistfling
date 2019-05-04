package net.mostlyoriginal.game.component.map;

import com.artemis.Component;
import com.artemis.annotations.Transient;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import net.mostlyoriginal.api.Singleton;
import net.mostlyoriginal.api.utils.MapMask;

/**
 * @author Daan van Yperen
 */
@Singleton
@Transient
public class TiledMapSingleton extends Component {
    public com.badlogic.gdx.maps.tiled.TiledMap map;
    public int width;
    public int height;
    public int tileWidth;
    public int tileHeight;
    public Array<TiledMapTileLayer> tiledLayers;
    public MapProperties properties;

    public MapMask createMask(String property) {
        return new MapMask(height, width, tileWidth, tileHeight, tiledLayers, property);
    }
}
