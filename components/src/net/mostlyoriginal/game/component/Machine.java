package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.artemis.annotations.LinkPolicy;
import com.artemis.utils.IntBag;

/**
 * @author Daan van Yperen
 */
public class Machine extends Component {

    public enum Type {
        ALTAR
    }

    public Type type;
    public float warmupAge = 0;

    @EntityId
    public IntBag hoppers = new IntBag();

    @EntityId
    @LinkPolicy(LinkPolicy.Policy.CHECK_SOURCE_AND_TARGETS)
    public IntBag contents = new IntBag();
}
