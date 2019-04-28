package net.mostlyoriginal.game.system;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.game.Slot;

/**
 * @author Daan van Yperen
 */
@All(Slot.class)
public class SlotHighlightingSystem extends FluidIteratingSystem {

    private String liftingType;

    @Override
    protected void begin() {
        super.begin();
        E player = E.withTag("player");
        liftingType = player.hasLifting() && player.liftingId() != -1 ? E.E(player.liftingId()).itemType() : null;
    }

    @Override
    protected void process(E e) {
        boolean highlight = false;
        if ( liftingType != null ) {
            for (String s : e.slotAccepts()) {
                if (liftingType.equals(s) || s.equals("any")) {
                    highlight=true;
                }
            }
        }
        if ( highlight ) {
            e.anim("hopper_highlight");
        } else if ( e.hasAnim() ) e.removeAnim();
    }
}
