package net.mostlyoriginal.game.component;

import com.artemis.Component;
import com.artemis.annotations.DelayedComponentRemoval;
import com.artemis.annotations.EntityId;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * @author Daan van Yperen
 */
@DelayedComponentRemoval
public class Boxed extends Component {
    public Body body;
}
