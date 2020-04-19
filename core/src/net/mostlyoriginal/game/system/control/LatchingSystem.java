package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.component.interact.Tapped;
import net.mostlyoriginal.game.system.box2d.BoxPhysicsSystem;
import net.mostlyoriginal.game.system.common.FluidSystem;

import static net.mostlyoriginal.game.system.box2d.BoxPhysicsSystem.PPM;

/**
 * @author Daan van Yperen
 */
@All(Tapped.class)
public class LatchingSystem extends FluidSystem {

    private E player;
    private BoxPhysicsSystem boxPhysicsSystem;
    private Joint grappleJoint;

    @Override
    protected void begin() {
        player = E.withTag("player");
    }

    @Override
    protected void end() {
        if (grappleJoint != null && subscription.getEntities().isEmpty()) {
            World box2d = boxPhysicsSystem.box2d;
            box2d.destroyJoint(grappleJoint);
            grappleJoint=null;
        }
    }

    Vector3 v2 = new Vector3();

    @Override
    protected void process(E e) {
        if (grappleJoint == null && e.hasBoxed() && player.hasBoxed()) {

            float pixelDistance = v2.set(e.getPos().xy).sub(player.getPos().xy).len() * 0.75f;

            World box2d = boxPhysicsSystem.box2d;
            DistanceJointDef grappleJointDef = new DistanceJointDef();
            grappleJointDef.bodyA = e.getBoxed().body;
            grappleJointDef.bodyB = player.getBoxed().body;
            grappleJointDef.length = pixelDistance / PPM;
            grappleJointDef.frequencyHz = 0.8f;
            grappleJointDef.dampingRatio = 1f;

            grappleJoint = box2d.createJoint(grappleJointDef);
        }
    }
}
