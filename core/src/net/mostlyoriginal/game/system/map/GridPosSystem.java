package net.mostlyoriginal.game.system.map;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.Vector3;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.GridPos;

/**
 * Convert gridpos to pos.
 *
 * @author Daan van Yperen
 */
@All({Pos.class, GridPos.class})
public class GridPosSystem extends FluidIteratingSystem {
    @Override
    protected void process(E e) {
        final GridPos gridPos = e.getGridPos();
        final Vector3 pos = e.getPos().xy;
        pos.x = gridPos.x * GameRules.CELL_SIZE;
        pos.y = gridPos.y * GameRules.CELL_SIZE;
    }
}
