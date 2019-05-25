package net.mostlyoriginal.game.system;

import com.artemis.ComponentMapper;
import com.artemis.E;
import com.artemis.ESubscription;
import com.artemis.annotations.All;
import net.mostlyoriginal.game.CollisionPairTypes;
import net.mostlyoriginal.game.component.collision.CollisionPair;
import net.mostlyoriginal.game.system.map.BruteforceCollisionSystem;

/**
 * Collision handler with entrance/exit tracking.
 *
 * @todo Push tracking lifecycle to the system? or a dedicated class.
 * @author Daan van Yperen
 */
public class MyCollisionHandler implements BruteforceCollisionSystem.CollisionHandler {

    private ComponentMapper<CollisionPair> mCollisionPair;

    @All(CollisionPair.class)
    private ESubscription collisionPairs;

    @Override
    public void onCollision(int observerId, int collidedWithId) {
        final E observer = E.E(observerId);
        final E collided = E.E(collidedWithId);

        // create pair
        if (observer.hasPlayer() && collided.hasHopper()) {
            getCreateCollisionPair(observerId, collidedWithId, CollisionPairTypes.PLAYER_ON_HOPPER);
        }
    }

    private CollisionPair getCreateCollisionPair(int observerId, int collidedWithId, int collisionType) {

        for (E e : collisionPairs) {
            if (e.hasCollisionPair()) {
                final CollisionPair pair = e.getCollisionPair();
                if (pair.matches(observerId, collidedWithId)) {
                    pair.phase = CollisionPair.Phase.INSIDE;
                    return pair;
                }
            }
        }

        return E.E().collisionPair(observerId, collidedWithId, collisionType).getCollisionPair();
    }

    @Override
    public void begin() {
        for (E e : collisionPairs) {
            final CollisionPair pair = e.getCollisionPair();
            if (pair.phase == CollisionPair.Phase.INSIDE) {
                // if untouched will become exit phase. If collision will become INSIDE again during onCollision calls.
                pair.phase = CollisionPair.Phase.EXIT;
            } else if (pair.phase == CollisionPair.Phase.EXIT) {
                e.removeCollisionPair(); // expire.
            }
        }
    }

    @Override
    public void end() {
        for (E e : collisionPairs) {
            if ( !e.hasCollisionPair() )
                e.deleteFromWorld();
        }
    }
}
