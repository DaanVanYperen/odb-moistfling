package net.mostlyoriginal.game.component;

import com.artemis.Component;
import net.mostlyoriginal.game.system.control.Days;

/**
 * @author Daan van Yperen
 */
public class Player extends Component {
    public static final int MAX_AGE = 3;
    public static final int MIN_AGE = 0;
    public int gold;
    public int age=3;
    public int dx;
    public int dy;
    public boolean nighttime=true;
    public int day= Days.FIRST_DAY_IN_THE_SHOP;

    public int visitorsRemaining = 20; // sync with GameRules.VISITORS_EACH_DAY
}
