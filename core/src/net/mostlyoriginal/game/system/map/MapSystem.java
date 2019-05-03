package net.mostlyoriginal.game.system.map;

import com.artemis.BaseSystem;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.utils.Array;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

import static com.badlogic.gdx.Input.Keys.G;

/**
 * @author Daan van Yperen
 */
public class MapSystem extends BaseSystem {

    public TiledMap map;
    public int width;
    public int height;
    private Array<TiledMapTileLayer> layers;
    private boolean isSetup;
    public MapProperties properties;
    public int activeLevel = -1;
    private MapSpawnerSystem mapSpawnerSystem;

    @Override
    protected void initialize() {
        map = new TmxMapLoader().load("map" + (GameRules.level) + ".tmx");
        activeLevel = GameRules.level;

        properties = map.getProperties();

        layers = new Array<>();
        for (MapLayer rawLayer : map.getLayers()) {
            layers.add((TiledMapTileLayer) rawLayer);
        }
        width = layers.get(0).getWidth();
        height = layers.get(0).getHeight();
    }

    public MapMask getMask(String property) {
        return new MapMask(height, width, GameRules.CELL_SIZE, GameRules.CELL_SIZE, layers, property);
    }

    /**
     * Spawn map entities.
     */
    protected void setup() {
        for (TiledMapTileLayer layer : layers) {
            for (int ty = 0; ty < height; ty++) {
                for (int tx = 0; tx < width; tx++) {
                    final TiledMapTileLayer.Cell cell = layer.getCell(tx, ty);
                    if (cell != null) {
                        final MapProperties properties = cell.getTile().getProperties();
                        if (properties.containsKey("entity")) {
                            if (mapSpawnerSystem.spawn(tx, ty, properties)) {
                                layer.setCell(tx, ty, null);
                            }
                        }
//                        if (properties.containsKey("invisible")) {
//                            layer.setCell(tx, ty, null);
//                        }
                    }
                }
            }
        }
        mapSpawnerSystem.finalizeSpawns();
    }

    @Override
    protected void processSystem() {
        if (!isSetup) {
            isSetup = true;
            setup();
        }
    }

}