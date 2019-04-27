package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.EntitySubscription;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.annotations.Exclude;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.MyAnimRenderSystem;

/**
 * @author Daan van Yperen
 */
@All(Lifter.class)
public class PickupSystem extends FluidIteratingSystem {

    private static final int CARRIED_OBJECT_LIFTING_HEIGHT = 0;
    @All({CanPickup.class, GridPos.class})
    @Exclude(Moving.class)
    private EntitySubscription pickupables;

    RenderBatchingSystem renderBatchingSystem;

    @Override
    protected void process(E e) {
        if (e.lifterAttemptLifting()) {
            if (!e.hasLifting()) {
                attemptPickup(e);
            }
        } else {
            if (e.hasLifting()) {
                attemptDrop(e);
            }
        }
        followCarrier(e);
    }

    private void followCarrier(E actor) {
        if (actor.hasLifting() && actor.getLifting().id != -1) {
            E lifting = E.E(actor.getLifting().id);
            lifting.posX(actor.getPos().xy.x);
            lifting.posY(actor.getPos().xy.y + CARRIED_OBJECT_LIFTING_HEIGHT);
        }
    }

    private void attemptDrop(E actor) {
        E item = E.E(actor.liftingId());
        if (!actor.isMoving() && !item.isMoving() && anythingAt(actor)) {
            actor.removeLifting();
            item.gridPos(actor.getGridPos()).removeLifted().renderLayer(GameRules.LAYER_ITEM);
            renderBatchingSystem.sortedDirty=true;
            System.out.println("Drop!");
        }
    }

    private boolean anythingAt(E actor) {
        return getOverlapping(actor) == null;
    }

    private void attemptPickup(E actor) {
        E item = getOverlapping(actor);
        if (item != null) {
            actor.liftingId(item.id());
            item.removeGridPos().lifted().renderLayer(GameRules.LAYER_ITEM_CARRIED);
            renderBatchingSystem.sortedDirty=true;
            System.out.println("Pickup!");
        }
    }

    private E getOverlapping(E actor) {
        IntBag pickupEntities = pickupables.getEntities();
        E overlaps = null;
        for (int i = 0, s = pickupEntities.size(); i < s; i++) {
            E item = E.E(pickupEntities.get(i));
            if (!item.hasLifted() && item.getGridPos().overlaps(actor.getGridPos())) {
                overlaps = item;
            }
        }
        return overlaps;
    }
}
