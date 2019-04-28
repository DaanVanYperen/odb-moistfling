package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Shopper extends Component {
    private static final int SECONDS_BEFORE_LEAVING=999;

    public float age=0;
    public float leaveAge=SECONDS_BEFORE_LEAVING;
}
