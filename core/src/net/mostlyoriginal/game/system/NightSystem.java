package net.mostlyoriginal.game.system;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.Interpolation;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.AffectedByNight;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.api.util.Cooldown;

import static net.mostlyoriginal.api.utils.Duration.seconds;

/**
 * @author Daan van Yperen
 */
@All(AffectedByNight.class)
public class NightSystem extends FluidIteratingSystem {

    private boolean nighttime = true;
    private boolean flipped = true;
    private Cooldown cooldown = Cooldown.withInterval(seconds(1));

    @Override
    protected void begin() {
        super.begin();
        cooldown.decreaseBy(world.delta);
    }

    @Override
    protected void end() {
        super.end();
        flipped = false;
    }

    public void toggle() {
        if (cooldown.ready()) {
            E player = E.withTag("player");
            nighttime = player.playerNighttime();
            nighttime = !nighttime;
            flipped = true;
            player.playerNighttime(nighttime);
            Player playerComp = player.getPlayer();
            if (nighttime) {
                playerComp.day++;
                playerComp.visitorsRemaining = GameRules.VISITORS_EACH_DAY;
            }
        }
    }

    @Override
    protected void process(E e) {
        if (flipped) {
            boolean fade = (e.getAffectedByNight().visibleAt == AffectedByNight.Moment.NIGHT) != nighttime;

            if (fade) {
                e.script(JamOperationFactory.tintBetween(e.getTint(), Tint.TRANSPARENT, e.affectedByNightDuration(), Interpolation.fade));
            } else {
                e.script(JamOperationFactory.tintBetween(e.getTint(), Tint.WHITE, e.affectedByNightDuration(), Interpolation.fade));
            }
        }
    }
}
