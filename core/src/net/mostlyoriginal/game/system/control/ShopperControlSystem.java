package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.graphics.Color;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Shopper;
import net.mostlyoriginal.game.system.map.Scripts;

/**
 * @author Daan van Yperen
 */
@All(Shopper.class)
public class ShopperControlSystem extends FluidIteratingSystem {

    PickupManager pickupManager;
    private E player;

    @Override
    protected void begin() {
        super.begin();
        player = E.withTag("player");
    }

    @Override
    protected void process(E e) {
        Shopper shopper = e.getShopper();
        shopper.age += world.delta;

        if (e.hasLifting()) {
            // mirror parent tint.
            Color color = E.E(e.getLifting().id).tint(e.getTint()).getTint().color;
            color.a = color.a * 0.8f;
        }

        if (timeToLeaveFor(shopper)) {
            if (e.getLifter().itemsLifted != 3) {
                e.getLifter().itemsLifted = 3;
                walkOffscreen(e);
                deleteWhenOffscreen(e);
            }
        } else {
            int itemsLifted = e.getLifter().itemsLifted;

            if (itemsLifted == 2) {
                e.getLifter().itemsLifted++;
                walkOffscreen(e);
            }
            if (itemsLifted >= 3) {
                deleteWhenOffscreen(e);
            }
        }

    }

    private boolean timeToLeaveFor(Shopper shopper) {
        return shopper.age >= shopper.leaveAge || player.playerNighttime();
    }

    private void deleteWhenOffscreen(E e) {
        if (!e.hasScript()) {
            if (e.liftingId() != -1) {
                E.E(e.liftingId()).deleteFromWorld();
            }
            e.deleteFromWorld();
        }
    }

    private void walkOffscreen(E e) {
        e.gridPosY(GameRules.SCREEN_HEIGHT / GameRules.CELL_SIZE).removeScript().script(Scripts.fadeOverTime());
    }

    private void attemptToExchangeCarriedItem(E e) {
        E itemOnFloor = pickupManager.getOverlapping(e);
        if (itemOnFloor != null) {
            e.lifterAttemptLifting(false);
        }
    }
}
