package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.EntitySubscription;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.component.Shopper;
import net.mostlyoriginal.game.component.ShopperSpawner;
import net.mostlyoriginal.game.manager.ItemRepository;
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
    private int spawns = 0;
    private E player;
    private ItemRepository itemRepository;

    @Override
    protected boolean checkProcessing() {
        boolean daytime = !E.withTag("player").playerNighttime();
        if ( !daytime ) {
            spawns=0;
        }
        return daytime;
    }

    @Override
    protected void begin() {
        super.begin();
        player = E.withTag("player");
    }

    @Override
    protected void process(E e) {
        ShopperSpawner shopperSpawner = e.getShopperSpawner();
        if (shopperSpawner.shopperId == -1 && !enoughShoppers() && !isShopperAtSpawner(e.getGridPos())) {
            shopperSpawner.cooldown -= world.delta;
            if (shopperSpawner.cooldown < 0) {
                shopperSpawner.cooldown += 8;

                Player player = this.player.getPlayer();
                if (player.visitorsRemaining > 0) {
                    spawns++;
                    int day = E.withTag("player").playerDay();
                    if (lastScriptedSpawnDay != day) {
                        lastScriptedSpawnDay = day;
                        if (spawnScriptedShopper(e.gridPosX(), e.gridPosY(), day)) {
                            player.visitorsRemaining--;
                            return;
                        }
                    }
                    mapSpawnerSystem.spawnShopper(e.gridPosX(), e.gridPosY(), "customer");
                    player.visitorsRemaining--;
                }
            }
        }
    }

    private boolean isShopperAtSpawner(GridPos spawnerGridPos) {
        IntBag entities = shoppers.getEntities();
        int[] data = entities.getData();
        for (int i = 0, s = entities.size(); i < s; i++) {
            E e = E.E(data[i]);
            if (e.hasGridPos() && e.gridPosOverlaps(spawnerGridPos)) {
                return true;
            }
        }
        return false;
    }

    private boolean spawnScriptedShopper(int gridPosX, int gridPosY, int day) {

        if (day == Days.ENCHANTED_BOW_BUYER) {
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_enchanted_bow", "item_boxed_coop", "customer", 1);
            return true;
        }

        if (day == Days.DRAGON_HEART) {
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_unichicken", "item_dragonheart", "customer", 1);
            return true;
        }

        if (day == Days.SPLINTERS_EVERYWHERE) {
            lastScriptedSpawnDay=-1; // run this over and over.
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY,
                    "item_healing_potion",
                    spawns == 4 ?  "item_boxed_bush" : itemRepository.randomReward(), "customer", 1);
            return true;
        }

        if (day == Days.DUNGEON_DELVED) {
            lastScriptedSpawnDay=-1; // run this over and over.
            String desiredItem = "item_bow";
            switch (MathUtils.random(0,4)) {
                case 0: desiredItem = "item_sword"; break;
                case 1: desiredItem = "item_sword"; break;
                case 2: desiredItem = "item_leather_armor"; break;
                case 3: desiredItem = "item_bow"; break;
                case 4: desiredItem = "item_boots"; break;
            }
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY,
                    desiredItem,itemRepository.randomReward() , "customer", 1);
            return true;
        }

        if (day == Days.TRAVELING_CIRCUS) {
            lastScriptedSpawnDay=-1; // run this over and over.
            String desiredItem = "item_imp";
            switch (MathUtils.random(0,4)) {
                case 0: desiredItem = "item_hydra_chicken"; break;
                case 1: desiredItem = "item_hydra_chicken"; break;
                case 2: desiredItem = "item_hydra_chicken"; break;
                case 3: desiredItem = "item_imp"; break;
                case 4: desiredItem = "item_imp"; break;
            }
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY,
                    desiredItem, "item_unicorn", "customer", 1);
            return true;
        }

        if (day % 2 == 1) {
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_magical_staff", "item_mystical_tome", "customer", 1);
            return true;
        }
//
//        if (day == 3) {
//            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_enchanted_armor", "item_boxed_forge", "customer", 1);
//            return true;
//        }
//
//        if (day == 3) {
//            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_enchanted_armor", "item_boxed_forge", "customer", 1);
//            return true;
//        }


        return false;
    }

    private boolean enoughShoppers() {
        return shoppers.getEntities().size() >= TARGET_SHOPPER_COUNT;
    }
}
