package net.mostlyoriginal.game.system.mechanics;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.api.util.Cooldown;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.dialog.DialogSingleton;
import net.mostlyoriginal.game.screen.LogoScreen;
import net.mostlyoriginal.game.system.control.PlayerControlSystem;
import net.mostlyoriginal.game.system.logic.TransitionSystem;
import net.mostlyoriginal.game.util.Scripts;

/**
 * @author Daan van Yperen
 */
public class DialogSystem extends BaseSystem {

    private PlayerControlSystem playerControlSystem;
    private DialogSingleton dialog;

    private static final Tint DIALOG_TINT = new Tint("444444");

    private E talkLabel;
    private E faceIcon;
    private E dialogBox;
    private String targetText;
    private int revealedLength;
    private Cooldown revealCooldown = Cooldown.withInterval(1f / 60f).autoReset(false);

    // @todo destroyyyy!
    @Deprecated
    public boolean isDialogActive() {
        return (dialog.size() > 0) || (targetText != null);
    }

    protected void clear() {
        dialog.clear();
        talkLabel.invisible();
        faceIcon.invisible().tint(1f, 1f, 1f, 0f);
        targetText = null;
        dialogBox.invisible().tint(1f, 1f, 1f, 0f);
        E player = E.withTag("player");
        player.inDialog(false);
        if (player.playerDone()) {
            world.getSystem(TransitionSystem.class).transition(LogoScreen.class, 0.55f);
        }
    }

    protected void set(String faceAnim, String text) {
        if (dialogBox.isInvisible()) {
            talkLabel.removeInvisible();
            faceIcon.removeInvisible();
            dialogBox.removeInvisible();
            dialogBox.script(Scripts.appearOverTime());
            faceIcon.script(Scripts.appearOverTime());
        }

        faceIcon.anim(faceAnim);

        targetText = text;
        talkLabel.labelText("");
        revealedLength = 0;
    }

    @Override
    protected void initialize() {
        super.initialize();

        talkLabel = E.E()
                .pos(220, 98)
                .tint(DIALOG_TINT)
                .fontFontName("5x5")
                .labelAlign(Label.Align.LEFT)
                .renderLayer(GameRules.LAYER_DIALOG_TEXT)
                .labelText("").invisible();

        faceIcon = E.E()
                .pos(180, 85).anim("player_face").renderLayer(GameRules.LAYER_DIALOG_TEXT).invisible();


        dialogBox = E.E().pos(160, 50).anim("talk_box").renderLayer(GameRules.LAYER_DIALOG_BOX).invisible();
    }

    @Override
    protected void processSystem() {
        if (!dialog.isEmpty() ) {
            E.withTag("player").inDialog(true);
        }
        if (revealCooldown.ready(world.delta) && targetText != null) {
            revealCooldown.restart();
            revealedLength++;
            if (revealedLength < targetText.length()) {
                talkLabel.labelText(targetText.substring(0, revealedLength));
            }
        }

        if (targetText == null) {
            popDialog();
        }


        if (isDialogActive() && (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT))) {
            popDialog();
            //todo nightSystem.preventAccidentalReactivation();
            preventPlayerAccidentallyClickingStuff();
        }

        if (isDialogActive() && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            clear();
            //@todo nightSystem.preventAccidentalReactivation();
            preventPlayerAccidentallyClickingStuff();
        }

    }

    private void preventPlayerAccidentallyClickingStuff() {
        playerControlSystem.interactCooldown.restart();
    }

    private void popDialog() {
        if (!dialog.isEmpty()) {
            E.withTag("player").inDialog(true);
            DialogSingleton.Dialog pop = dialog.pop();
            set(pop.faceAnim, pop.text);
        } else clear();
    }
}
