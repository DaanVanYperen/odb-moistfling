package net.mostlyoriginal.game.system;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.api.utils.Duration;
import net.mostlyoriginal.game.GameRules;

import static net.mostlyoriginal.api.operation.JamOperationFactory.*;

/**
 * @author Daan van Yperen
 */
public class ScoreSystem extends BaseSystem {

    private static final Tint STONE_FONT_TINT = new Tint("798292");
    private static final int BOTTOM_Y = 200 + 16;
    private static final float MIDDLE_X = (float) (GameRules.SCREEN_WIDTH / GameRules.CAMERA_ZOOM) / 2f - 16;
    private static final int LINE_HEIGHT = 12;
    private E goldLabel;
    private E hintLabel;
    private E rankLabel;
    private int lastGold;
    private int lastDay;

    @Override
    protected void initialize() {
        super.initialize();

        goldLabel = E.E()
                .pos(MIDDLE_X, BOTTOM_Y)
                .tint(STONE_FONT_TINT)
                .fontFontName("5x5")
                .labelAlign(Label.Align.RIGHT)
                .renderLayer(GameRules.LAYER_SCORE_TEXT)
                .labelText("Banked: 5.000.000 Gold");


        hintLabel = E.E()
                .pos(MIDDLE_X, BOTTOM_Y + LINE_HEIGHT)
                .tint(STONE_FONT_TINT)
                .fontFontName("5x5")
                .labelAlign(Label.Align.RIGHT)
                .renderLayer(GameRules.LAYER_SCORE_TEXT)
                .labelText(randomDaytimeHint());

        rankLabel = E.E()
                .pos(MIDDLE_X, BOTTOM_Y + LINE_HEIGHT * 2)
                .tint(STONE_FONT_TINT)
                .fontFontName("5x5")
                .labelAlign(Label.Align.RIGHT)
                .renderLayer(GameRules.LAYER_SCORE_TEXT)
                .labelText(dayLabel(10));

        flash(hintLabel);
    }

    private String dayLabel(int day) {
        return "Day "+ day +": Elderly Grubling";
    }

    private boolean nighttimeMode = false;

    @Override
    protected void processSystem() {
        final E player = E.withTag("player");

        if (lastGold != player.playerGold()) {
            lastGold = player.playerGold();
            goldLabel.labelText("Banked " + player.playerGold() + " gold");
            flash(goldLabel);
        }

        if (lastDay != player.playerDay()) {
            lastDay = player.playerDay();
            rankLabel.labelText(dayLabel(lastDay));
            flash(rankLabel);
        }

        if (player.playerNighttime()) {
            if (!nighttimeMode) {
                hintLabel.labelText(randomNighttimeHint());
                flash(hintLabel);
            }
            nighttimeMode = true;
        } else {
            if (nighttimeMode) {
                if (!nighttimeMode) {
                    hintLabel
                            .labelText(randomDaytimeHint());
                    flash(hintLabel);
                }
            }
            nighttimeMode = false;
        }
    }

    private void flash(E fieldLabel) {
        fieldLabel.script(
                OperationFactory.sequence(
                        tintBetween(STONE_FONT_TINT, Tint.WHITE, 2f),
                        OperationFactory.delay(Duration.seconds(1)),
                        tintBetween(Tint.WHITE,STONE_FONT_TINT, 2f)
                ));
    }

    String[] daytimeHints = {
            "Space to pick up items!",
            "Give patrons what they desire!",
            "Rare items sell for more!",
            "Enchant with your life force!",
            "Selling earns you gold!",
    };

    private String randomDaytimeHint() {
        return daytimeHints[MathUtils.random(0, daytimeHints.length - 1)];
    }


    String[] nighttimeHints = {
            "Craft during the night!",
            "Stock up as best you can!",
            "Patrons crave magical items!",
            "Craft upgrades with tomes!",
            "Craft a coop for free chicks!",
            "Craft a forge for free ingots!",
            "Craft a bush for sticks!",
            "Experiment with ingredients!",
            "Enchant with your life force!",
            "Create a varied stock!",
    };

    private String randomNighttimeHint() {
        return nighttimeHints[MathUtils.random(0, nighttimeHints.length - 1)];
    }
}
