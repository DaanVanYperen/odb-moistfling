package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author Daan van Yperen
 */
public class Desire extends Component {
    public enum Type {
        LEAVE,
        TIP,
        POOP,
        WASH_HANDS,
        PEE,
    }
    public Type type;
    public int index= MathUtils.random(1,7);
    public void set(Type type) { this.type = type; }
}
