package net.mostlyoriginal.game.component.action;

import com.artemis.Component;
import net.mostlyoriginal.api.util.Cooldown;

/**
 * @author Daan van Yperen
 */
public class ActionInteract extends Component {
    public Cooldown cooldown = Cooldown.withInterval(0.2f).autoReset(false);
}
