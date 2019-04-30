package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Slot extends Component {
    public enum Mode {
        STORE,
        EXPAND
    }
    public String[] accepts;
    public Mode mode;
    public int x;
    public int y;
}
