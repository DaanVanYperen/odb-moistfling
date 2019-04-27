package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Laser extends Component {
    public float sourceX;
    public float sourceY;
    public float targetX;
    public float targetY;

    public float charging=0;
    public float firing=0;
    public float blink;
    public boolean fired;

    public void set(float sourceX, float sourceY ) {
        this.sourceX=sourceX;
        this.sourceY=sourceY;
    }
}
