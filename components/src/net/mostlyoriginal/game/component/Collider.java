package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.artemis.utils.IntBag;

/**
 * @author Daan van Yperen
 */
public class Collider extends Component {
    public long layer; // where are we?
    public long mask; // what do we collide with?

    // @todo is this the best solution? we can do better!
    @EntityId public IntBag colliding = new IntBag(4);

    public void set(long layer, long mask) {
        this.layer = layer;
        this.mask = mask;
    }

    public void set(long layer) {
        this.layer = layer;
        this.mask = 0;
    }

    public boolean isColliding() { return !colliding.isEmpty(); }

    public boolean collidesWith(Collider other) {
        return (other.layer & this.mask) != 0;
    }
}
