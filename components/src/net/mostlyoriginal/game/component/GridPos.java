package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class GridPos extends Component {
    public int x;
    public int y;

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(GridPos source) {
        this.x = source.x;
        this.y = source.y;
    }

    public boolean overlaps(GridPos gridPos) {
        return this.x == gridPos.x && this.y == gridPos.y;
    }
}
