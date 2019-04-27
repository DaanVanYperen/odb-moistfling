package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.artemis.annotations.EntityId;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author Daan van Yperen
 */
public class ShopperSpawner extends Component {
    @EntityId public int shopperId = -1;
    public float cooldown = MathUtils.random(1f,10f);
}
