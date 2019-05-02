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

        if (day == Days.FIRST_DAY_IN_THE_SHOP) {
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", null, "actor_hag", 0).shopperType(Shopper.Type.HAG);
            dialogSystem.queue(NameHelper.getActor_player_face(), "Enough sitting behind the geraniums!");
            dialogSystem.queue(NameHelper.getActor_player_face(), "It's been a while since I opened the shop.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "I missed this place.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "It really needs a dusting.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "...");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Who are you!?");
            dialogSystem.queue("actor_hag_face", "I am the hag with unclear motives!");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Typical haggery. What do you want?");
            dialogSystem.queue("actor_hag_face", "A grave warning I bring, master of life.");
            dialogSystem.queue("actor_hag_face", "in exactly ten days, make me your wife!");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Whaaaaaaat? Why?");
            dialogSystem.queue("actor_hag_face", "In exactly ten days, a choice I will gave");
            dialogSystem.queue("actor_hag_face", "Into my bed, or into a grave!");
            return true;
        }

        if (day == Days.ENCHANTED_BOW_BUYER) {
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", null, "actor_hag", 0).shopperType(Shopper.Type.HAG);
            dialogSystem.queue(NameHelper.getActor_player_face(), "Pfew, good thing I stocked up on sticks.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Not you again.");
            dialogSystem.queue("actor_hag_face", "Hello future husband.");
            dialogSystem.queue("actor_hag_face", "Prepare an enchanted blue bow,");
            dialogSystem.queue("actor_hag_face", "and tomorrow, riches you will know!");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Your rhymes really blow.");
            dialogSystem.queue("actor_hag_face", "They are only for show.");
            dialogSystem.queue("actor_hag_face", "Now I will go..w! g.. never mind.");

            //dialogSystem.queue(NameHelper.getActor_player_face(), "Hello i'm talking about my banana.");
            //dialogSystem.queue("actor_hag_face", "EEK EEEK. EEEEEK.");
            //dialogSystem.queue("actor_postal_face", "I'm not even here!");
            return true;
        }

        if (day == Days.DRUID_PACKAGE) {
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", "item_herb_branch", "actor_postal", 8)
                    .shopperType(Shopper.Type.POSTAL);
            dialogSystem.queue(NameHelper.getActor_player_face(), "Hi there Sam! Another delivery?");
            dialogSystem.queue("actor_postal_face", "You look.. different!");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Ate a few kids.");
            dialogSystem.queue("actor_postal_face", "... uh.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Just kidding Sam! I prefer adults.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "What did you bring me?");
            dialogSystem.queue("actor_postal_face", "Package from the druids. See ya!");
            return true;
        }

        if (day == Days.CURIOUS_IMP) {
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", "item_imp", "actor_hag", 1).shopperType(Shopper.Type.HAG);
            dialogSystem.queue(NameHelper.getActor_player_face(), "Not you again.");
            dialogSystem.queue("actor_hag_face", "My familiar is too familiar with me.");
            dialogSystem.queue("actor_hag_face", "Impish behaviour i wish not to see.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Leave it here you old hag,");
            dialogSystem.queue(NameHelper.getActor_player_face(), "I'll skin it for free!");
            dialogSystem.queue("actor_hag_face", "Teee heee heee heee. Now I will Flee!");
            return true;
        }

        if (day == Days.SPLINTERS_EVERYWHERE) {
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", null, "actor_postal", 0)
                    .shopperType(Shopper.Type.POSTAL);

            dialogSystem.queue("actor_postal_face", "...");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Don't worry Sam, I already ate.");
            dialogSystem.queue("actor_postal_face", "Ha! ha! ..");
            dialogSystem.queue(NameHelper.getActor_player_face(), "What do you need sam, spit it out.");
            dialogSystem.queue("actor_postal_face", "The druid herb delivery had a letter.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Oh.");
            dialogSystem.queue("actor_postal_face", "I found it behind the couch.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Oh.");
            dialogSystem.queue("actor_postal_face", "It reads:");
            dialogSystem.queue("actor_postal_face", "Bla bla.. Druid jousting festival");
            dialogSystem.queue("actor_postal_face", "Bla bla bla.. Bad case of splinters");
            dialogSystem.queue("actor_postal_face", "Bla bla. Herbs for potions");
            dialogSystem.queue("actor_postal_face", "Bla. Otherwise massive druid death.");
            dialogSystem.queue("actor_postal_face", "Sounds bad. Hope you didn't waste all herbs?");
            dialogSystem.queue(NameHelper.getActor_player_face(), "I might eat you after all.");
            return true;
        }

        if (day == Days.DUNGEON_DELVED) {
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", "item_boxed_forge", "actor_hag", 1).shopperType(Shopper.Type.HAG);
            dialogSystem.queue("actor_hag_face", "I opened a dungeon deep under your lawn.");
            dialogSystem.queue("actor_hag_face", "Its gates bursting forth my demonic spawn");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Please don't say burst. or spawn.");
            dialogSystem.queue("actor_hag_face", "The village will need protection from harm.");
            dialogSystem.queue("actor_hag_face", "Don't prepare anything protective or arm.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Yes, I will avoid making armor, and boots.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Or bows. And lots and lots of swords!");
            dialogSystem.queue("actor_hag_face", "Thank you my lovely. My thanks I will show.");
            dialogSystem.queue("actor_hag_face", "A magical door to the imps down below!");
            return true;
        }

        if (day == Days.TRAVELING_CIRCUS) {
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", null, "actor_postal", 0)
                    .shopperType(Shopper.Type.POSTAL);
            dialogSystem.queue("actor_postal_face", "Giants! Giants! AHHHHH.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Sam slow down, what's going on?");
            dialogSystem.queue("actor_postal_face", "A traveling troupe.");
            dialogSystem.queue("actor_postal_face", "Elephants. Clowns. EVERYWHERE!");
            dialogSystem.queue(NameHelper.getActor_player_face(), "The horror.");
            dialogSystem.queue("actor_postal_face", "They even have unicorns!!!");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Oh?");
            dialogSystem.queue("actor_postal_face", "Here, look at this poster!");
            dialogSystem.queue("actor_postal_face", "Grand opening tomorrow.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Maybe they're looking to expand..");
            return true;
        }

        if (day == Days.MAGE_COURT) { // druids need health potions. Will trade it for a health tome.
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", null, "actor_postal", 0)
                    .shopperType(Shopper.Type.POSTAL);
            dialogSystem.queue("actor_postal_face", "Can I hide in here for a while?");
            dialogSystem.queue(NameHelper.getActor_player_face(), "What now Sam.");
            dialogSystem.queue("actor_postal_face", "The Elephants collapsed the wizard tower.");
            dialogSystem.queue("actor_postal_face", "Who knew crushed elephants explode.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Sam.");
            dialogSystem.queue("actor_postal_face", "Explode like giant melons.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Sam!");
            dialogSystem.queue("actor_postal_face", "Screaming trumpets. The darnest thing.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "SAM!!!!");
            dialogSystem.queue("actor_postal_face", "Oh right. Thanks for the breather.");
            dialogSystem.queue("actor_postal_face", "Wanna get out of here anyway.");
            dialogSystem.queue("actor_postal_face", "Can see a horde of wizards heading this way.");
            dialogSystem.queue("actor_postal_face", "Probably have some trinkets to replace!");
            return true;
        }

        if (day == Days.DRAGON_HEART) { // Unicorn chicken for a dragon heart (part of the end).
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", null, "actor_hag", 1).shopperType(Shopper.Type.HAG);

            dialogSystem.queue("actor_hag_face", "Did you miss me, future husband?");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Like a headache. I got both now.");
            dialogSystem.queue("actor_hag_face", "A knight slayed a dragon a fortnite away");
            dialogSystem.queue("actor_hag_face", "Perfect reagents for love I would say!");
            dialogSystem.queue(NameHelper.getActor_player_face(), "I wish further away you could stay.");
            dialogSystem.queue("actor_hag_face", "He searched the world for a dish he faves!");
            dialogSystem.queue("actor_hag_face", "A rainbow chicken roast is what he craves!");
            dialogSystem.queue("actor_hag_face", "Prepare a chicken prismatic can be!");
            dialogSystem.queue("actor_hag_face", "And forge a ring, weddingengly!");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Stop stabbing me with bad rhymes.");

            return true;
        }

        if (day == Days.MARRIAGE_NIGHT) { // Marriage night.
            mapSpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", null, "actor_postal", 0)
                    .shopperType(Shopper.Type.POSTAL);
            dialogSystem.queue(NameHelper.getActor_player_face(), "Oh, hi Sam.");
            dialogSystem.queue("actor_postal_face", "Why so glum?");
            dialogSystem.queue(NameHelper.getActor_player_face(), "It's the hag with unclear motives...");
            dialogSystem.queue(NameHelper.getActor_player_face(), ".. She is forcing me to marry her.");
            dialogSystem.queue("actor_postal_face", "Sounds like that hag of clear motives to me.");
            dialogSystem.queue("actor_hag_face", "I'm actually pretty nice!");
            dialogSystem.queue("actor_postal_face", "What?");
            dialogSystem.queue("actor_hag_face", "I made a dungeon for him!");
            dialogSystem.queue("actor_postal_face", "Wait that was you?");
            dialogSystem.queue("actor_hag_face", "I forgot I'm not in this scene. carry on!");
            dialogSystem.queue(NameHelper.getActor_player_face(), "I don't know what to do!");
            dialogSystem.queue("actor_postal_face", "Chin up bud. You could have any man!");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Wait what?");
            dialogSystem.queue("actor_postal_face", "What?");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Huh?");
            dialogSystem.queue("actor_postal_face", "Oh look at the time, got to run, bye!");
            return true;
        }

        return false;
    }

    private boolean enoughShoppers() {
        return shoppers.getEntities().size() >= TARGET_SHOPPER_COUNT;
    }
}
