package net.mostlyoriginal.game.system.render;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.annotations.Exclude;
import com.badlogic.gdx.math.Interpolation;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.component.inventory.Inventory;

/**
 * @author Daan van Yperen
 */
@All(Inventory.class)
@Exclude(Player.class)
public class SlotHighlightingSystem extends FluidIteratingSystem {

    private String liftingType;

    @Override
    protected void begin() {
        super.begin();
        E player = E.withTag("player");
        liftingType = player.hasHolding() && player.holdingId() != -1 ? E.E(player.holdingId()).itemType() : null;
    }

    private static final Tint HIGHLIGHTED = new Tint(1f,1f,1f,0.6f);

    @Override
    protected void process(E e) {
        boolean highlight = e.getInventory().acceptsType(liftingType);
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
}
