package net.mostlyoriginal.game.system.render;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.ESubscription;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.api.utils.Duration;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Item;

import static net.mostlyoriginal.api.operation.JamOperationFactory.tintBetween;

/**
 * @author Daan van Yperen
 */
public class ScoreSystem extends BaseSystem {

    private static final Tint STONE_FONT_TINT = new Tint(0f, 0f, 0f, 0.4f);
    private E hintLabel;
    private E rankLabel;
    private int lastScore = -1;

    @All(Item.class)
    ESubscription items;
    private String lastLabel="";

    @Override
    protected void initialize() {
        super.initialize();

        rankLabel = E.E().tag("scoreLabel").labelText("").tint(STONE_FONT_TINT).fontScale(0.5f).fontFontName("tropical").labelAlign(Label.Align.RIGHT).pos(20*16f,2*16f).renderLayer(2000);
    }

    @Override
    protected void processSystem() {
        final E player = E.withTag("player");

        int raftSize = 0;
        int newScore = MathUtils.random(0,2000);
        int dogs = 0;
        int score=0;

        for (E item : items) {
            if ( item.isLocked() ) {
                switch ( item.itemType() ) {
                    case "item_pallet": raftSize++; break;
                    case "item_dog_placed": dogs++; break;
                }
                score += item.getItemMetadata().data.score;
            }
        }

        String sizeAdjective = sizeLabel[MathUtils.clamp(raftSize, 0, sizeLabel.length - 1)];
        String suffix="";
        if ( dogs > 0 ) {
            suffix = " of the Dog";
        }
        String label = sizeAdjective+" raft"+suffix+" ("+score+" points)";

        if (!lastLabel.equals(label) || lastScore != newScore) {
            lastLabel = label;
            lastScore = newScore;
            GameRules.lastScore = lastScore;
            rankLabel.labelText(label);
            flash(rankLabel);
        }

    }

    private String[] sizeLabel= {
            "Missing",
            "Puny",
            "Baby",
            "Feeble",
            "Scraggy",
            "Teeny-Tiny",
            "Tiny",
            "Pint-size",
            "Trifling",
            "Tubby",
            "Average",
            "Great",
            "Sizable",
            "Beefy",
            "Brawny",
            "Vast",
            "Massive",
            "Hulking",
            "Enormous",
            "Titanic",
            "Gigantic",
            "Colossal"
    };

    private void flash(E fieldLabel) {
        fieldLabel.script(
                OperationFactory.sequence(
                        tintBetween(STONE_FONT_TINT, Tint.WHITE, 2f),
                        OperationFactory.delay(Duration.seconds(1)),
                        tintBetween(Tint.WHITE, STONE_FONT_TINT, 2f)
                ));
    }
}
