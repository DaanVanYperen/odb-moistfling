package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.annotations.All;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.component.interact.Tapped;
import net.mostlyoriginal.game.system.box2d.BoxDestructionListener;
import net.mostlyoriginal.game.system.box2d.BoxPhysicsSystem;
import net.mostlyoriginal.game.system.common.FluidSystem;

import static net.mostlyoriginal.game.system.box2d.BoxPhysicsSystem.PPM;

/**
 * @author Daan van Yperen
 */
@All(Tapped.class)
public class LatchingSystem extends FluidSystem implements BoxDestructionListener {

    private E player;
    private BoxPhysicsSystem boxPhysicsSystem;
    private Joint grappleJoint;
    private E latchedTarget;

    @Override
    protected void initialize() {
        super.initialize();
        boxPhysicsSystem.register(this);
    }

    @Override
    protected void begin() {
        player = E.withTag("player");
    }

    @Override
    protected void end() {
        if (grappleJoint != null && subscription.getEntities().isEmpty() && Gdx.input.isTouched()) {
            killTheLatch();
        }

        player.tethered(grappleJoint != null);
    }

    private void killTheLatch() {
        if ( latchedTarget != null) {
            latchedTarget.removeBeamed();
            latchedTarget=null;
        }
        if (grappleJoint != null) {
            World box2d = boxPhysicsSystem.box2d;
            box2d.destroyJoint(grappleJoint);
            grappleJoint = null;
        }
    }

    Vector3 v2 = new Vector3();

    @Override
    protected void process(E e) {
        if (e.hasBoxed() && player.hasBoxed() && !player.isDead()) {

            killTheLatch();
            latchedTarget = e;
            latchedTarget.beamed();

            float pixelDistance = v2
                    .set(e.getPos().xy)
                    .add(e.getBounds().cx(), e.getBounds().cy(), 0)
                    .sub(player.getPos().xy)
                    .sub(player.getBounds().cx(), player.getBounds().cy(), 0).len() * 0.25F;


            if (pixelDistance < 50)
                pixelDistance = 50;

            if (e.hasPickup()) {
                pixelDistance = 0;
            }

            World box2d = boxPhysicsSystem.box2d;
            DistanceJointDef grappleJointDef = new DistanceJointDef();
            grappleJointDef.localAnchorB.x = -18 / PPM;
            grappleJointDef.localAnchorB.y = 20 / PPM;
            grappleJointDef.bodyA = e.getBoxed().body;
            grappleJointDef.bodyB = player.getBoxed().body;
            grappleJointDef.length = pixelDistance / PPM;
            grappleJointDef.frequencyHz = 0.15f;
            grappleJointDef.dampingRatio = 0.3f;

            //if (e.hasPickup()) {
                grappleJointDef.collideConnected = true;
            //}

            grappleJoint = box2d.createJoint(grappleJointDef);
        }
    }

    @Override
    public void beforeDestroy(Body body) {
    }

    @Override
    public void beforeDestroy(Joint joint) {
        if (grappleJoint == joint) {
            grappleJoint = null;
        }
    }
}
