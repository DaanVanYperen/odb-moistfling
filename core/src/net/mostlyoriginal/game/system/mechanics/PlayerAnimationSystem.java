package net.mostlyoriginal.game.system.mechanics;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.component.dialog.DialogSingleton;

/**
 * @author Daan van Yperen
 */
@All(Player.class)
public class PlayerAnimationSystem extends FluidIteratingSystem {

    private DialogSingleton dialog;

    @Override
    protected void process(E e) {
        Player player = e.getPlayer();

        String anim = "";

        switch (player.age) {
            case 0:
                anim = "player_kid";
                break;
            case 1:
                anim = "player_young_adult";
                break;
            case 2:
                anim = "player_adult";
                break;
            case 3:
                anim = "player_elderly";
                break;
        }


        if ((e.playerDx() != 0 || e.playerDy() != 0) && !e.isInDialog()) {
            anim = anim + "_walk";
            if (e.playerDx() < 0) anim = anim + "_left";
            else if (e.playerDx() > 0) anim = anim + "_right";
            else if (e.playerDy() < 0) anim = anim + "_down";
            else if (e.playerDy() > 0) anim = anim + "_up";
        }

        if (e.hasLifting()) {
            anim = anim + "_lift";
        }
        //System.out.println(anim);

        e.anim(anim);

        // for debugging until we have sprites.
    }
}
