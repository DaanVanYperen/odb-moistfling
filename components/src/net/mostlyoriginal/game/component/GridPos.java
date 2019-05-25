package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class GridPos extends Component {
    public int x;
    public int y;
    public boolean deriveFromPos=false;

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(GridPos source) {
        this.x = source.x;
        this.y = source.y;
        this.deriveFromPos = source.deriveFromPos;
    }

    public boolean overlaps(GridPos gridPos) {
        return this.x == gridPos.x && this.y == gridPos.y;
    }

    public boolean overlaps(int gridX, int  gridY) {
        return this.x == gridX && this.y == gridY;
    }
}
