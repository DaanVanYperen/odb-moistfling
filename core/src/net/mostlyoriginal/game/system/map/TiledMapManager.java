package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.utils.Array;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Render;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.component.map.TiledMapSingleton;
import net.mostlyoriginal.game.component.map.MapEntityMarker;
import net.mostlyoriginal.game.component.map.TiledMapLayer;

/**
 * Loads tiled map and craetes MapEntityMarker for all tiles with 'entity' property.
 *
 * Supported cell properties:
 * Entity (String): any value
 * Invisible: remove tile from map. Useful in combination with Entity.
 *
 * @author Daan van Yperen
 */
public class TiledMapManager extends BaseSystem {

    private static final String ENTITY_PROPERTY_KEY = "entity";
    private static final String LAYER_PROPERTY_KEY = "layer";
    private static final String INVISIBLE = "invisible";
    private final String filename;

    private M<MapEntityMarker> cMapEntityMarker;
    private M<TiledMapLayer> cMapLayer;
    private M<Render> cRender;
    private M<Pos> cPos;

    // @Todo
    @Deprecated
    public com.badlogic.gdx.maps.tiled.TiledMap map;

    // Injected singleton
    private TiledMapSingleton tiledMap;

    // @Todo
    @Deprecated
    public MapMask getMask(String name) {
        return tiledMap.createMask(name);
    }

    public TiledMapManager(String filename) {
        this.filename = filename;
    }

    @Override
    protected void processSystem() {
    }

    @Override
    protected void initialize() {
        super.initialize();
        load(filename);
    }

    protected void load(String filename) {
        tiledMap.map = new TmxMapLoader().load(filename);
        this.map = tiledMap.map;
        tiledMap.properties = tiledMap.map.getProperties();
        tiledMap.tiledLayers = new Array<>();
        for (MapLayer rawLayer : tiledMap.map.getLayers()) {
            TiledMapTileLayer layer = (TiledMapTileLayer) rawLayer;
            tiledMap.tiledLayers.add(layer);
            createLayerEntity(layer);
        }
        final TiledMapTileLayer firstLayer = tiledMap.tiledLayers.get(0);
        tiledMap.width = firstLayer.getWidth();
        tiledMap.height = firstLayer.getHeight();
        tiledMap.tileWidth = (int) firstLayer.getTileWidth();
        tiledMap.tileHeight = (int) firstLayer.getTileHeight();

        createMapMarkers(tiledMap);
    }

    private void createLayerEntity(TiledMapTileLayer layer) {
        Entity layerEntity = world.createEntity();
        cPos.create(layerEntity);
        cMapLayer.create(layerEntity).layer = layer;
        cRender.create(layerEntity).layer = (int)layer.getProperties().get(LAYER_PROPERTY_KEY);
    }

    private void createMapMarkers(TiledMapSingleton m) {
        for (TiledMapTileLayer layer : m.tiledLayers) {
            for (int ty = 0; ty < m.height; ty++) {
                for (int tx = 0; tx < m.width; tx++) {
                    final TiledMapTileLayer.Cell cell = layer.getCell(tx, ty);
                    if (cell != null) {
                        final MapProperties properties = cell.getTile().getProperties();
                        if (properties.containsKey(ENTITY_PROPERTY_KEY)) {
                            MapEntityMarker mapEntityMarker = cMapEntityMarker.create(world.createEntity());
                            mapEntityMarker.mapX = tx;
                            mapEntityMarker.mapY = ty;
                            mapEntityMarker.properties = properties;
                            if ( properties.containsKey(INVISIBLE)) {
                                cell.setTile(null);
                            }
                        }

                    }
                }
            }
        }
    }
}