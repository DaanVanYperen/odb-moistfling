package net.mostlyoriginal.game.system.mechanics;

import com.artemis.E;
import com.artemis.annotations.All;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.game.component.Timer;
import net.mostlyoriginal.game.system.common.FluidSystem;

/**
 * @author Daan van Yperen
 */
@All(Timer.class)
public class LevelTimerSystem extends FluidSystem {

    private static final Tint STONE_FONT_TINT = new Tint(1f, 1f, 1f, 0.8f);
    private E rankLabel;
    private E rankLabelShadow;
    private int lastSeconds=-1;

    @Override
    protected void initialize() {
        super.initialize();
        float x = 10f;
        float y = 10f;
        rankLabel = E.E().tag("scoreLabel").labelText("").tint(STONE_FONT_TINT).fontScale(1f).fontFontName("tropical").labelAlign(Label.Align.RIGHT).pos(x, y).renderLayer(2000);
        rankLabelShadow = E.E().tag("scoreLabel").labelText("").tint(0f,0f,0f,0.4f).fontScale(1f).fontFontName("tropical").labelAlign(Label.Align.RIGHT).pos(x +1, y -1).renderLayer(1999);
    }

    @Override
    protected void process(E e) {
        e.timerAge(e.timerAge() + world.delta);
        final int seconds = (int)e.timerAge();
        if ( lastSeconds != seconds ) {
            lastSeconds = seconds;
            final String txt = (lastSeconds / 60) + " " + (lastSeconds % 60);
            rankLabel.labelText(txt);
            rankLabelShadow.labelText(txt);
        }
    }
}
