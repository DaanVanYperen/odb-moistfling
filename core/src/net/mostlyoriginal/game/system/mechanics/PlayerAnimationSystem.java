package net.mostlyoriginal.game.system.mechanics;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.game.component.Player;

/**
 * @author Daan van Yperen
 */
@All(Player.class)
public class PlayerAnimationSystem extends FluidIteratingSystem {

    @Override
    protected void process(E e) {
        Player player = e.getPlayer();

        String anim = e.hasSwimming() ? "player_swim_idle" : "player_idle";

        if ((e.playerDx() != 0 || e.playerDy() != 0) && !e.isInDialog()) {
            anim =  e.hasSwimming() ? "player_swim": "player_walk";
            if (e.playerDx() < 0) anim = anim + "_left";
            else if (e.playerDx() > 0) anim = anim + "_right";
            else if (e.playerDy() < 0) anim = anim + "_down";
            else if (e.playerDy() > 0) anim = anim + "_up";
        }

        if (e.hasHolding()) {
            anim = anim + "_carry";
        }

        //System.out.println(anim);

        e.anim(anim);

        // for debugging until we have sprites.
    }
}
