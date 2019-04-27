package net.mostlyoriginal.game.component;

import com.artemis.Component;

import static net.mostlyoriginal.game.component.Guard.State.WALKING;

/**
 * @author Daan van Yperen
 */
public class Guard extends Component {
    public float slideCooldown=0;
    public State state = WALKING;
    public int tutorial = -1;
    public int bandaged = 0;

    public float targetX = 0;

    public enum State {
        WALKING,
        CROUCHING,
        SLIDING,
        JUMPING,
        GRABBING
    }
}
