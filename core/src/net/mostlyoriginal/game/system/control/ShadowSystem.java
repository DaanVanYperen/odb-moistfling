package net.mostlyoriginal.game.system.control;

import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.CastsShadow;
import net.mostlyoriginal.game.component.GridPos;

/**
 * @author Daan van Yperen
 */
@All({CastsShadow.class, Pos.class, GridPos.class})
public class ShadowSystem extends FluidIteratingSystem {

    @Override
    protected void process(E e) {
        CastsShadow castsShadow = e.getCastsShadow();
        if (castsShadow.shadowId == -1) {
            final E shadowEntity = E.E()
                    .anim("shadow")
                    .renderLayer(GameRules.LAYER_SHADOWS - 1)
                    .tint(1f, 1f, 1f, 0.7f);
            castsShadow.shadowId =
                    shadowEntity.id();
        }

        E shadow = E.E(castsShadow.shadowId);
        if ( e.hasTint() ) {
            shadow.tintColor().a = e.tintColor().a * 0.7f;
        }
        shadow
                .posX(e.posX())
                .posY(e.posY() +castsShadow.yOffset);
    }

    private ComponentMapper<CastsShadow> mShadow;

    @Override
    protected void removed(int entityId) {
        // kill indicator as well.
        int shadowId = mShadow.get(entityId).shadowId;
        if (shadowId != -1) {
            E.E(shadowId).deleteFromWorld();
            //mShadow.get(entityId).shadowId=-1;
        }
    }
}
