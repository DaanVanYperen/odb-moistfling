package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.EntitySubscription;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.annotations.Exclude;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.InDialog;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.Item;
import net.mostlyoriginal.game.component.ItemData;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.manager.ItemRepository;
import net.mostlyoriginal.game.system.NightSystem;
import net.mostlyoriginal.game.system.map.MapSpawnerSystem;
import net.mostlyoriginal.game.system.map.MapSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
@All(Player.class)
@Exclude(InDialog.class)
public class PlayerControlSystem extends FluidIteratingSystem {

    private static final float PLAYER_MOVEMENT_SPEED = 220f;
    private MapSystem mapSystem;
    private MapMask solid;

    @All(Item.class)
    private EntitySubscription items;
    private MapSpawnerSystem mapSpawnSystem;
    private ItemRepository itemRepository;
    private NightSystem nightSystem;

    public float interactCooldown = 0;
    private GameScreenAssetSystem gameScreenAssetSystem;

    @Override
    protected void initialize() {
        super.initialize();
        solid = mapSystem.getMask("solid");
    }

    @Override
    protected void process(E e) {
        interactCooldown -= world.delta;
        if (!e.hasMoving()) {
            handleMovement(e);
        }
    }

    Vector2 vector2 = new Vector2();

    private void handleMovement(E e) {
        final GridPos gridPos = e.getGridPos();

        if ( GameRules.DEBUG_ENABLED  ) {
            if (Gdx.input.isKeyPressed(Input.Keys.F5)) {
                E.withTag("player").playerAge(0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.F6)) {
                E.withTag("player").playerAge(1);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.F7)) {
                E.withTag("player").playerAge(2);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.F8)) {
                E.withTag("player").playerAge(3);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.F9)) {
                int index=0;
                for (ItemData data : itemRepository.itemLibrary.items) {
                    mapSpawnSystem.spawnItem(index++ % 20,index / 20,data.id).itemCount(99);
                }

            }
        }

        if ( interactCooldown <= 0 ) {
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
