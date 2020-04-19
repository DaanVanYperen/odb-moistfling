package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.annotations.All;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.system.box2d.BoxPhysicsSystem;
import net.mostlyoriginal.game.system.common.FluidSystem;
import net.mostlyoriginal.game.system.render.ParticleEffectSystem;

/**
 * @author Daan van Yperen
 */
@All(Player.class)
public class PlayerAnimationSystem extends FluidSystem {

    Vector2 worldOrigin = new Vector2();
    Vector2 vel = new Vector2();
    ParticleEffectSystem particleEffectSystem;

    @Override
    protected void process(E e) {

        float percentage = e.oxygenPercentage();

        String suffix;
        if ( percentage > 100 ) suffix="_thicc";
        else if ( percentage > 50 ) suffix="";
        else if ( percentage > 25 ) suffix="_skinny";
        else suffix="_deflated";

        Body body = e.boxedBody();
        if (Math.abs(body.getAngularVelocity()) / body.getMass() > 0.05 ) {
            e.anim("player_rotate"+suffix);
        } else if ( body.getLinearVelocity().len2() / body.getMass() > 0.5 ) {
            e.anim("player_stretched_out"+suffix);
//            vel.set(0.01f * body.getMass(),0).rotate(e.angleRotation() + 90);
        } else {
            e.anim("player_idle"+suffix);
        }

        if ( e.isTethered() ) {
            e.anim("player_tethered"+suffix);
        }

        if ( percentage <= 0 ) e.anim("astronaut_corpse");
    }
}
