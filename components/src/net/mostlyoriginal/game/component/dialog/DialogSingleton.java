package net.mostlyoriginal.game.component.dialog;

import com.artemis.Component;
import com.artemis.annotations.Transient;
import net.mostlyoriginal.api.Singleton;

import java.util.LinkedList;

/**
 * @author Daan van Yperen
 */
@Singleton
@Transient
public class DialogSingleton extends Component {

    public static class Dialog {

        public final String faceAnim;
        public final String text;

        public Dialog(String faceAnim, String text) {
            this.faceAnim = faceAnim;
            this.text = text;
        }

    }
    private LinkedList<Dialog> conversations = new LinkedList<>();

    /**
     * Queue a message for display to the player.
     *
     * @param faceAnim Sprite with face of speaker
     * @param text Text by speaker.
     */
    public void add(String faceAnim, String text) {
        conversations.add(new Dialog(faceAnim, "\"" + text + "\""));
    }

    public void clear() {
        conversations.clear();
    }

    public int size() {
        return conversations.size();
    }

    public boolean isEmpty() {
        return conversations.isEmpty();
    }

    public Dialog pop() {
        return conversations.pop();
    }
}
