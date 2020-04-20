package net.mostlyoriginal.game.system.mechanics;

import com.artemis.BaseSystem;
import com.artemis.E;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.game.GameRules;

/**
 * @author Daan van Yperen
 */
public class LevelTimerSystem extends BaseSystem {

    private static final Tint STONE_FONT_TINT = new Tint(1f, 1f, 1f, 0.8f);
    private E rankLabel;
    private E rankLabelShadow;
    private int lastSeconds=-1;

    @Override
    protected void initialize() {
        super.initialize();
        float x = 10f;
        float y = 10f;
        rankLabel = E.E().tag("scoreLabel").labelText("test123").tint(STONE_FONT_TINT).fontScale(1f).fontFontName("5x5").labelAlign(Label.Align.LEFT).pos(x, y).renderLayer(2000);
        rankLabelShadow = E.E().tag("scoreLabel").labelText("test123").tint(0f,0f,0f,0.4f).fontScale(1f).fontFontName("5x5").labelAlign(Label.Align.LEFT).pos(x +1, y -1).renderLayer(1999);
    }
    @Override
    protected void processSystem() {
        GameRules.score.age += world.delta;
        final int seconds = (int)GameRules.score.age;
        if ( lastSeconds != seconds ) {
            lastSeconds = seconds;
            final String txt = ((lastSeconds / 60) < 10 ? "0":"")+(lastSeconds / 60) + ":" +((lastSeconds % 60) < 10 ? "0":"") +(lastSeconds % 60) ;
            rankLabel.labelText(txt);
            rankLabelShadow.labelText(txt);
        }
    }
}
