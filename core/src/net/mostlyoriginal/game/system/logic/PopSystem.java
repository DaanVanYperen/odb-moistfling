package net.mostlyoriginal.game.system.logic;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.annotations.All;
import com.badlogic.gdx.utils.compression.lzma.Base;
import net.mostlyoriginal.game.system.box2d.BoxContactListener;
import net.mostlyoriginal.game.system.box2d.BoxPhysicsSystem;
import net.mostlyoriginal.game.system.common.FluidSystem;

/**
 * @author Daan van Yperen
 */
public class PopSystem extends BaseSystem implements BoxContactListener {

    private int toBeDeleted = -1;
    private BoxPhysicsSystem boxPhysicsSystem;

    @Override
    protected void initialize() {
        super.initialize();
        boxPhysicsSystem.register(this);
    }

    @Override
    protected void processSystem() {
        if ( toBeDeleted != -1 ) {
            E.E(toBeDeleted).deleteFromWorld();
            toBeDeleted=-1;
        }
    }

    @Override
    public void beginContact(E a, E b) {
        if ( a.hasPickup() ) {
            if ( b.isSharp() ) {
                toBeDeleted = a.id();
                E.E().playSound("water1");
            } else if ( b.hasPlayer() ) {
                toBeDeleted = a.id(); // also delete, but oxygen!
                E.E().playSound("burp");
            }
        }
    }

    @Override
    public void endContact(E a, E b) {

    }
}
