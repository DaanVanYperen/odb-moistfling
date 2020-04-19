package net.mostlyoriginal.game.system;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.Dripping;

/**
 * @author Daan van Yperen
 */
@All({Dripping.class, Pos.class})
public class DrippingSystem extends FluidIteratingSystem {

    @Override
    protected void process(E e) {
        final Dripping dripping = e.getDripping();
        dripping.age += world.delta;
        dripping.duration -= world.delta;
        while ( dripping.age > 0.15f ) {
            dripping.age-= 0.15f;
            E.E().particleEffect(MyParticleEffectStrategy.EFFECT_DROPLET).pos(
                    MathUtils.random(e.posX() + e.boundsMinx(),
                    e.posX() + e.boundsMaxx() +4),
                    MathUtils.random(e.posY() + e.boundsMiny(),
                            e.posY() + e.boundsMaxy()));
        }
        if (dripping.duration <= 0) {
            e.removeDripping();
        }
    }
}
