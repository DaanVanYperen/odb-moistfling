package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Paying;

/**
 * @author Daan van Yperen
 */
@All(Paying.class)
public class PayingSystem extends FluidIteratingSystem {

    @Override
    protected void begin() {
        super.begin();
    }

    @Override
    protected void process(E e) {
        E player = E.withTag("player");
        int gold = e.getPaying().gold;
        player.getPlayer().gold += gold;
        e.removePaying();

        E.E()
                .pos(e.getPos())
                .labelText(gold + "S")
                .fontFontName("5x5")
                .labelAlign(Label.Align.RIGHT)
                .renderLayer(GameRules.LAYER_PAYMENT)
                .script(
                        OperationFactory.parallel(
                                JamOperationFactory.moveBetween(e.posX() + GameRules.CELL_SIZE/2f, e.posY()+ GameRules.CELL_SIZE, e.posX() + GameRules.CELL_SIZE/2f, e.posY() + 128+ GameRules.CELL_SIZE, 2f),
                                JamOperationFactory.tintBetween(Tint.WHITE, Tint.TRANSPARENT, 2f)
                        ));
    }
}
