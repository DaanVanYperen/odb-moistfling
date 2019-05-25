package net.mostlyoriginal.game.system.action;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.api.component.graphics.RendererSingleton;
import net.mostlyoriginal.game.component.action.ActionTrade;
import net.mostlyoriginal.game.component.dialog.DialogSingleton;
import net.mostlyoriginal.game.system.MyDialogFactory;


/**
 * Drop target at current location.
 *
 * @author Daan van Yperen
 */
@All(ActionTrade.class)
public class TradeActionSystem extends FluidIteratingSystem {

    private RendererSingleton rendererSingleton;
    private DialogSingleton dialog;

    @Override
    protected void process(E actor) {
        final ActionTrade action = actor.getActionTrade();
        if (action.target != -1 && actor.hasHolding() && !actor.isMoving()) {
            final E patron = E.E(action.target);
            if (patron != null && patron.hasHolding() && !patron.isMoving() && patronHasntTradedYet(patron)) {
                swapItems(actor, E.E(actor.holdingId()), patron, E.E(patron.holdingId()));
            }
        }
        actor.removeActionTrade();
    }


    private boolean patronHasntTradedYet(E patron) {
        return patron.getLifter().itemsLifted <= 2;
    }

    private void swapItems(E actor, E actorItem, E patron, E patronItem) {

        int tmp = actorItem.id();
        actor.holdingId(patronItem.id());
        actor.getLifter().itemsLifted++;
        patron.holdingId(actorItem.id());

        final int goldValue = E.E(actorItem.id()).getItemMetadata().data.gold;
        if (goldValue > 0) {
            patron.paying(goldValue);
            E.E().playSound("sfx_money_1");
        } else {
            E.E().playSound("sfx_putdown");
        }

        patron.removeDesire();
        patron.getLifter().itemsLifted++;
        rendererSingleton.sortedDirty = true;

        if (E.E(patronItem.id()).itemType().equals("item_ring")) {
            if (patron.getAnim().id.equals("actor_postal")) {
                dialog.startNextDialog = MyDialogFactory.DIALOG_FINAL_POSTMAN;
            } else {
                dialog.startNextDialog = MyDialogFactory.DIALOG_FINAL_HAG;
            }
            E.withTag("player").playerDone(true);
        }
    }
}
