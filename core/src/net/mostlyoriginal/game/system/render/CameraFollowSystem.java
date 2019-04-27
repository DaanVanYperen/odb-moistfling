package net.mostlyoriginal.game.system.render;

import com.artemis.Aspect;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Interpolation;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.render.CameraFocus;
import net.mostlyoriginal.game.system.MyAnimRenderSystem;
import net.mostlyoriginal.game.system.common.FluidSystem;

import static com.badlogic.gdx.Input.Keys.G;

/**
 * @author Daan van Yperen
 */
public class CameraFollowSystem extends FluidSystem {

    CameraSystem cameraSystem;
    private MyAnimRenderSystem myAnimRenderSystem;
    private boolean lockCamera;

    public CameraFollowSystem() {
        super(Aspect.all(Pos.class, CameraFocus.class));
    }

    private int targetX = 0;
    private int sourceX = 0;
    private float cooldown = 0f;

    public float minCameraX() {
        return cameraSystem.camera.position.x - (GameRules.SCREEN_WIDTH / GameRules.CAMERA_ZOOM) * 0.25f;
    }

    @Override
    protected void process(E e) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F9)) lockCamera = !lockCamera;

        if (lockCamera) return;
        float newTargetX = (int) myAnimRenderSystem.roundToPixels(e.posX() + e.boundsCx()) + (GameRules.SCREEN_WIDTH / GameRules.CAMERA_ZOOM);
        if (targetX != newTargetX) {
            sourceX = (int) myAnimRenderSystem.roundToPixels(cameraSystem.camera.position.x);
            targetX = (int) myAnimRenderSystem.roundToPixels(newTargetX);
            cooldown = 0f;
        }
        if (cooldown <= 1F) {
            cooldown += world.delta * 2f;
            if (cooldown > 1f) cooldown = 1f;
            cameraSystem.camera.position.x = myAnimRenderSystem.roundToPixels(Interpolation.pow2Out.apply(sourceX, targetX, cooldown));
        }
        //cameraSystem.camera.position.y = myAnimRenderSystem.roundToPixels(e.posY()) + e.boundsCy();
        cameraSystem.camera.update();

        float maxDistance = 50;
        if (e.posX() < cameraSystem.camera.position.x - maxDistance) {
            cameraSystem.camera.position.x = myAnimRenderSystem.roundToPixels(e.posX() + maxDistance);
            cameraSystem.camera.update();
        }
    }

    public float maxCameraX() {
        return minCameraX() + (GameRules.SCREEN_WIDTH / GameRules.CAMERA_ZOOM);
    }
}

