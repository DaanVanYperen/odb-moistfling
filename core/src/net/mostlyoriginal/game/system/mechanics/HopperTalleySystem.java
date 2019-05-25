package net.mostlyoriginal.game.system.mechanics;

import com.artemis.E;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import net.mostlyoriginal.game.component.Machine;
import net.mostlyoriginal.game.component.inventory.Inventory;
import net.mostlyoriginal.game.system.common.FluidSystem;

/**
 *
 * @author Daan van Yperen
 */
@All(Machine.class)
public class HopperTalleySystem extends FluidSystem {

    @Override
    protected void process(E e) {

        final Machine machine = e.getMachine();
        final IntBag hoppers = machine.hoppers;

        machine.contents.clear();
        for (int i = 0, s = hoppers.size(); i < s; i++) {
            final Inventory inventory = E.E(hoppers.get(i)).getInventory();
            if (inventory.isNotEmpty()) {
                machine.contents.addAll(inventory.contents);
            }
        }
    }
}
