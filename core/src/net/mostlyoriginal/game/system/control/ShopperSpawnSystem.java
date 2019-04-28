package net.mostlyoriginal.game.system.control;

import com.artemis.BaseSystem;
import com.artemis.E;
import com.artemis.EntitySubscription;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.MathUtils;
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

    float spawnCooldown = 0;

    @Override
    protected boolean checkProcessing() {
        return !E.withTag("player").playerNighttime();
    }

    @Override
    protected void process(E e) {
        ShopperSpawner shopperSpawner = e.getShopperSpawner();
        if ( shopperSpawner.shopperId == -1 && !enoughShoppers()) {
            shopperSpawner.cooldown -= world.delta;
            if (shopperSpawner.cooldown < 0) {
                shopperSpawner.cooldown += MathUtils.random(5,10);
                mapSpawnerSystem.spawnShopper(e.gridPosX(),e.gridPosY());
            }
        }
    }

    private boolean enoughShoppers() {
        return shoppers.getEntities().size() >= TARGET_SHOPPER_COUNT;
    }
}
