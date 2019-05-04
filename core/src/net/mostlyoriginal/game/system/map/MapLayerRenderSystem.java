package net.mostlyoriginal.game.system.map;

import com.artemis.Aspect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Invisible;
import net.mostlyoriginal.api.component.graphics.Render;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;
import net.mostlyoriginal.game.component.map.TiledMapSingleton;
import net.mostlyoriginal.game.component.map.TiledMapLayer;

/**
 * @author Daan van Yperen
 */
public class MapLayerRenderSystem extends DeferredEntityProcessingSystem {

    private final SpriteBatch batch;

    private TiledMapSingleton tiledMap;

    private CameraSystem cameraSystem;
    private M<TiledMapLayer> mLayer;

    public MyMapRendererImpl renderer;

    public MapLayerRenderSystem(EntityProcessPrincipal principal, SpriteBatch batch) {
        super(Aspect.all(Pos.class, TiledMapLayer.class, Render.class).exclude(Invisible.class), principal);
        this.batch = batch;
    }


    @Override
    protected void begin() {
        super.begin();
        if (renderer == null) {
            renderer = new MyMapRendererImpl(tiledMap.map);
        }
    }

    @Override
    protected void process(int e) {
        renderer.setView(cameraSystem.camera);
        renderer.renderLayer(mLayer.get(e).layer);
    }
}