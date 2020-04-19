package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.system.box2d.BoxPhysicsSystem;
import net.mostlyoriginal.game.system.common.FluidSystem;

/**
 * @author Daan van Yperen
 */
@All(Player.class)
public class PlayerAnimationSystem extends FluidSystem {

    Vector2 worldOrigin = new Vector2();
    Vector2 vel = new Vector2();

    @Override
    protected void process(E e) {

        Body body = e.boxedBody();
        if (Math.abs(body.getAngularVelocity()) / body.getMass() > 0.1 ) {
            e.anim("player_rotate");
        } else if ( body.getLinearVelocity().len2() / body.getMass() > 0.5 ) {
            e.anim("player_stretched_out");
//            vel.set(0.01f * body.getMass(),0).rotate(e.angleRotation() + 90);
        } else {
            e.anim("player_idle");
        }

        if ( e.isTethered() ) {
            e.anim("player_tethered");
        }
    }
}
