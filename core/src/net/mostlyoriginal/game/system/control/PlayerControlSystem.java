package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.annotations.Exclude;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.component.dialog.InDialog;
import net.mostlyoriginal.game.system.StaminaSystem;

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

    StaminaSystem staminaSystem;
    private boolean flipperBonus;
    private boolean snorkelBonus;

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

        if ( e.hasDiving() ) {
            e.getDiving().diving -= world.delta;
            if ( e.getDiving().diving <0 ) {
                e.removeDiving();
                //E.E().playSound("water1");
            }
        }

//        if ( dx==0&&dy==0) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
                if ( e.isSwimming() && !e.hasHolding() ) {
                    e.diving();
                   //E.E().playSound("water2");
                }
                e.actionInteract();
            }
//        } else {
//            if ( divingCooldown <= 0 && Gdx.input.isKeyPressed(Input.Keys.SPACE) )
//                divingCooldown=0.2f;
//        }
//
//        if ( divingCooldown > 0 ) {
//            divingCooldown-=world.delta;
//            e.submerged();
//            e.submergedSubmergedAnim("player_dive");
//        } else {
//            e.removeSubmerged();
//        }

        if (e.hasBlinking() ) {
            dx=dy=0;

        }

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

        if ( dx != 0 || dy != 0 ) {
            staminaSystem.drainStamina(flipperBonus ? 0.05f : 0.1f);
        } else {
            if ( e.hasDiving() ) {
                if ( !snorkelBonus ) {
                    staminaSystem.drainStamina(0.22f);
                }
            } else if ( e.isSwimming() ) {
                    staminaSystem.drainStamina(flipperBonus ? 0.01f : 0.02f);
            } else
                staminaSystem.slowRegenStamina(e.isSwimming()?0.2f:0.4f,e.isSwimming()?0.1f:0.2f);
        }

        e.physics();

        float speed = e.hasSwimming() ? (e.hasSubmerged() ? PLAYER_SUBMERGED_SPEED : PLAYER_SWIMMING_SPEED) : PLAYER_WALKING_SPEED;

        // affect movement speed by stamina.
        speed *= Interpolation.linear.apply(1.2f,2f, staminaSystem.getStamina());
        if ( staminaSystem.getStamina() <= 0.05f ) speed *= 0.5f;
        if ( flipperBonus ) speed *= 1.25f;

        if (movementVector.x != 0) {
            e.getPhysics().vx = movementVector.x * speed * 1.1f;
        }
        if (movementVector.y != 0) {
            e.getPhysics().vy = movementVector.y * speed;
        }
        e.getPhysics().friction = 50;
    }

    public void enableFlipperBonus() {
        flipperBonus=true;
    }

    public void enableSnorkelBonus() {
        snorkelBonus = true;
    }
}
