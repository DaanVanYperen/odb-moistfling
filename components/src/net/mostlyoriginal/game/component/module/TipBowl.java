package net.mostlyoriginal.game.component.module;

import com.artemis.Component;
import com.artemis.annotations.EntityId;

/**
 * @author Daan van Yperen
 */
public class TipBowl extends Component {
    @EntityId
    public int bowlId;
    public int coins=0;
    public int anger=0;
    public int maxAnger=10;
}
