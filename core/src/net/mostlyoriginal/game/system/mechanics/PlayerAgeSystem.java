package net.mostlyoriginal.game.system.mechanics;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.system.DialogSystem;

/**
 * @author Daan van Yperen
 */
@All(Player.class)
public class PlayerAgeSystem extends FluidIteratingSystem {
    private DialogSystem dialogSystem;

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


        if ( (e.playerDx() != 0 || e.playerDy() != 0) && !dialogSystem.isDialogActive() ) {
            anim = anim + "_walk";
            if ( e.playerDx() < 0 ) anim = anim + "_left" ;
            else if ( e.playerDx() > 0 ) anim = anim + "_right";
            else if ( e.playerDy() < 0 ) anim = anim + "_down";
            else if ( e.playerDy() > 0 ) anim = anim + "_up";
        }

        if ( e.hasLifting() ) {
            anim = anim + "_lift";
        }
        //System.out.println(anim);

        e.anim(anim);

        // for debugging until we have sprites.
    }

    public boolean attemptPayment(int ageCost) {
        Player player = E.withTag("player").getPlayer();
        if ( ageCost < 0 ) {
            // de-age
            player.age = MathUtils.clamp(player.age + ageCost ,Player.MIN_AGE, Player.MAX_AGE);
            return true;
        }
        if ( player.age + ageCost <= Player.MAX_AGE ) {
            // age!
            player.age += ageCost;
            return true;
        }
        return false;
    }
}
