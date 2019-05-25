package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Shopper extends Component {

    public float age=0;
    public Type type = Type.SHOPPER;

    public enum Type {
        SHOPPER,
        HAG,
        POSTAL
    }
}
