package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.artemis.annotations.EntityId;
import net.mostlyoriginal.api.util.Cooldown;

import static net.mostlyoriginal.api.utils.Duration.seconds;

/**
 * @author Daan van Yperen
 */
public class ShopperSpawner extends Component {
    @EntityId public int shopperId = -1;
    public Cooldown cooldown = Cooldown.withInterval(seconds(8));
}
