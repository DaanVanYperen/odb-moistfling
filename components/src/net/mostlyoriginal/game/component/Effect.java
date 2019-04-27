package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Effect extends Component {
    public enum Type {
        UNCLOG,
        CLEAN
    }
    public Effect.Type type;
    public void set(Effect.Type type) { this.type = type; }
}
