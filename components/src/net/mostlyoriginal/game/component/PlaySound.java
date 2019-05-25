package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class PlaySound extends Component {

    public String id;
    public float volume = 0.2f;

    public void set(String sfx) {
        this.id = sfx;
    }

    public void set(String sfx, float volume) {
        this.id = sfx;
        this.volume = volume;
    }
}
