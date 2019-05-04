package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.annotations.Exclude;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.util.Cooldown;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.component.dialog.InDialog;
import net.mostlyoriginal.game.component.map.TiledMapSingleton;
import net.mostlyoriginal.game.system.mechanics.NightSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
@All(Player.class)
@Exclude(InDialog.class)
public class PlayerControlSystem extends FluidIteratingSystem {

    private static final float PLAYER_MOVEMENT_SPEED = 220f;

    private TiledMapSingleton tiledMap;
    private NightSystem nightSystem;

    public Cooldown interactCooldown = Cooldown.withInterval(0.2f).autoReset(false);
    private GameScreenAssetSystem gameScreenAssetSystem;

    @Override
    protected void process(E e) {
        interactCooldown.decreaseBy(world.delta);
        if (!e.hasMoving()) {
            handleMovement(e);
        }
    }

    Vector2 vector2 = new Vector2();

    private void handleMovement(E e) {
        final GridPos gridPos = e.getGridPos();

        if ( interactCooldown.ready() ) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
                if (e.getGridPos().x >= 15 && e.getGridPos().x <= 18 && e.getGridPos().y >= 8) {
                    nightSystem.toggle();
                    gameScreenAssetSystem.playSfx("sfx_pickup");
                } else {
                    e.getLifter().attemptLifting = !e.getLifter().attemptLifting;
                }
            }
        }

        int dx = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT) ? -1 :
                Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) ? 1 : 0;
        int dy = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)? 1 :
                Gdx.input.isKeyPressed(Input.Keys.S)|| Gdx.input.isKeyPressed(Input.Keys.DOWN) ? -1 : 0;

        e.playerDx(dx);
        e.playerDy(dy);

        Vector2 movementVector = vector2.set(dx, dy).nor();

        e.physics();
        if ( movementVector.x != 0 ) {
            e.getPhysics().vx = movementVector.x * PLAYER_MOVEMENT_SPEED * 1.1f;
        }
        if ( movementVector.y != 0 ) {
            e.getPhysics().vy = movementVector.y * PLAYER_MOVEMENT_SPEED;
        }
        e.getPhysics().friction = 50;
    }

}
