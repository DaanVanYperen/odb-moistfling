package net.mostlyoriginal.game.system.box2d;

import com.artemis.E;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Joint;

/**
 * Extensions for box2d contact events.
 *
 * @author Daan van Yperen
 */
public interface BoxContactListener {
    /**
     * called for both directions, a,b and b,a.
     *
     * Warning: called from the {@link BoxPhysicsSystem} process lifecycle, delay anything that breaks that lifecycle (entity removal/creation).
     */
    void beginContact(E a, E b);
    void endContact(E a, E b);
}
