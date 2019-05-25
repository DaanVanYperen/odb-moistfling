package net.mostlyoriginal.game.component.collision;

import com.artemis.Component;
import com.artemis.annotations.EntityId;

/**
 * @author Daan van Yperen
 */
public class CollisionPair extends Component {
    @EntityId
    public int observerId;
    @EntityId
    public int collidedWithId;
    public int type;
    public Phase phase = Phase.ENTER;

    public enum Phase {
        ENTER,
        INSIDE,
        EXIT
    }

    public boolean isEntered() {
        return phase == Phase.ENTER;
    }

    public void set(int observerId, int collidedWithId, int type) {
        this.observerId = observerId;
        this.collidedWithId = collidedWithId;
        this.type = type;
    }

    public boolean matches(int observerId, int collidedWithId) {
        return this.observerId == observerId && this.collidedWithId == collidedWithId;
    }
}
