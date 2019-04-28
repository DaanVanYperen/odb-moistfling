package net.mostlyoriginal.game.system.control;

import com.artemis.ComponentMapper;
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
            final E indicatorCloud = E.E()
                    .anim("timmy_the_speech_bubble")
                    .renderLayer(GameRules.LAYER_DESIRE_INDICATOR-1)
                    .tint(1f, 1f, 1f, 0.9f);
            desire.desireIndicatorCloudId =
                    indicatorCloud.id();

            final E indicator = E.E()
                    .anim(itemRepository.get(desire.desiredItem).sprite)
                    .renderLayer(GameRules.LAYER_DESIRE_INDICATOR)
                    .tint(1f, 1f, 1f, 0.9f);
            desire.desireIndicatorId =
                    indicator.id();
        }

        // follow shopper.
        E.E(desire.desireIndicatorId).posX(e.posX()-2)
                .posY(e.posY() + DESIRE_INDICATOR_OFFSET_Y+2);
        E.E(desire.desireIndicatorCloudId).posX(e.posX()-4)
                .posY(e.posY() + DESIRE_INDICATOR_OFFSET_Y-4);
    }

    ComponentMapper<Desire> mDesire;


    @Override
    protected void removed(int entityId) {
        // kill indicator as well.
        int indicatorId = mDesire.get(entityId).desireIndicatorId;
        if ( indicatorId != -1 ) {
            E.E(indicatorId).deleteFromWorld();
        }
        int cloudIndicatorId = mDesire.get(entityId).desireIndicatorCloudId;
        if ( cloudIndicatorId != -1 ) {
            E.E(cloudIndicatorId).deleteFromWorld();
        }
    }
}
