package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.game.component.Lifter;


/**
 * @author Daan van Yperen
 */
@All({Lifter.class, Pos.class})
public class CarriedItemPositioningSystem extends FluidIteratingSystem {

    private static final int CARRIED_OBJECT_PLAYER_LIFTING_HEIGHT = 32;
    private static final int CARRIED_OBJECT_SHOPPER_LIFTING_HEIGHT = 4;

    @Override
    protected void process(E e) {
        followCarrier(e);
    }

    private void followCarrier(E actor) {
        if (actor.hasHolding() && actor.getHolding().id != -1) {
            E lifting = E.E(actor.getHolding().id);
            if (actor.hasShopper()) {
                lifting.scale(1f);
                lifting.posX(actor.getPos().xy.x + 8);
                lifting.posY(actor.getPos().xy.y + CARRIED_OBJECT_SHOPPER_LIFTING_HEIGHT - 8);
            } else {
                lifting.scale(1f);
                lifting.tint(Tint.WHITE);
                lifting.posX(actor.getPos().xy.x);
                lifting.posY(actor.getPos().xy.y + CARRIED_OBJECT_PLAYER_LIFTING_HEIGHT);
            }
        }
    }
}
