package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Shopper;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.map.MapSpawnerSystem;

/**
 * @author Daan van Yperen
 */
@All(Shopper.class)
public class ShopperControlSystem extends FluidIteratingSystem {

    PickupManager pickupManager;

    @Override
    protected void end() {
        super.end();
    }

    @Override
    protected void process(E e) {
        Shopper shopper = e.getShopper();
        shopper.age += world.delta;

        if (timeToLeaveFor(shopper)) {
            walkOffscreen(e);
            deleteWhenOffscreen(e);
        } else {
            int itemsLifted = e.getLifter().itemsLifted;

            if (itemsLifted == 2) {
                walkOffscreen(e);
                deleteWhenOffscreen(e);
            }
        }

    }

    private boolean timeToLeaveFor(Shopper shopper) {
        return shopper.age >= shopper.leaveAge;
    }

    private void deleteWhenOffscreen(E e) {
        if (e.posX() > GameRules.SCREEN_WIDTH) {
            if (e.liftingId() != -1) {
                E.E(e.liftingId()).deleteFromWorld();
            }
            e.deleteFromWorld();
        }
    }

    private void walkOffscreen(E e) {
        e.gridPosX(GameRules.SCREEN_WIDTH / GameRules.CELL_SIZE + GameRules.CELL_SIZE);
    }

    private void attemptToExchangeCarriedItem(E e) {
        E itemOnFloor = pickupManager.getOverlapping(e);
        if (itemOnFloor != null) {
            e.lifterAttemptLifting(false);
        }
    }
}
