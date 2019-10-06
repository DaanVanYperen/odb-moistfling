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
public class PassiveSpawnerSystem extends FluidIteratingSystem {

    private static final float UPDATE_EVERY_SECONDS = 1f;

    private Cooldown cooldown = Cooldown.withInterval(UPDATE_EVERY_SECONDS);
    private PickupManager pickupManager;

    @Override
    protected boolean checkProcessing() {
        return cooldown.ready(world.delta);
    }

    @Override
    protected void process(E e) {
        PassiveSpawner spawner = e.getPassiveSpawner();

        e.animId(pickupManager.getOverlapping(e)!=null?spawner.animSpawned:spawner.animNormal);
        if (spawner.items.length > 0 && MathUtils.random(1, 100) < 2) {
            spawn(e, spawner.items[MathUtils.random(0,spawner.items.length-1)]);
        }
    }

    private void spawn(E e, String item) {

        if ( pickupManager.getOverlapping(e) == null ) {
            e.animId(e.getPassiveSpawner().animSpawned);
            FutureSpawnUtility.item(item, 1, e.gridPosX(), e.gridPosY(), false);
            E.E().playSound("sfx_putdown");

            E.E().particleEffect("poof").pos(e.gridPosX() * GameRules.CELL_SIZE + 16, e.gridPosY() * GameRules.CELL_SIZE + 16);
        }
    }
}
