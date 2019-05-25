package net.mostlyoriginal.game.system.render;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.inventory.Inventory;

/**
 * @author Daan van Yperen
 */
@All(Inventory.class)
public class InventoryDebugSystem extends FluidIteratingSystem {

    ShapeRenderer shapeRenderer = new ShapeRenderer();
    CameraSystem cameraSystem;

    public InventoryDebugSystem() {
    }

    @Override
    protected void begin() {
        shapeRenderer.setProjectionMatrix(cameraSystem.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
    }

    @Override
    protected void end() {
        shapeRenderer.end();
    }

    @Override
    protected void process(E e) {
        shapeRenderer.setColor(e.inventoryIsNotEmpty() ? Color.YELLOW : Color.WHITE);
        shapeRenderer.circle(e.posX() + e.boundsCx(), e.posY() + e.boundsCy(), 16, 12);
    }
}
