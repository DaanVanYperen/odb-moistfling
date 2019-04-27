package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.artemis.utils.IntBag;

/**
 * @author Daan van Yperen
 */
public class Machine extends Component {
    public enum Type {
        ALTAR
    }

    public Type type;

    @EntityId
    public IntBag hoppers = new IntBag();

    @EntityId
    public IntBag contents = new IntBag();
}
