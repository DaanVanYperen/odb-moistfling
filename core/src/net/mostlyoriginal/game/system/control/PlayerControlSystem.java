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

    private static final float PLAYER_WALKING_SPEED = GameRules.DEBUG_ENABLED ? 150f : 60f;
    private static final float PLAYER_SWIMMING_SPEED = GameRules.DEBUG_ENABLED ? 150f : 40f;
    private static final float PLAYER_SUBMERGED_SPEED = GameRules.DEBUG_ENABLED ? 150f : 80f;

    @Override
    protected void process(E e) {
    }

    private void handleMovement(E e) {

        int dx = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT) ? -1 :
                Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) ? 1 : 0;
        int dy = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP) ? 1 :
                Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN) ? -1 : 0;

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
