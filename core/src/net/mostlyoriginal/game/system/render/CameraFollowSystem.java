package net.mostlyoriginal.game.system.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.annotations.All;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import net.mostlyoriginal.api.component.basic.Angle;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.camera.Camera;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.system.box2d.BoxPhysicsDebugRenderSystem;
import net.mostlyoriginal.game.system.common.FluidSystem;

/**
 * @author Daan van Yperen
 */
@All({Pos.class, Camera.class})
public class CameraFollowSystem extends FluidSystem {

    private CameraSystem cameraSystem;
    private BoxPhysicsDebugRenderSystem debugRenderSystem;

    public float roundToPixels(final float val) {
        // since we use camera zoom rounding to integers doesn't work properly.
        return ((int) (val * cameraSystem.zoom)) / (float) cameraSystem.zoom;
    }

    @Override
    protected void process(E e) {
        cameraSystem.camera.position.x =roundToPixels(e.posX() + e.boundsCx());
        cameraSystem.camera.position.y = roundToPixels(e.posY() + e.boundsCy());
        cameraSystem.camera.update();

        debugRenderSystem.camera.position.x = roundToPixels(e.posX() + e.boundsCx());
        debugRenderSystem.camera.position.y =roundToPixels(e.posY()+ e.boundsCy());
    }
}
