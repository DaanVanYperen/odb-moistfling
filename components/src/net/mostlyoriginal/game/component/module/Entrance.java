package net.mostlyoriginal.game.component.module;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Entrance extends Component {

    public float cooldown = 0;
    public float timeBetweenSpawns = 4;

    public int minCount;
    public int maxCount;
    public float timeBetweenSpawnsEasiest = 12;
    public float timeBetweenSpawnsHardest = 2;
    public float age = 0;
    public float maxAge = 120;
}
