package net.mostlyoriginal.game.system.mechanics;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.badlogic.gdx.math.Interpolation;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.api.operation.JamOperationFactory;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.api.operation.common.Operation;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.screen.GameScreen;

import static net.mostlyoriginal.api.utils.Duration.seconds;

/**
 * @author Daan van Yperen
 */
public class LevelTimerSystem extends BaseSystem {

    private static final Tint STONE_FONT_TINT = new Tint(1f, 1f, 1f, 0.8f);
    public String mapName;
    private E rankLabel;
    private E rankLabelShadow;
    private E titleLabel;
    private E titleLabelShadow;
    private int lastSeconds=-1;

    @Override
    protected void initialize() {
        super.initialize();
        float x = 10f;
        float y = 10f;
        rankLabel = E.E().tag("scoreLabel").labelText("test123").tint(STONE_FONT_TINT).fontScale(1f).fontFontName("5x5").labelAlign(Label.Align.LEFT).pos(x, y).renderLayer(2000);
        rankLabelShadow = E.E().tag("scoreLabel").labelText("test123").tint(0f,0f,0f,0.4f).fontScale(1f).fontFontName("5x5").labelAlign(Label.Align.LEFT).pos(x +1, y -1).renderLayer(1999);
        x = GameRules.SCREEN_WIDTH/4;
        y = 120f;
        titleLabel = E.E().tag("titleLabel").labelText(mapName).tint(STONE_FONT_TINT).fontScale(4f).fontFontName("5x5")
                .labelAlign(Label.Align.RIGHT).pos(x, y)
                .renderLayer(2000)
                .script(OperationFactory.sequence(
                        JamOperationFactory.tintBetween(Tint.TRANSPARENT,Tint.WHITE,seconds(0.5f), Interpolation.exp5In),
                        OperationFactory.delay(5.0f),
                        JamOperationFactory.tintBetween(Tint.WHITE,Tint.TRANSPARENT,seconds(1f), Interpolation.exp5Out)
                ));
        titleLabelShadow = E.E().tag("titleLabel").labelText(mapName).tint(0f,0f,0f,0.4f).fontScale(4f).fontFontName("5x5")
                .labelAlign(Label.Align.RIGHT).pos(x +1, y -1)
                .renderLayer(1999)
                .script(OperationFactory.sequence(
                        JamOperationFactory.tintBetween(Tint.TRANSPARENT,new Tint(0f,0f,0f,0.4f),seconds(0.5f), Interpolation.exp5In),
                        OperationFactory.delay(5.0f),
                        JamOperationFactory.tintBetween(new Tint(0f,0f,0f,0.4f),Tint.TRANSPARENT,seconds(1f), Interpolation.exp5Out)
                ));
    }
    @Override
    protected void processSystem() {
        GameRules.score.age += world.delta;
        final int seconds = (int)GameRules.score.age;
        if ( lastSeconds != seconds ) {
            lastSeconds = seconds;
            final String txt = mapName + " (" + ((lastSeconds / 60) < 10 ? "0":"")+(lastSeconds / 60) + ":" +((lastSeconds % 60) < 10 ? "0":"") +(lastSeconds % 60) + ")" ;
            rankLabel.labelText(txt);
            rankLabelShadow.labelText(txt);
        }
    }
}
