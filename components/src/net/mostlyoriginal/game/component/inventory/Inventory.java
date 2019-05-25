package net.mostlyoriginal.game.component.inventory;

import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.artemis.annotations.LinkPolicy;
import com.artemis.utils.IntBag;

/**
 * @author Daan van Yperen
 */
public class Inventory extends Component {
    public enum Mode {
        STORE,
        EXPAND,
        HOPPER
    }

    @EntityId
    @LinkPolicy(LinkPolicy.Policy.CHECK_SOURCE_AND_TARGETS)
    public IntBag contents = new IntBag();

    public String[] accepts;
    public Mode mode; // @todo deprecate?
    public int maxItemTypes = 1;
    public int x; // @todo deprecate?
    public int y; // @todo deprecate?

    public boolean isFull() {
        return contents.size() >= maxItemTypes;
    }
    public boolean isNotEmpty() { return !contents.isEmpty(); }
    public boolean isEmpty() { return contents.isEmpty(); }

    public boolean acceptsType(String type) {
        if (type != null) {
            for (String s : accepts) {
                if (type.equals(s) || s.equals("any")) {
                    return true;
                }
            }
        }
        return false;
    }
}
