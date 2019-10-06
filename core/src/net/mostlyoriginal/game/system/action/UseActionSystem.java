package net.mostlyoriginal.game.system.action;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.action.ActionUse;
import net.mostlyoriginal.game.system.MyParticleEffectStrategy;
import net.mostlyoriginal.game.system.StaminaSystem;
import net.mostlyoriginal.game.system.future.FutureSpawnUtility;
import net.mostlyoriginal.game.system.render.SlotManager;


/**
 * Build target at current location.
 *
 * @author Daan van Yperen
 */
@All(ActionUse.class)
public class UseActionSystem extends FluidIteratingSystem {

    private SlotManager slotManager;
    private StaminaSystem staminaSystem;

    @Override
    protected void process(E actor) {
        ActionUse action = actor.getActionUse();
        if (action.target != -1 && actor.hasHolding() && !actor.isMoving()) {
            use(E.E(action.target), actor.getGridPos(), actor);
        }
        actor.removeActionBuild();
    }

    public void use(E item, GridPos gridPos, E actor) {

        staminaSystem.staminaIncrease(0.5f);
        actor.removeHolding();
        item.deleteFromWorld();
    }
}
