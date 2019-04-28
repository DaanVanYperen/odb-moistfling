package net.mostlyoriginal.game.manager;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import net.mostlyoriginal.game.component.ItemData;
import net.mostlyoriginal.game.system.ItemLibrary;

/**
 * @author Daan van Yperen
 */
public class ItemRepository extends BaseSystem {

    private ItemLibrary itemLibrary;
    private int rewardDropTotal = 0;
    private int desireDropTotal = 0;

    @Override
    protected void initialize() {
        super.initialize();
        final Json json = new Json();
        itemLibrary = json.fromJson(ItemLibrary.class, Gdx.files.internal("items.json"));
        for (ItemData item : itemLibrary.items) {
            rewardDropTotal += item.rewardChance;
            desireDropTotal += item.desireChance;
        }
    }

    @Override
    protected void processSystem() {
    }

    public ItemData get(String type) {
        ItemData byId = itemLibrary.getById(type);
        if (byId == null) throw new RuntimeException("unknown item type " + type);
        return byId;
    }

    public String randomReward() {
        boolean valid = false;

        int target = MathUtils.random(0, rewardDropTotal - 1);
        for (ItemData item : itemLibrary.items) {
            target -= item.rewardChance;
            if (target <= 0) return item.id;
        }
        return null;
    }

    public String randomDesire() {
        int target = MathUtils.random(0, desireDropTotal - 1);
        for (ItemData item : itemLibrary.items) {
            target -= item.desireChance;
            if (target <= 0) return item.id;
        }
        return null;
    }

    public String substitute(String ingredient) {
        String identicalTo = get(ingredient).identicalTo;
        return identicalTo != null ? identicalTo : ingredient;
    }
}

