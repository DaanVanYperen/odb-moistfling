package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Blinking extends Component {
    public float duration=2;

    public void set(float duration ) {
        this.duration=duration;
    }
}
