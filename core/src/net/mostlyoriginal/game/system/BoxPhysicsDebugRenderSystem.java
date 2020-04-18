package net.mostlyoriginal.game.system;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import net.mostlyoriginal.api.system.camera.CameraSystem;

/**
 * @author Daan van Yperen
 */
public class BoxPhysicsDebugRenderSystem extends BaseSystem {

    private Box2DDebugRenderer debugRenderer;
    private BoxPhysicsSystem boxPhysicsSystem;
    private OrthographicCamera camera;
    private CameraSystem cameraSystem;
    private boolean enabled;

    @Override
    protected void initialize() {
        super.initialize();
        debugRenderer = new Box2DDebugRenderer();
        float zoomFactorInverter = 1f / 2f;
        setupViewport((Gdx.graphics.getWidth() * zoomFactorInverter) / boxPhysicsSystem.SCALING, (Gdx.graphics.getHeight() * zoomFactorInverter) / boxPhysicsSystem.SCALING);
    }

    private void setupViewport(float width, float height) {
        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    @Override
    protected void processSystem() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F8)) enabled = !enabled;
        if (enabled) {
            camera.position.x = cameraSystem.camera.position.x / boxPhysicsSystem.SCALING;
            camera.update();
            debugRenderer.render(boxPhysicsSystem.box2d, camera.combined);

        }
    }
}
