package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Desire;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.manager.ItemRepository;

/**
 * @author Daan van Yperen
 */
@All({Desire.class, Pos.class, GridPos.class})
public class DesireSystem extends FluidIteratingSystem {

    private static final int DESIRE_INDICATOR_OFFSET_Y = 48;
    private ItemRepository itemRepository;

    @Override
    protected void process(E e) {
        Desire desire = e.getDesire();
        if (desire.desireIndicatorId == -1) {
            final E indicator = E.E()
                    .posX(e.posX())
                    .posY(e.posY() + DESIRE_INDICATOR_OFFSET_Y)
                    .anim(itemRepository.get(desire.desiredItem).sprite)
                    .renderLayer(GameRules.LAYER_DESIRE_INDICATOR)
                    .tint(1f, 1f, 1f, 0.5f);
            desire.desireIndicatorId =
                    indicator.id();
        }

        // follow shopper.
        E.E(desire.desireIndicatorId).posX(e.posX())
                .posY(e.posY() + DESIRE_INDICATOR_OFFSET_Y);
    }
}
