package net.mostlyoriginal.api;

import com.artemis.Aspect;
import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import net.mostlyoriginal.game.CollisionPairTypes;
import net.mostlyoriginal.game.component.collision.CollisionPair;

/**
 * Handles collision
 *
 * @author Daan van Yperen
 */
public abstract class FluidCollisionSystem extends FluidIteratingSystem {

    private int type;

    public FluidCollisionSystem(int type) {
        super(Aspect.all(CollisionPair.class));
        this.type = type;
    }

    @Override
    protected void process(E e) {
        type = CollisionPairTypes.PLAYER_ON_HOPPER;
        if ( e.collisionPairType() == type) {
            handleCollision(
                    E.E(e.collisionPairObserverId()),
                    E.E(e.collisionPairCollidedWithId()), e.collisionPairPhase());
        }
    }

    protected abstract void handleCollision(E observer, E collidedWith, CollisionPair.Phase phase);
}
