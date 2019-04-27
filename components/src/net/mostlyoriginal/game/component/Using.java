package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author Daan van Yperen
 */
public class Using extends Component {
    @EntityId
    public int usingId=-1;
    public float sfxCooldown= MathUtils.random(0.4f,0.8f);
    public int sfxOffset = 1;

    public void set(int usingId)
    {
        this.usingId = usingId;
    }

}
