package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.EntitySubscription;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.Item;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.system.map.MapSystem;

import java.security.Key;

/**
 * @author Daan van Yperen
 */
@All(Player.class)
public class PlayerControlSystem extends FluidIteratingSystem {

    private static final float PLAYER_MOVEMENT_SPEED = 220f;
    private MapSystem mapSystem;
    private MapMask solid;

    @All(Item.class)
    private EntitySubscription items;

    @Override
    protected void initialize() {
        super.initialize();
        solid = mapSystem.getMask("solid");
    }

    @Override
    protected void process(E e) {
        if (!e.hasMoving()) {
            handleMovement(e);
        }
    }

    Vector2 vector2 = new Vector2();

    private void handleMovement(E e) {
        final GridPos gridPos = e.getGridPos();

        if ( Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ) {
            e.getLifter().attemptLifting = !e.getLifter().attemptLifting;
        }

        int dx = Gdx.input.isKeyPressed(Input.Keys.A) ? -1 :
                Gdx.input.isKeyPressed(Input.Keys.D) ? 1 : 0;
        int dy = Gdx.input.isKeyPressed(Input.Keys.W) ? 1 :
                Gdx.input.isKeyPressed(Input.Keys.S) ? -1 : 0;

        Vector2 movementVector = vector2.set(dx, dy).nor();


        if ( movementVector.x != 0 ) {
            e.getPhysics().vx = movementVector.x * PLAYER_MOVEMENT_SPEED * 1.1f;
        }
        if ( movementVector.y != 0 ) {
            e.getPhysics().vy = movementVector.y * PLAYER_MOVEMENT_SPEED;
        }
        e.getPhysics().friction = 50;
    }

}
