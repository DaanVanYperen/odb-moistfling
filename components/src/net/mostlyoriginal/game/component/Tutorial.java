package net.mostlyoriginal.game.component;

import com.artemis.Component;

import static net.mostlyoriginal.game.component.Tutorial.Step.*;

/**
 * @author Daan van Yperen
 */
public class Tutorial extends Component {

    public enum Step {
        PLUNGE_TOILET,
        GRAB_MOP,
        MOP_TOILET,
        DONE
    }

    public Step step = PLUNGE_TOILET;

    public void next() {
        switch (step) {
            case PLUNGE_TOILET:
                step = GRAB_MOP;
                break;
            case GRAB_MOP:
                step = MOP_TOILET;
                break;
            case MOP_TOILET:
                step = DONE;
                break;
            case DONE:
                break;
        }
    }
}
