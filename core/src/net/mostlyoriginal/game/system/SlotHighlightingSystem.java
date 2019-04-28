package net.mostlyoriginal.game.system;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Interpolation;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.api.operation.common.Operation;
import net.mostlyoriginal.game.Slot;
import net.mostlyoriginal.game.component.GridPos;

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

    private static final Tint HIGHLIGHTED = new Tint(1f,1f,1f,0.6f);

    @Override
    protected void process(E e) {
        boolean highlight = acceptsItemType(e,liftingType);
        if (highlight ) {
            if ( !e.hasAnim() ) {
                e.anim("hopper_highlight").tint(Tint.TRANSPARENT).script(
                        OperationFactory.sequence(
                                JamOperationFactory.tintBetween(Tint.TRANSPARENT,HIGHLIGHTED, 0.5f, Interpolation.fade)
                        )
                );
            }
        } else if (e.hasAnim() && !e.hasScript()) {
            e.script(
                    OperationFactory.sequence(
                            JamOperationFactory.tintBetween(HIGHLIGHTED, Tint.TRANSPARENT, 0.5f, Interpolation.fade),
                            OperationFactory.remove(Anim.class)
                    )
            );
        }
    }

    public boolean acceptsItemType(E e, String type) {
        boolean result = false;
        if (type != null) {
            for (String s : e.slotAccepts()) {
                if (type.equals(s) || s.equals("any")) {
                    result = true;
                }
            }
        }
        return result;
    }

    public E getSlotAt(GridPos gridPos) {
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();
        for (int i = 0, s = actives.size(); s > i; i++) {
            E slot = E.E(ids[i]);
            if (slot.gridPosOverlaps(gridPos)) return slot;
        }
        return null;
    }
}
