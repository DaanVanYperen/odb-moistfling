package net.mostlyoriginal.game.system.map;

import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.CastsShadow;
import net.mostlyoriginal.game.component.Floating;
import net.mostlyoriginal.game.component.GridPos;

/**
 * Convert gridpos to pos.
 *
 * @author Daan van Yperen
 */
@All({GridPos.class, Floating.class})
public class GridPosFloatSystem extends FluidIteratingSystem {

    private static final float MOVEMENT_SPEED = 150f;

    @Override
    protected void process(E e) {
        Floating floating = e.getFloating();
        floating.age += world.delta;
        float goalY = e.getGridPos().y * GameRules.CELL_SIZE;
        float intensity = MathUtils.clamp(floating.age * 2f, 0f, 8f); // slowly ramp it up so things don't just jump into the air.
        float hoverHeight = MathUtils.clamp(floating.age * 4f, 0f, 12f); // slowly ramp it up so things don't just jump into the air.
        e.posY(goalY + hoverHeight + (float) Math.sin(floating.age) * intensity);

        if ( e.hasCastsShadow() ) {
            e.castsShadowYOffset((int)(goalY-e.posY()+0.5f));
        }
    }

    ComponentMapper<CastsShadow> mCastsShadow;

    @Override
    protected void removed(int entityId) {
        CastsShadow castsShadow = mCastsShadow.get(entityId);
        if ( castsShadow != null ) {
            castsShadow.yOffset = 0;
        }
    }
}
