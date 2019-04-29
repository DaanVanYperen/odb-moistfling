package net.mostlyoriginal.game.system;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.game.component.AffectedByNight;
import net.mostlyoriginal.game.component.Player;

/**
 * @author Daan van Yperen
 */
@All(AffectedByNight.class)
public class NightSystem extends FluidIteratingSystem {

    private static final int CYCLE_DURATION = 30;
    private static final int CYCLE_DURATION_DAY = 45;

    private boolean nighttime = true;
    private float cooldown = CYCLE_DURATION;

    private boolean flipped = true;

    @Override
    protected void begin() {
        super.begin();
        cooldown -= world.delta;
        //if (cooldown <= 0) {
        //    toggle();
        //};
    }

    @Override
    protected void end() {
        super.end();
        flipped = false;
    }

    public void toggle() {
        E player = E.withTag("player");
        nighttime = player.playerNighttime();
        nighttime = !nighttime;
        cooldown = nighttime ? CYCLE_DURATION : CYCLE_DURATION_DAY;
        flipped = true;
        player.playerNighttime(nighttime);
        Player playerComp = player.getPlayer();
        if (nighttime) {
            playerComp.day++;
            playerComp.visitorsRemaining = MathUtils.clamp(playerComp.day, 2, 5);
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
