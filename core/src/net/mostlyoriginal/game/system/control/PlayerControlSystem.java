package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.annotations.Exclude;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.component.dialog.InDialog;

/**
 * Basic keyboard control system for player.s
 *ww
 * @author Daan van Yperen
 */
@All(Player.class)
@Exclude(InDialog.class)
public class PlayerControlSystem extends FluidIteratingSystem {

    private static final float PLAYER_WALKING_SPEED = 60f;
    private static final float PLAYER_SWIMMING_SPEED = 40f;
    private static final float PLAYER_SUBMERGED_SPEED = 80f;

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

//        if ( dx==0&&dy==0) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
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

        e.playerDx(dx);
        e.playerDy(dy);

        float centerX = e.posX()+e.boundsCx();
        float centerY = e.posY();

        if ( centerX % 8 < 2f && dx == 0 && dy != 0 ) dx = 1;
        if ( centerX % 8 > 4f && dx == 0 && dy != 0) dx = -1;
        if ( centerY % 8 < 2f && dy == 0 && dx != 0) dy = 1;
        if ( centerY % 8 > 4f && dy == 0 && dx != 0) dy = -1;

        Vector2 movementVector = vector2.set(dx, dy).nor();

        e.physics();

        float speed = e.hasSwimming() ? (e.hasSubmerged() ? PLAYER_SUBMERGED_SPEED : PLAYER_SWIMMING_SPEED) : PLAYER_WALKING_SPEED;

        if (movementVector.x != 0) {
            e.getPhysics().vx = movementVector.x * speed * 1.1f;
        }
        if (movementVector.y != 0) {
            e.getPhysics().vy = movementVector.y * speed;
        }
        e.getPhysics().friction = 50;
    }

}
