package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.EntitySubscription;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.game.component.Shopper;
import net.mostlyoriginal.game.component.ShopperSpawner;
import net.mostlyoriginal.game.system.DialogSystem;
import net.mostlyoriginal.game.system.map.MapSpawnerSystem;

/**
 * @author Daan van Yperen
 */
@All(ShopperSpawner.class)
public class NightShopperSpawnSystem extends FluidIteratingSystem {

    private static final int TARGET_SHOPPER_COUNT = 3;

    @All(Shopper.class)
    private EntitySubscription shoppers;
    MapSpawnerSystem mapSpawnerSystem;

    private float spawnCooldown = 0;
    private int lastScriptedSpawnDay = 0;
    private E player;
    private DialogSystem dialogSystem;

    @Override
    protected boolean checkProcessing() {
        return E.withTag("player").playerNighttime();
    }

    @Override
    protected void begin() {
        super.begin();
        player = E.withTag("player");

    }

    @Override
    protected void process(E e) {
        int day = E.withTag("player").playerDay();
        if ( lastScriptedSpawnDay != day ) {
            lastScriptedSpawnDay=day;
            spawnScriptedShopper(16, 6, day);
        }
    }

    private boolean spawnScriptedShopper(int gridPosX, int gridPosY, int day) {

        if (day == 1) {
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", null, "actor_hag", 0).shopperType(Shopper.Type.HAG);
            dialogSystem.queue("actor_player_face", "Who are you!?");
            dialogSystem.queue("actor_hag_face", "I am the hag with unclear motives!");
            dialogSystem.queue("actor_player_face", "Typical haggery. What do you want?");
            dialogSystem.queue("actor_hag_face", "Prepare an enchanted blue bow,");
            dialogSystem.queue("actor_hag_face", "and tomorrow, riches you will know!");
            dialogSystem.queue("actor_player_face", "Your rimes really blow.");
            dialogSystem.queue("actor_hag_face", "They are only for show.");
            dialogSystem.queue("actor_hag_face", "Now I will go..w! g.. never mind.");

            //dialogSystem.queue("actor_player_face", "Hello i'm talking about my banana.");
            //dialogSystem.queue("actor_hag_face", "EEK EEEK. EEEEEK.");
            //dialogSystem.queue("actor_postal_face", "I'm not even here!");
            return true;
        }

        if (day == 2) {
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", "item_herb_branch", "actor_postal", 5)
                    .shopperType(Shopper.Type.POSTAL);
            dialogSystem.queue("actor_player_face", "Hi there Sam! Another delivery?");
            dialogSystem.queue("actor_postal_face", "You look.. different!");
            dialogSystem.queue("actor_player_face", "Ate a few kids.");
            dialogSystem.queue("actor_postal_face", "... uh.");
            dialogSystem.queue("actor_player_face", "Just kidding Sam! I prefer adults.");
            dialogSystem.queue("actor_player_face", "What did you bring me?");
            dialogSystem.queue("actor_postal_face", "Package from the druids. See ya!");
            return true;
        }
        return false;
    }

    private boolean enoughShoppers() {
        return shoppers.getEntities().size() >= TARGET_SHOPPER_COUNT;
    }
}
