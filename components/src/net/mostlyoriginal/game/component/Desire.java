package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.artemis.annotations.EntityId;

/**
 * @author Daan van Yperen
 */
public class Desire extends Component {
    public String desiredItem;
    @EntityId public int desireIndicatorId = -1;
}
