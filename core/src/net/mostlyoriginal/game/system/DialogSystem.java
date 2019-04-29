package net.mostlyoriginal.game.system;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.api.component.ui.Label;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.screen.GameScreen;
import net.mostlyoriginal.game.screen.LogoScreen;
import net.mostlyoriginal.game.system.control.PlayerControlSystem;
import net.mostlyoriginal.game.system.logic.TransitionSystem;
import net.mostlyoriginal.game.system.map.Scripts;

import java.util.LinkedList;

import static net.mostlyoriginal.api.operation.JamOperationFactory.tintBetween;

/**
 * @author Daan van Yperen
 */
public class DialogSystem extends BaseSystem {

    private NightSystem nightSystem;
    private PlayerControlSystem playerControlSystem;

    public static class Dialog {
        String faceAnim;
        String text;

        public Dialog(String faceAnim, String text) {
            this.faceAnim = faceAnim;
            this.text = text;
        }
    }

    private LinkedList<Dialog> dialogs = new LinkedList<>();

    private static final Tint DIALOG_TINT = new Tint("444444");
    private static final int BOTTOM_Y = 200 + 16;
    private static final float MIDDLE_X = (float) (GameRules.SCREEN_WIDTH / GameRules.CAMERA_ZOOM) / 2f - 16;
    private static final int LINE_HEIGHT = 12;

    private int lastGold = -1;
    private int lastDay = -1;
    private E talkLabel;
    private E faceIcon;
    private E dialogBox;
    private String targetText;
    private int revealedLength;
    private float revealCooldown;

    public boolean isDialogActive() {
        return (dialogs.size() > 0) || (targetText != null);
    }

    protected void clear() {
        dialogs.clear();
        talkLabel.invisible();
        faceIcon.invisible().tint(1f,1f,1f,0f);
        targetText=null;
        dialogBox.invisible().tint(1f,1f,1f,0f);
        E player = E.withTag("player");
        player.inDialog(false);
        if (player.playerDone() ) {
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

    public void queue(String faceAnim, String text) {
        dialogs.add(new Dialog(faceAnim, "\""+text+"\""));
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
        revealCooldown -= world.delta;
        if (revealCooldown <= 0 && targetText != null) {
            revealCooldown += 1f / 60f;
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
            nightSystem.preventAccidentalReactivation();
            preventPlayerAccidentallyClickingStuff();
        }

        if (isDialogActive() &&Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            clear();
            nightSystem.preventAccidentalReactivation();
            preventPlayerAccidentallyClickingStuff();
        }

    }

    private void preventPlayerAccidentallyClickingStuff() {
        playerControlSystem.interactCooldown=0.2f;
    }

    private void popDialog() {
        if (!dialogs.isEmpty()) {
            E.withTag("player").inDialog(true);
            Dialog pop = dialogs.pop();
            set(pop.faceAnim, pop.text);
        } else clear();
    }
}
