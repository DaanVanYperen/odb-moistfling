package net.mostlyoriginal.game.system.box2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;

/**
 * @author Daan van Yperen
 */
public interface BoxDestructionListener {
    void beforeDestroy(Body body);
    void beforeDestroy(Joint joint);
}
