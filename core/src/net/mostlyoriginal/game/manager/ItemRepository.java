package net.mostlyoriginal.game.manager;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.game.component.ItemData;
import net.mostlyoriginal.game.system.ItemLibrary;

/**
 * @author Daan van Yperen
 */
public class ItemRepository extends BaseSystem {

    private ItemLibrary itemLibrary;

    @Override
    protected void initialize() {
        super.initialize();
        final Json json = new Json();
        itemLibrary = json.fromJson(ItemLibrary.class, Gdx.files.internal("items.json"));
    }

    @Override
    protected void processSystem() {
    }

    public ItemData get(String type) {
        return itemLibrary.getById(type);
    }
}
