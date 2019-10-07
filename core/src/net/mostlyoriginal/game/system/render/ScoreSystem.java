package net.mostlyoriginal.game.system.render;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.ESubscription;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.api.operation.OperationFactory;
import net.mostlyoriginal.api.utils.Duration;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Item;
import net.mostlyoriginal.game.system.StaminaSystem;

import static net.mostlyoriginal.api.operation.JamOperationFactory.tintBetween;

/**
 * @author Daan van Yperen
 */
public class ScoreSystem extends BaseSystem {

    private static final Tint STONE_FONT_TINT = new Tint(1f, 1f, 1f, 0.8f);
    private E hintLabel;
    private E rankLabel;
    private int lastScore = -1;
    @All(Item.class)
    ESubscription items;
    private String lastLabel="";
    private E rankLabelShadow;

    private StaminaSystem staminaSystem;

    @Override
    protected void initialize() {
        super.initialize();

        //hintLabel = E.E().tag("scoreLabel").labelText("Rest on raft or eat!").tint(1f,0f,0f,0.8f).fontScale(0.4f).fontFontName("tropical").labelAlign(Label.Align.RIGHT).pos(20*16f,14*16f).renderLayer(2000);
        rankLabel = E.E().tag("scoreLabel").labelText("").tint(STONE_FONT_TINT).fontScale(1f).fontFontName("tropical").labelAlign(Label.Align.RIGHT).pos(20*16f,3*16f).renderLayer(2000);
        rankLabelShadow = E.E().tag("scoreLabel").labelText("").tint(0f,0f,0f,0.4f).fontScale(1f).fontFontName("tropical").labelAlign(Label.Align.RIGHT).pos(20*16f+1,3*16f-1).renderLayer(1999);
    }

    @Override
    protected void processSystem() {
        final E player = E.withTag("player");

        int raftSize = 0;
        int newScore = MathUtils.random(0,2000);
        int flamingos=0;
        int dogs = 0;
        int wifes = 0;
        int score=0;

        for (E item : items) {
            if ( item.isLocked() ) {
                switch ( item.itemType() ) {
                    case "item_pallet": raftSize++; break;
                    case "item_dog_placed": dogs++; break;
                    case "item_wife_placed": wifes++; break;
                    case "item_flamingo_placed": flamingos++; break;
                }
                score += item.getItemMetadata().data.score;
            }
        }

        String sizeAdjective = sizeLabel[MathUtils.clamp(raftSize, 0, sizeLabel.length - 1)];
        String suffix="";
        String prefix="";
        if ( dogs > 0 ) {
            suffix = " of the Dog";
        }
        if ( wifes > 0 ) {
            suffix = " ruled by Wife";
        }
        if ( flamingos > 0 ) {
            prefix = "Fabulous ";
        }
        String label = prefix+sizeAdjective+" raft"+suffix+" ("+score+" points)";

        //Vector3 playerPos = player.getPos().xy;
        //hintLabel.pos(playerPos.x,playerPos.y);
        //hintLabel.tint(1f,0f,0f,staminaSystem.isLowStamina()? 1f:0f);


        if (!lastLabel.equals(label) || lastScore != newScore) {
            lastLabel = label;
            lastScore = newScore;
            GameRules.lastScore = lastScore;
            rankLabel.labelText(label);
            rankLabelShadow.labelText(label);
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
