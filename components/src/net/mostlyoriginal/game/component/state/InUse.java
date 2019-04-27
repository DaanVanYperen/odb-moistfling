package net.mostlyoriginal.game.component.state;

import com.artemis.Component;
import com.artemis.annotations.EntityId;

/**
 * @author Daan van Yperen
 */
public class InUse extends Component {
    @EntityId
    public int userId;

    public float duration;

    public void set(int userId)
    {
        this.userId = userId;
    }
}
