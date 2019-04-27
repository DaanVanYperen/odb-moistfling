package net.mostlyoriginal.game.system.map;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.GridPos;

/**
 * Convert gridpos to pos.
 *
 * @author Daan van Yperen
 */
@All({GridPos.class})
public class GridPosSystem extends FluidIteratingSystem {

    private static final float MOVEMENT_SPEED = 150f;

    @Override
    protected void process(E e) {

        final GridPos gridPos = e.getGridPos();

        float goalX = gridPos.x * GameRules.CELL_SIZE;
        float goalY = gridPos.y * GameRules.CELL_SIZE;

        boolean hasPos = e.hasPos();
        final Vector3 pos = e.pos().getPos().xy;

        if (!hasPos) {
            // initialize instantly.
            pos.x = goalX;
            pos.y = goalY;
        } else {
            if ( gridPos.deriveFromPos ) {
                gridPos.x = (int)((pos.x + e.boundsCx()) / GameRules.CELL_SIZE);
                gridPos.y = (int)((pos.y + e.boundsMiny()+4) / GameRules.CELL_SIZE);
            } else {
                float dx = MathUtils.clamp(goalX - pos.x, -1, 1);
                float dy = MathUtils.clamp(goalY - pos.y, -1, 1);

                pos.x = pos.x + (dx * world.delta * MOVEMENT_SPEED);
                if ( dx > 0 && pos.x > goalX) pos.x = goalX;
                if ( dx < 0 && pos.x < goalX) pos.x = goalX;

                pos.y = pos.y + (dy * world.delta * MOVEMENT_SPEED);
                if ( dx > 0 && pos.y > goalY) pos.y = goalY;
                if ( dx < 0 && pos.y < goalY) pos.y = goalY;


                boolean atTarget = MathUtils.isEqual(pos.x, goalX, 1f) && MathUtils.isEqual(pos.y, goalY, 1f);
                if (atTarget) {
                    pos.x = goalX;
                    pos.y = goalY;
                }
                e.moving(!atTarget);
            }
        }

    }
}
