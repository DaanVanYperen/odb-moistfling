package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Player extends Component {

    public enum Tool {
        PLUNGER(0.75f),
        MOP(0.75f);

        public final float multiplier;

        Tool(float multiplier ) {

            this.multiplier = multiplier;
        }
    }

    public Tool tool = Tool.PLUNGER;

    public int moduleIndex = 1;
    public int activeModuleId = -1;

    public void nextTool() {
        tool = Tool.values()[(tool.ordinal()+1) % Tool.values().length];
    }
}
