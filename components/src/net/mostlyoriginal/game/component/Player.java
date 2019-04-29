package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Player extends Component {
    public static final int MAX_AGE = 3;
    public static final int MIN_AGE = 0;
    public int gold;
    public int age=1;
    public boolean nighttime=true;
    public int day=1;

    public int visitorsRemaining = 2;
}
