package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author Daan van Yperen
 */
public class Emotion extends Component {

    public enum State {
        NEUTRAL,
        HAPPY,
        ANGRY,
        ENRAGED
    }

    public State state = MathUtils.randomBoolean() ? State.HAPPY : State.NEUTRAL;
}
