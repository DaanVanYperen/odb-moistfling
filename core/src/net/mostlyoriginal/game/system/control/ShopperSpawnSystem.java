package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.ESubscription;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.Player;
import net.mostlyoriginal.game.component.Shopper;
import net.mostlyoriginal.game.component.ShopperSpawner;
import net.mostlyoriginal.game.component.dialog.DialogSingleton;
import net.mostlyoriginal.game.system.map.MapEntitySpawnerSystem;
import net.mostlyoriginal.game.system.repository.ItemManager;

/**
 * @author Daan van Yperen
 */
@All(ShopperSpawner.class)
public class ShopperSpawnSystem extends FluidIteratingSystem {

    private static final int TARGET_SHOPPER_COUNT = 3;

    @All(Shopper.class)
    private ESubscription shoppers;

    private MapEntitySpawnerSystem mapEntitySpawnerSystem;
    private ItemManager itemManager;
    private DialogSingleton dialog;

    private int lastScriptedSpawnDay = 0;
    private int spawns = 0;
    private E player;

    @Override
    protected boolean checkProcessing() {
        boolean daytime = !E.withTag("player").playerNighttime();
        if (!daytime) {
            spawns = 0;
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
            if (shopperSpawner.cooldown.ready(world.delta)) {
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
                    mapEntitySpawnerSystem.spawnShopper(e.gridPosX(), e.gridPosY(), "customer");
                    player.visitorsRemaining--;
                }
            }
        }
    }

    private boolean isShopperAtSpawner(GridPos spawnerGridPos) {
        for (E e : shoppers) {
            if (e.hasGridPos() && e.gridPosOverlaps(spawnerGridPos)) {
                return true;
            }
        }
        return false;
    }

    private boolean spawnScriptedShopper(int gridPosX, int gridPosY, int day) {


        if (day == Days.FIRST_DAY_IN_THE_SHOP) {
            Player player = E.withTag("player").getPlayer();
            if (player.visitorsRemaining > 5) {
                player.visitorsRemaining = 5;
            }
            lastScriptedSpawnDay = -1; // run this over and over.
            String desiredItem = "item_wood";
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY,
                    desiredItem, itemManager.randomReward(), "customer", 1);
            if (spawns == 1) {
                dialog.add(NameHelper.getActor_player_face(), "Patrons want to buy specific items!");
                dialog.add(NameHelper.getActor_player_face(), "All these guys want sticks!");
                dialog.add(NameHelper.getActor_player_face(), "Use space near the sticks to pick one up.");
                dialog.add(NameHelper.getActor_player_face(), "Stand at a patron and use space to trade.");
            }
            if (player.visitorsRemaining == 4) {
                dialog.add(NameHelper.getActor_player_face(), "When things slow down or out of stock,");
                dialog.add(NameHelper.getActor_player_face(), "just close the door and patrons will leave");
            }
            return true;
        }


        if (day == Days.ENCHANTED_BOW_BUYER) {
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_enchanted_bow", "item_boxed_coop", "customer", 1);
            return true;
        }

        if (day == Days.DRAGON_HEART) {
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_unichicken", "item_dragonheart", "customer", 1);
            return true;
        }

        if (day == Days.SPLINTERS_EVERYWHERE) {
            lastScriptedSpawnDay = -1; // run this over and over.
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY,
                    "item_healing_potion",
                    spawns == 4 ? "item_boxed_bush" : itemManager.randomReward(), "customer", 1);
            return true;
        }

        if (day == Days.DUNGEON_DELVED) {
            lastScriptedSpawnDay = -1; // run this over and over.
            String desiredItem = "item_bow";
            switch (MathUtils.random(0, 4)) {
                case 0:
                    desiredItem = "item_sword";
                    break;
                case 1:
                    desiredItem = "item_sword";
                    break;
                case 2:
                    desiredItem = "item_leather_armor";
                    break;
                case 3:
                    desiredItem = "item_bow";
                    break;
                case 4:
                    desiredItem = "item_boots";
                    break;
            }
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY,
                    desiredItem, itemManager.randomReward(), "customer", 1);
            return true;
        }

        if (day == Days.TRAVELING_CIRCUS) {
            lastScriptedSpawnDay = -1; // run this over and over.
            String desiredItem = "item_imp";
            switch (MathUtils.random(0, 4)) {
                case 0:
                    desiredItem = "item_hydra_chicken";
                    break;
                case 1:
                    desiredItem = "item_hydra_chicken";
                    break;
                case 2:
                    desiredItem = "item_hydra_chicken";
                    break;
                case 3:
                    desiredItem = "item_imp";
                    break;
                case 4:
                    desiredItem = "item_imp";
                    break;
            }
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY,
                    desiredItem, "item_unicorn", "customer", 1);
            return true;
        }


        if (day == Days.MAGE_COURT) {
            lastScriptedSpawnDay = -1; // run this over and over.
            String desiredItem = "item_magical_staff";
            switch (MathUtils.random(0, 4)) {
                case 0:
                    desiredItem = "item_magical_staff";
                    break;
                case 1:
                    desiredItem = "item_mana_potion";
                    break;
                case 2:
                    desiredItem = "item_mana_potion";
                    break;
                case 3:
                    desiredItem = "item_winged_boots";
                    break;
                case 4:
                    desiredItem = "item_imp";
                    break;
            }
            String rewardItem = itemManager.randomReward();
            if (spawns == 4) {
                desiredItem = "item_rainbow_armor";
                rewardItem = "item_boxed_forge";
            }

            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY,
                    desiredItem, rewardItem, "customer", 1);
            return true;
        }


        if (day == Days.MARRIAGE_NIGHT) {
            lastScriptedSpawnDay = -1; // run this over and over.
            if (spawns == 1) {
                mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY,
                        "item_ring", "item_ring", "actor_hag", 1);
            }
            if (spawns == 2) {
                mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY,
                        "item_ring", "item_ring", "actor_postal", 1);
                E.withTag("player").playerVisitorsRemaining(0);
            }
            return true;
        }


        if (day % 2 == 1) {
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_magical_staff", "item_mystical_tome", "customer", 1);
            return true;
        }

        return false;
    }

    private boolean enoughShoppers() {
        return shoppers.size() >= TARGET_SHOPPER_COUNT;
    }
}
