package net.mostlyoriginal.game.system.mechanics;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.Player;

/**
 * @author Daan van Yperen
 */
@All(Player.class)
public class PlayerAgeSystem extends FluidIteratingSystem {
    @Override
    protected void process(E e) {
        Player player = e.getPlayer();

        switch (player.age) {
            case 0:
                e.anim("player_kid");
                break;
            case 1:
                e.anim("player_young_adult");
                break;
            case 2:
                e.anim("player_adult");
                break;
            case 3:
                e.anim("player_elderly");
                break;
        }

        // for debugging until we have sprites.
        e.tint();
        e.getTint().color.r=1f;
        e.getTint().color.g=1f;
        e.getTint().color.b=1f;
        switch (player.age) {
            case 0:
                e.getTint().color.a = 1f; break;
            case 1:
                e.getTint().color.a = 0.75f; break;
            case 2:
                e.getTint().color.a = 0.5f; break;
            case 3:
                e.getTint().color.a = 0.25f; break;
        }


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
