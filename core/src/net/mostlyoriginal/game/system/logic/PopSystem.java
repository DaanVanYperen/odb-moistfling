package net.mostlyoriginal.game.system.logic;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.annotations.All;
import com.badlogic.gdx.utils.compression.lzma.Base;
import net.mostlyoriginal.game.component.Pickup;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.system.box2d.BoxContactListener;
import net.mostlyoriginal.game.system.box2d.BoxPhysicsSystem;
import net.mostlyoriginal.game.system.common.FluidSystem;

/**
 * @author Daan van Yperen
 */
public class PopSystem extends BaseSystem implements BoxContactListener {

    private int toBeDeleted = -1;
    private BoxPhysicsSystem boxPhysicsSystem;
    private TransitionSystem transitionSystem;

    @Override
    protected void initialize() {
        super.initialize();
        boxPhysicsSystem.register(this);
    }

    @Override
    protected void processSystem() {
        if ( toBeDeleted != -1 ) {
            E e = E.E(toBeDeleted);
            if (e.hasPickup() && e.pickupType() == Pickup.Type.EXIT) {
                transitionSystem.transition(GameScreen.class,0);
            }
            e.deleteFromWorld();
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
                b.oxygenIncrease();
            }
        }
    }

    @Override
    public void endContact(E a, E b) {

    }
}
