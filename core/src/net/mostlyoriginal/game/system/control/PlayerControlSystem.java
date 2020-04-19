package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.annotations.Exclude;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.system.box2d.BoxPhysicsSystem;

/**
 * Basic keyboard control system for player.s
 * ww
 *
 * @author Daan van Yperen
 */
@All(Player.class)
public class PlayerControlSystem extends FluidIteratingSystem {

    @Override
    protected void process(E e) {
        handleMovement(e);
    }

    Vector2 vel = new Vector2();

    private void handleMovement(E e) {

        Body body = e.boxedBody();

//        vel.set(body.getLinearVelocity());
//        float targetAngle = vel.angleRad() - 90f;
//        body.setTransform(body.getWorldCenter(), targetAngle);
//
        //body.applyLinearImpulse(vel, worldOrigin, true);

//        if (movementVector.x != 0 || movementVector.y != 0) {
//            Body body = e.boxedBody();
//            final Vector2 vel = body.getLinearVelocity();
//            worldOrigin.x = (e.posX() + e.boundsCx()) / BoxPhysicsSystem.PPM;
//            worldOrigin.y = (e.posY() + e.boundsCy()) / BoxPhysicsSystem.PPM;
//            this.vel.x = (movementVector.x * 0.1f) * body.getMass();
//            this.vel.y = (movementVector.y * 0.1f) * body.getMass();
//            body.applyLinearImpulse(this.vel, worldOrigin, true);
//        }
    }
}
