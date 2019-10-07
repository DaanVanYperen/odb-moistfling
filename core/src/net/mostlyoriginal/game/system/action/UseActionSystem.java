package net.mostlyoriginal.game.system.action;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.action.ActionUse;
import net.mostlyoriginal.game.system.MyParticleEffectStrategy;
import net.mostlyoriginal.game.system.StaminaSystem;
import net.mostlyoriginal.game.system.control.PlayerControlSystem;
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

    private PlayerControlSystem playerControlSystem;

    @Override
    protected void process(E actor) {
        ActionUse action = actor.getActionUse();
        if (action.target != -1 && actor.hasHolding() && !actor.isMoving()) {
            use(E.E(action.target), actor.getGridPos(), actor);
        }
        actor.removeActionBuild();
    }

    public void use(E item, GridPos gridPos, E actor) {

        switch ( item.getItem().type) {
            case "item_flippers":
                playerControlSystem.enableFlipperBonus();
                wear(item,actor); break;
            case "item_snorkel":
                playerControlSystem.enableSnorkelBonus();
                wear(item,actor); break;
            default:
                eat(item, actor);
                break;
        }

    }

    private void wear(E item, E actor) {
        actor.removeHolding();
//        E.E().pos().attachedParent(actor.id()).anim(item.animId()).renderLayer(GameRules.LAYER_PLAYER-1);
        item.deleteFromWorld();
    }

    private void eat(E item, E actor) {
        E.E().playSound("burp");
        staminaSystem.staminaIncrease(1f);
        actor.removeHolding();
        item.deleteFromWorld();
    }
}
