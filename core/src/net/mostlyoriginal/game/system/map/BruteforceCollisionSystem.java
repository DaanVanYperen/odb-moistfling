package net.mostlyoriginal.game.system.map;

import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.game.component.Collider;

/**
 * Collision system. Bruteforce but should still be reasonably fast.
 *
 * @author Daan van Yperen
 */
@All({Collider.class, Pos.class, Bounds.class})
public class BruteforceCollisionSystem extends BaseEntitySystem {

    private ComponentMapper<Collider> mCollider;
    private ComponentMapper<Pos> mPos;
    private ComponentMapper<Bounds> mBounds;
    private final CollisionHandler collisionHandler;

    public BruteforceCollisionSystem(CollisionHandler collisionHandler) {
        this.collisionHandler = collisionHandler;
    }

    @Override
    protected void initialize() {
        super.initialize();
        world.inject(this.collisionHandler);
    }

    public interface CollisionHandler {
        void onCollision( int observerId, int collidedWithId );

        void begin();
        void end();
    }

    @Override
    protected void begin() {
        collisionHandler.begin();
    }

    @Override
    protected void end() {
        collisionHandler.end();
    }

    @Override
    protected void processSystem() {
        final IntBag actives = subscription.getEntities();
        final int[] ids = actives.getData();
        // clear collision lists.
        for (int i = 0, s = actives.size(); i < s; i++) {
            final Collider c1 = mCollider.get(ids[i]);
            // always forward search, no need to check all previous matches.
            for (int j = i + 1; j < s; j++) {
                final Collider c2 = mCollider.get(ids[j]);

                final boolean c1Interested = c1.collidesWith(c2);
                final boolean c2Interested = c2.collidesWith(c1);
                if ((c1Interested || c2Interested) && overlaps(ids[i], ids[j])) {
                    if (c1Interested) {
                        collisionHandler.onCollision(ids[i], ids[j]);
                    }
                    if (c2Interested) {
                        collisionHandler.onCollision(ids[j], ids[i]);
                    }
                }
            }
        }
    }

    public final boolean overlaps(final int a, final int b) {
        final Bounds b1 = mBounds.get(a);
        final Pos p1 = mPos.get(a);
        final Bounds b2 = mBounds.get(b);
        final Pos p2 = mPos.get(b);

        if (b1 == null || p1 == null || b2 == null || p2 == null)
            return false;

        final float minx = p1.xy.x + b1.minx;
        final float miny = p1.xy.y + b1.miny;
        final float maxx = p1.xy.x + b1.maxx;
        final float maxy = p1.xy.y + b1.maxy;

        final float bminx = p2.xy.x + b2.minx;
        final float bminy = p2.xy.y + b2.miny;
        final float bmaxx = p2.xy.x + b2.maxx;
        final float bmaxy = p2.xy.y + b2.maxy;

        return
                !(minx > bmaxx || maxx < bminx ||
                        miny > bmaxy || maxy < bminy);
    }
}
