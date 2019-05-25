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
import net.mostlyoriginal.game.system.MyDialogFactory;
import net.mostlyoriginal.game.system.future.FutureSpawnUtility;
import net.mostlyoriginal.game.system.repository.ItemTypeManager;

/**
 * @author Daan van Yperen
 */
@All(ShopperSpawner.class)
public class ShopperSpawnSystem extends FluidIteratingSystem {

    private static final int TARGET_SHOPPER_COUNT = 3;

    @All(Shopper.class)
    private ESubscription shoppers;

    private ItemTypeManager itemManager;
    private DialogSingleton dialog;

    private int lastScriptedSpawnDay = 0;
    private int spawns = 0;
    private E player;

    @Override
    protected void begin() {
        super.begin();
        player = E.withTag("player");
    }

    @Override
    protected void process(E e) {
        final boolean nightTime = player.playerNighttime();
        final int day = player.playerDay();
        if (nightTime) {
            processNight(day);
            spawns = 0;
        } else {
            processDay(e, day);
        }
    }

    private void processNight(int day) {
        if (lastScriptedSpawnDay != day) {
            lastScriptedSpawnDay = day;
            spawnScriptedShopper(16, 6, day);
        }
    }

    private void processDay(E e, int day) {
        ShopperSpawner shopperSpawner = e.getShopperSpawner();
        if (shopperSpawner.shopperId == -1 && !enoughShoppers() && !isShopperAtSpawner(e.getGridPos())) {
            if (shopperSpawner.cooldown.ready(world.delta)) {
                Player player = this.player.getPlayer();
                if (player.visitorsRemaining > 0) {
                    spawns++;
                    if (lastScriptedSpawnDay != day) {
                        lastScriptedSpawnDay = day;
                        if (spawnScriptedShopperDay(e.gridPosX(), e.gridPosY(), day)) {
                            player.visitorsRemaining--;
                            return;
                        }
                    }
                    FutureSpawnUtility.shopper(e.gridPosX(), e.gridPosY(), "customer", "random", "random", 1);
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
            FutureSpawnUtility.shopper(gridPosX, gridPosY, "actor_hag", "item_talk", null, 0)
                    .shopperType(Shopper.Type.HAG).wantsToDiscuss(MyDialogFactory.DIALOG_FIRST_DAY_IN_SHOP);
            return true;
        }

        if (day == Days.ENCHANTED_BOW_BUYER) {
            FutureSpawnUtility.shopper(gridPosX, gridPosY, "actor_hag", "item_talk", null, 0).shopperType(Shopper.Type.HAG)
                    .wantsToDiscuss(MyDialogFactory.DIALOG_DAY_ENCHANTED_BOW_BUYER);
            return true;
        }

        if (day == Days.DRUID_PACKAGE) {
            FutureSpawnUtility.shopper(gridPosX, gridPosY, "actor_postal", "item_talk", "item_herb_branch", 8)
                    .shopperType(Shopper.Type.POSTAL)
                    .wantsToDiscuss(MyDialogFactory.DIALOG_DAY_DRUID_PACKAGE);
            return true;
        }

        if (day == Days.CURIOUS_IMP) {
            FutureSpawnUtility.shopper(gridPosX, gridPosY, "actor_hag", "item_talk", "item_imp", 1).shopperType(Shopper.Type.HAG)
                    .wantsToDiscuss(MyDialogFactory.DIALOG_DAY_CURIOUS_IMP);
            return true;
        }

        if (day == Days.SPLINTERS_EVERYWHERE) {
            FutureSpawnUtility.shopper(gridPosX, gridPosY, "actor_postal", "item_talk", null, 0)
                    .shopperType(Shopper.Type.POSTAL)
                    .wantsToDiscuss(MyDialogFactory.DIALOG_DAY_SPLINTERS_EVERYWHERE);
            return true;
        }

        if (day == Days.DUNGEON_DELVED) {
            FutureSpawnUtility.shopper(gridPosX, gridPosY, "actor_hag", "item_talk", "item_boxed_forge", 1).shopperType(Shopper.Type.HAG)
                    .wantsToDiscuss(MyDialogFactory.DIALOG_DAY_DUNGEON_DELVED);
            return true;
        }

        if (day == Days.TRAVELING_CIRCUS) {
            FutureSpawnUtility.shopper(gridPosX, gridPosY, "actor_postal", "item_talk", null, 0)
                    .shopperType(Shopper.Type.POSTAL)
                    .wantsToDiscuss(MyDialogFactory.DIALOG_DAY_TRAVELING_CIRCUS);
            return true;
        }

        if (day == Days.MAGE_COURT) { // druids need health potions. Will trade it for a health tome.
            FutureSpawnUtility.shopper(gridPosX, gridPosY, "actor_postal", "item_talk", null, 0)
                    .shopperType(Shopper.Type.POSTAL)
                    .wantsToDiscuss(MyDialogFactory.DIALOG_DAY_MAGE_COURT);
            return true;
        }

        if (day == Days.DRAGON_HEART) { // Unicorn chicken for a dragon heart (part of the end).
            FutureSpawnUtility.shopper(gridPosX, gridPosY, "actor_hag", "item_talk", null, 1).shopperType(Shopper.Type.HAG)
                    .wantsToDiscuss(MyDialogFactory.DIALOG_DAY_DRAGON_HEART);
            return true;
        }

        if (day == Days.MARRIAGE_NIGHT) { // Marriage night.
            FutureSpawnUtility.shopper(gridPosX, gridPosY, "actor_postal", "item_talk", null, 0)
                    .shopperType(Shopper.Type.POSTAL)
                    .wantsToDiscuss(MyDialogFactory.DIALOG_DAY_MARRIAGE_NIGHT);
            return true;
        }

        return false;
    }


    private boolean spawnScriptedShopperDay(int gridPosX, int gridPosY, int day) {

        if (day == Days.FIRST_DAY_IN_THE_SHOP) {
            Player player = E.withTag("player").getPlayer();
            if (player.visitorsRemaining > 5) {
                player.visitorsRemaining = 5;
            }
            lastScriptedSpawnDay = -1; // run this over and over.
            String desiredItem = "item_wood";
            FutureSpawnUtility.shopper(gridPosX, gridPosY,
                    "customer", desiredItem, itemManager.randomReward(), 1);
            if (spawns == 1) {
                dialog.startNextDialog = MyDialogFactory.DIALOG_PATRONS_WANT_STICKS;
            }
            if (player.visitorsRemaining == 4) {
                dialog.startNextDialog = MyDialogFactory.DIALOG_PATRONS_DAY_SLOWING_DOWN;
            }
            return true;
        }

        if (day == Days.ENCHANTED_BOW_BUYER) {
            FutureSpawnUtility.shopper(gridPosX, gridPosY, "customer", "item_enchanted_bow", "item_boxed_coop", 1);
            return true;
        }

        if (day == Days.DRAGON_HEART) {
            FutureSpawnUtility.shopper(gridPosX, gridPosY, "customer", "item_unichicken", "item_dragonheart", 1);
            return true;
        }

        if (day == Days.SPLINTERS_EVERYWHERE) {
            lastScriptedSpawnDay = -1; // run this over and over.
            FutureSpawnUtility.shopper(gridPosX, gridPosY,
                    "customer", "item_healing_potion",
                    spawns == 4 ? "item_boxed_bush" : itemManager.randomReward(), 1);
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
            FutureSpawnUtility.shopper(gridPosX, gridPosY,
                    "customer", desiredItem, itemManager.randomReward(), 1);
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
            FutureSpawnUtility.shopper(gridPosX, gridPosY,
                    "customer", desiredItem, "item_unicorn", 1);
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

            FutureSpawnUtility.shopper(gridPosX, gridPosY,
                    "customer", desiredItem, rewardItem, 1);
            return true;
        }


        if (day == Days.MARRIAGE_NIGHT) {
            lastScriptedSpawnDay = -1; // run this over and over.
            if (spawns == 1) {
                FutureSpawnUtility.shopper(gridPosX, gridPosY,
                        "actor_hag", "item_ring", "item_ring", 1);
            }
            if (spawns == 2) {
                FutureSpawnUtility.shopper(gridPosX, gridPosY,
                        "actor_postal", "item_ring", "item_ring", 1);
                E.withTag("player").playerVisitorsRemaining(0);
            }
            return true;
        }


        if (day % 2 == 1) {
            FutureSpawnUtility.shopper(gridPosX, gridPosY, "customer", "item_magical_staff", "item_mystical_tome", 1);
            return true;
        }

        return false;
    }

    private boolean enoughShoppers() {
        return shoppers.size() >= TARGET_SHOPPER_COUNT;
    }
}
