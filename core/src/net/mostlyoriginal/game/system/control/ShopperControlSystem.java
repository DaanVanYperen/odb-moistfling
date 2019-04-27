package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Shopper;

/**
 * @author Daan van Yperen
 */
@All(Shopper.class)
public class ShopperControlSystem extends FluidIteratingSystem {

    PickupManager pickupManager;

    @Override
    protected void process(E e) {
        int itemsLifted = e.getLifter().itemsLifted;
        if ( itemsLifted == 1 ) {
            e.getLifter().payOnPickup=true;
            attemptToExchangeCarriedItem(e);
        }

        if ( itemsLifted == 2) {
            walkOffscreen(e);
            deleteWhenOffscreen(e);
        }

    }

    private void deleteWhenOffscreen(E e) {
        if ( e.posX() > GameRules.SCREEN_WIDTH ) {
            if ( e.liftingId() != -1 ) {
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

            // we only exchange when we get a desired item.
            if ( itemOnFloor.itemType().equals(e.getDesire().desiredItem)) {
                e.lifterDestroyWhenDropped(false);
            }

            e.lifterAttemptLifting(false);

        }
    }
}
