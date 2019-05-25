package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.graphics.Color;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.game.component.Shopper;
import net.mostlyoriginal.game.util.Scripts;

import static net.mostlyoriginal.api.operation.OperationFactory.*;
import static net.mostlyoriginal.api.utils.Duration.seconds;

/**
 * @author Daan van Yperen
 */
@All(Shopper.class)
public class ShopperAISystem extends FluidIteratingSystem {

    private boolean nightTime;

    @Override
    protected void begin() {
        super.begin();
        nightTime = E.withTag("player").playerNighttime();
    }

    @Override
    protected void process(E e) {
        Shopper shopper = e.getShopper();
        shopper.age += world.delta;

        if (e.hasHolding() && e.getHolding().id != -1) {
            // mirror parent tint.
            Color color = E.E(e.getHolding().id).tint(e.getTint()).getTint().color;
            color.a = color.a * 0.8f;
        }

        if (!e.hasScript()) {
            if (timeToLeaveFor(shopper)) {
                walkOffscreen(e);
            } else if (e.hasLifter()) {
                int itemsLifted = e.getLifter().itemsLifted;
                if (itemsLifted == 2) {
                    e.getLifter().itemsLifted++;
                    walkOffscreen(e);
                }
            }
        }
    }

    private boolean timeToLeaveFor(Shopper shopper) {
        return ((shopper.type == Shopper.Type.SHOPPER) == nightTime);
    }

    private void walkOffscreen(E e) {
        if (e.hasHolding() && e.holdingId() != -1) {
            E.E(e.holdingId()).deleteFromWorld();
            e.removeHolding();
        }
        e
                .removeGridPos()
                .script(sequence(
                        parallel(
                                Scripts.fadeOverTime(),
                                JamOperationFactory.moveBetween(e.posX(), e.posY(), e.posX(), e.posY() + 200, seconds(2f))),
                        deleteFromWorld()
                ));
    }
}
