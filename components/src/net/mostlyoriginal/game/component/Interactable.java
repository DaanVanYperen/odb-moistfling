package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Interactable extends Component {
    public String startAnimId;
    public String endAnimId;
    public int useOffsetY = 0;
    public float cooldownBefore = 0;
    public float duration = 3;

    public void set(String startAnimId, String endAnimId) {
        this.startAnimId = startAnimId;
        this.endAnimId = endAnimId;
    }
}
