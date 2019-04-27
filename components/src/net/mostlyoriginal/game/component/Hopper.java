package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.artemis.annotations.EntityId;

/**
 * @author Daan van Yperen
 */
public class Hopper extends Component {
    @EntityId
    public int machineId = -1;

    @EntityId
    public int slottedId = -1;
}