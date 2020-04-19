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

            float pixelDistance = v2
                    .set(e.getPos().xy)
                    .add(e.getBounds().cx(),e.getBounds().cy(),0)
                    .sub(player.getPos().xy)
                    .sub(player.getBounds().cx(),player.getBounds().cy(),0).len() * 0.8f;


            if ( pixelDistance < 50 )
                pixelDistance=50;

            World box2d = boxPhysicsSystem.box2d;
            DistanceJointDef grappleJointDef = new DistanceJointDef();
            grappleJointDef.bodyA = e.getBoxed().body;
            grappleJointDef.bodyB = player.getBoxed().body;
            grappleJointDef.length = pixelDistance / PPM;
            grappleJointDef.frequencyHz = 0.5f;
            grappleJointDef.dampingRatio = 0.3f;

            grappleJoint = box2d.createJoint(grappleJointDef);
        }
    }
}
