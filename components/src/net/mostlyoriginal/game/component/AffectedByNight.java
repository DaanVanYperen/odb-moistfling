package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class AffectedByNight extends Component {
    public enum Moment {
        DAY,
        NIGHT
    };
    public Moment visibleAt = Moment.DAY;
    public float duration = 2f;
}
