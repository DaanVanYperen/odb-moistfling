package net.mostlyoriginal.game.system.action;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.game.component.action.ActionTalk;
import net.mostlyoriginal.game.component.dialog.DialogSingleton;


/**
 * Talk with target.
 *
 * @author Daan van Yperen
 */
@All(ActionTalk.class)
public class TalkActionSystem extends FluidIteratingSystem {

    private DialogSingleton dialog;

    @Override
    protected void process(E actor) {
        final ActionTalk action = actor.getActionTalk();
        if (action.target != -1) {
            final E patron = E.E(action.target);
            if (patron != null) {
                dialog.startNextDialog = patron.wantsToDiscussDialog();

                if ( patron.hasHolding() ) {
                    actor.holdingId(patron.holdingId());
                    patron.removeHolding();
                }
                //patron.removeWantsToDiscuss();
            }
        }
        actor.removeActionTalk();
    }
}
