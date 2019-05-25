package net.mostlyoriginal.game.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 * @todo ideally ItemData would be directly exposed. We can't do that yet cause we load it from Json.
 * @todo maybe merge with item.
 */
public class ItemMetadata extends Component {
    public ItemData data;
}
