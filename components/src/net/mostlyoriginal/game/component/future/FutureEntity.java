package net.mostlyoriginal.game.component.future;

import com.artemis.Component;

/**
 * Future entity.
 *
 * @author Daan van Yperen
 */
public class FutureEntity extends Component {
    public int type; // add to EntityType.
    public String subType;
    public int count=1;

    public void set(int entityType ) {
        this.type = entityType;
    }
}
