package net.mostlyoriginal.game.component.action;

import com.artemis.Component;
import com.artemis.annotations.EntityId;

/**
 * @author Daan van Yperen
 */
public class ActionTrade extends Component {
    @EntityId public int target=-1;
}
