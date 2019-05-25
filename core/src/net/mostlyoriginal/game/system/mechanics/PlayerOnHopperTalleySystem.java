package net.mostlyoriginal.game.system.mechanics;

import com.artemis.E;
import com.artemis.annotations.All;
import net.mostlyoriginal.api.FluidCollisionSystem;
import net.mostlyoriginal.game.CollisionPairTypes;
import net.mostlyoriginal.game.component.collision.CollisionPair;

/**
 * Count players on hoppers towards the contents of machines.
 *
 * Must be run after the main talley system.
 *
 * @author Daan van Yperen
 */
@All(CollisionPair.class)
public class PlayerOnHopperTalleySystem extends FluidCollisionSystem {

    public PlayerOnHopperTalleySystem() {
        super(CollisionPairTypes.PLAYER_ON_HOPPER);
    }

    @Override
    protected void handleCollision(E observer, E collidedWith, CollisionPair.Phase phase) {
        E.E(collidedWith.hopperMachineId()).machineContents().add(observer.id());
    }
}
