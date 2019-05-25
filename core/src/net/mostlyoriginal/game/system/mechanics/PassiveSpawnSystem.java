package net.mostlyoriginal.game.system.mechanics;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.util.Cooldown;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.PassiveSpawner;
import net.mostlyoriginal.game.system.control.PickupManager;
import net.mostlyoriginal.game.system.future.FutureSpawnUtility;

/**
 * Passive spawners produce items over time.
 *
 * @author Daan van Yperen
 */
@All(PassiveSpawner.class)
public class PassiveSpawnSystem extends FluidIteratingSystem {

    private static final float UPDATE_EVERY_SECONDS = 10f;

    private Cooldown cooldown = Cooldown.withInterval(UPDATE_EVERY_SECONDS);
    private PickupManager pickupManager;

    @Override
    protected boolean checkProcessing() {
        return cooldown.ready(world.delta);
    }

    @Override
    protected void process(E e) {
        PassiveSpawner spawner = e.getPassiveSpawner();
        if (spawner.items.length > 0 && MathUtils.random(1, 100) < 20) {
            spawn(e, spawner.items[0]);
            return;
        }
        // second item more rare.
        if (spawner.items.length > 1 && MathUtils.random(1, 100) < 10) {
            spawn(e, spawner.items[1]);
            return;
        }
        // third item rarest.
        if (spawner.items.length > 2 && MathUtils.random(1, 100) < 5) {
            spawn(e, spawner.items[2]);
        }
    }

    private void spawn(E e, String item) {

        if ( pickupManager.getOverlapping(e) == null ) {
            FutureSpawnUtility.item(item, 1, e.gridPosX(), e.gridPosY());
            E.E().playSound("sfx_putdown");

            E.E().particleEffect("poof").pos(e.gridPosX() * GameRules.CELL_SIZE + 16, e.gridPosY() * GameRules.CELL_SIZE + 16);
        }
    }
}
