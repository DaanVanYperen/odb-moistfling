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
import net.mostlyoriginal.game.component.dialog.InDialog;
import net.mostlyoriginal.game.system.BoxPhysicsSystem;

/**
 * Basic keyboard control system for player.s
 *ww
 * @author Daan van Yperen
 */
@All(Player.class)
@Exclude(InDialog.class)
public class PlayerControlSystem extends FluidIteratingSystem {

    private static final float PLAYER_WALKING_SPEED = GameRules.DEBUG_ENABLED ? 150f: 60f;
    private static final float PLAYER_SWIMMING_SPEED = GameRules.DEBUG_ENABLED ? 150f: 40f;
    private static final float PLAYER_SUBMERGED_SPEED = GameRules.DEBUG_ENABLED ? 150f: 80f;

    private boolean flipperBonus;
    private boolean snorkelBonus;
    private Vector2 worldOrigin = new Vector2();
    private Vector2 vel = new Vector2();

    public boolean isFlipperBonus() {
        return flipperBonus;
    }

    public boolean isSnorkelBonus() {
        return snorkelBonus;
    }

    @Override
    protected void process(E e) {
        if (!e.hasMoving()) {
            handleMovement(e);
        }
    }

    float divingCooldown = 0;
    Vector2 vector2 = new Vector2();

    private void handleMovement(E e) {

        int dx = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT) ? -1 :
                Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) ? 1 : 0;
        int dy = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP) ? 1 :
                Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN) ? -1 : 0;

        e.playerDx(dx);
        e.playerDy(dy);

        float centerX = e.posX()+e.boundsCx();
        float centerY = e.posY();

        // half-grid aligning
        if ( centerX % 8 < 2f && dx == 0 && dy != 0 ) dx = 1;
        if ( centerX % 8 > 4f && dx == 0 && dy != 0) dx = -1;
        if ( centerY % 8 < 2f && dy == 0 && dx != 0) dy = 1;
        if ( centerY % 8 > 4f && dy == 0 && dx != 0) dy = -1;

        Vector2 movementVector = vector2.set(dx, dy).nor();

        if (movementVector.x != 0 || movementVector.y != 0) {
            Body body = e.boxedBody();
            final Vector2 vel = body.getLinearVelocity();
            worldOrigin.x = (e.posX() + e.boundsCx()) / BoxPhysicsSystem.PPM;
            worldOrigin.y = (e.posY() + e.boundsCy()) / BoxPhysicsSystem.PPM;
            this.vel.x = (movementVector.x * 0.1f) * body.getMass();
            this.vel.y = (movementVector.y * 0.1f) * body.getMass();
            body.applyLinearImpulse(this.vel, worldOrigin, true);
        }
    }

    public void enableFlipperBonus() {
        flipperBonus=true;
    }

    public void enableSnorkelBonus() {
        snorkelBonus = true;
    }
}
