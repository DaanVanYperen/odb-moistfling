package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.artemis.annotations.DelayedComponentRemoval;
import com.artemis.annotations.EntityId;

/**
 * @author Daan van Yperen
 */
@DelayedComponentRemoval
public class CastsShadow extends Component {
    @EntityId public int shadowId =-1;
    public int yOffset=0;
}
