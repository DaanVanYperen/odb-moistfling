package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Pickup extends Component {
    public Type type;
    public enum Type {
        OXYGEN,
        EXIT
    }
}
