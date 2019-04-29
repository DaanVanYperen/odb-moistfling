package net.mostlyoriginal.game.system.control;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.EntitySubscription;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.Shopper;
import net.mostlyoriginal.game.component.ShopperSpawner;
import net.mostlyoriginal.game.system.map.MapSpawnerSystem;

/**
 * @author Daan van Yperen
 */
@All(ShopperSpawner.class)
public class ShopperSpawnSystem extends FluidIteratingSystem {

    private static final int TARGET_SHOPPER_COUNT = 3;

    @All(Shopper.class)
    private EntitySubscription shoppers;
    MapSpawnerSystem mapSpawnerSystem;

    private float spawnCooldown = 0;
    private int lastScriptedSpawnDay = 0;

    @Override
    protected boolean checkProcessing() {
        return !E.withTag("player").playerNighttime();
    }

    @Override
    protected void process(E e) {
        ShopperSpawner shopperSpawner = e.getShopperSpawner();
            if ( shopperSpawner.shopperId == -1 && !enoughShoppers() && !isShopperAtSpawner(e.getGridPos())) {
            shopperSpawner.cooldown -= world.delta;
            if (shopperSpawner.cooldown < 0) {
                shopperSpawner.cooldown += MathUtils.random(5,10);
                int day = E.withTag("player").playerDay();
                if ( lastScriptedSpawnDay != day) {
                    lastScriptedSpawnDay = day;
                    if ( spawnScriptedShopper(e.gridPosX(),e.gridPosY(),day))
                        return;
                }
                mapSpawnerSystem.spawnShopper(e.gridPosX(),e.gridPosY());
            }
        }
    }

    private boolean isShopperAtSpawner(GridPos spawnerGridPos) {
        IntBag entities = shoppers.getEntities();
        int[] data = entities.getData();
        for (int i = 0, s = entities.size(); i < s; i++) {
            E e = E.E(data[i]);
            if ( e.hasGridPos() && e.gridPosOverlaps(spawnerGridPos) ) {
                return true;
            }
        }
        return false;
    }

    private boolean spawnScriptedShopper(int gridPosX, int gridPosY, int day) {

        if ( day == 1 ) {
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_enchanted_bow","item_boxed_coop");
            return true;
        }

        if ( day == 2 ) {
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_magical_sword","item_unicorn");
            return true;
        }

        if ( day == 3 ) {
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_enchanted_armor","item_boxed_forge");
            return true;
        }

        if ( day % 5 == 4 ) {
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_magical_staff","item_mystical_tome");
            return true;
        }

        return false;
    }

    private boolean enoughShoppers() {
        return shoppers.getEntities().size() >= TARGET_SHOPPER_COUNT;
    }
}
