package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.game.component.Shopper;
import net.mostlyoriginal.game.component.ShopperSpawner;
import net.mostlyoriginal.game.component.dialog.DialogSingleton;
import net.mostlyoriginal.game.system.map.MapEntitySpawnerSystem;

/**
 * @author Daan van Yperen
 */
@All(ShopperSpawner.class)
public class NightShopperSpawnSystem extends FluidIteratingSystem {

    MapEntitySpawnerSystem mapEntitySpawnerSystem;

    private int lastScriptedSpawnDay = 0;
    private E player;

    private DialogSingleton dialog;

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
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", null, "actor_hag", 0).shopperType(Shopper.Type.HAG);
            dialog.add(NameHelper.getActor_player_face(), "Enough sitting behind the geraniums!");
            dialog.add(NameHelper.getActor_player_face(), "It's been a while since I opened the shop.");
            dialog.add(NameHelper.getActor_player_face(), "I missed this place.");
            dialog.add(NameHelper.getActor_player_face(), "It really needs a dusting.");
            dialog.add(NameHelper.getActor_player_face(), "...");
            dialog.add(NameHelper.getActor_player_face(), "Who are you!?");
            dialog.add("actor_hag_face", "I am the hag with unclear motives!");
            dialog.add(NameHelper.getActor_player_face(), "Typical haggery. What do you want?");
            dialog.add("actor_hag_face", "A grave warning I bring, master of life.");
            dialog.add("actor_hag_face", "in exactly ten days, make me your wife!");
            dialog.add(NameHelper.getActor_player_face(), "Whaaaaaaat? Why?");
            dialog.add("actor_hag_face", "In exactly ten days, a choice I will gave");
            dialog.add("actor_hag_face", "Into my bed, or into a grave!");
            return true;
        }

        if (day == Days.ENCHANTED_BOW_BUYER) {
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", null, "actor_hag", 0).shopperType(Shopper.Type.HAG);
            dialog.add(NameHelper.getActor_player_face(), "Pfew, good thing I stocked up on sticks.");
            dialog.add(NameHelper.getActor_player_face(), "Not you again.");
            dialog.add("actor_hag_face", "Hello future husband.");
            dialog.add("actor_hag_face", "Prepare an enchanted blue bow,");
            dialog.add("actor_hag_face", "and tomorrow, riches you will know!");
            dialog.add(NameHelper.getActor_player_face(), "Your rhymes really blow.");
            dialog.add("actor_hag_face", "They are only for show.");
            dialog.add("actor_hag_face", "Now I will go..w! g.. never mind.");

            //dialogSystem.queue(NameHelper.getActor_player_face(), "Hello i'm talking about my banana.");
            //dialogSystem.queue("actor_hag_face", "EEK EEEK. EEEEEK.");
            //dialogSystem.queue("actor_postal_face", "I'm not even here!");
            return true;
        }

        if (day == Days.DRUID_PACKAGE) {
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", "item_herb_branch", "actor_postal", 8)
                    .shopperType(Shopper.Type.POSTAL);
            dialog.add(NameHelper.getActor_player_face(), "Hi there Sam! Another delivery?");
            dialog.add("actor_postal_face", "You look.. different!");
            dialog.add(NameHelper.getActor_player_face(), "Ate a few kids.");
            dialog.add("actor_postal_face", "... uh.");
            dialog.add(NameHelper.getActor_player_face(), "Just kidding Sam! I prefer adults.");
            dialog.add(NameHelper.getActor_player_face(), "What did you bring me?");
            dialog.add("actor_postal_face", "Package from the druids. See ya!");
            return true;
        }

        if (day == Days.CURIOUS_IMP) {
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", "item_imp", "actor_hag", 1).shopperType(Shopper.Type.HAG);
            dialog.add(NameHelper.getActor_player_face(), "Not you again.");
            dialog.add("actor_hag_face", "My familiar is too familiar with me.");
            dialog.add("actor_hag_face", "Impish behaviour i wish not to see.");
            dialog.add(NameHelper.getActor_player_face(), "Leave it here you old hag,");
            dialog.add(NameHelper.getActor_player_face(), "I'll skin it for free!");
            dialog.add("actor_hag_face", "Teee heee heee heee. Now I will Flee!");
            return true;
        }

        if (day == Days.SPLINTERS_EVERYWHERE) {
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", null, "actor_postal", 0)
                    .shopperType(Shopper.Type.POSTAL);

            dialog.add("actor_postal_face", "...");
            dialog.add(NameHelper.getActor_player_face(), "Don't worry Sam, I already ate.");
            dialog.add("actor_postal_face", "Ha! ha! ..");
            dialog.add(NameHelper.getActor_player_face(), "What do you need sam, spit it out.");
            dialog.add("actor_postal_face", "The druid herb delivery had a letter.");
            dialog.add(NameHelper.getActor_player_face(), "Oh.");
            dialog.add("actor_postal_face", "I found it behind the couch.");
            dialog.add(NameHelper.getActor_player_face(), "Oh.");
            dialog.add("actor_postal_face", "It reads:");
            dialog.add("actor_postal_face", "Bla bla.. Druid jousting festival");
            dialog.add("actor_postal_face", "Bla bla bla.. Bad case of splinters");
            dialog.add("actor_postal_face", "Bla bla. Herbs for potions");
            dialog.add("actor_postal_face", "Bla. Otherwise massive druid death.");
            dialog.add("actor_postal_face", "Sounds bad. Hope you didn't waste all herbs?");
            dialog.add(NameHelper.getActor_player_face(), "I might eat you after all.");
            return true;
        }

        if (day == Days.DUNGEON_DELVED) {
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", "item_boxed_forge", "actor_hag", 1).shopperType(Shopper.Type.HAG);
            dialog.add("actor_hag_face", "I opened a dungeon deep under your lawn.");
            dialog.add("actor_hag_face", "Its gates bursting forth my demonic spawn");
            dialog.add(NameHelper.getActor_player_face(), "Please don't say burst. or spawn.");
            dialog.add("actor_hag_face", "The village will need protection from harm.");
            dialog.add("actor_hag_face", "Don't prepare anything protective or arm.");
            dialog.add(NameHelper.getActor_player_face(), "Yes, I will avoid making armor, and boots.");
            dialog.add(NameHelper.getActor_player_face(), "Or bows. And lots and lots of swords!");
            dialog.add("actor_hag_face", "Thank you my lovely. My thanks I will show.");
            dialog.add("actor_hag_face", "A magical door to the imps down below!");
            return true;
        }

        if (day == Days.TRAVELING_CIRCUS) {
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", null, "actor_postal", 0)
                    .shopperType(Shopper.Type.POSTAL);
            dialog.add("actor_postal_face", "Giants! Giants! AHHHHH.");
            dialog.add(NameHelper.getActor_player_face(), "Sam slow down, what's going on?");
            dialog.add("actor_postal_face", "A traveling troupe.");
            dialog.add("actor_postal_face", "Elephants. Clowns. EVERYWHERE!");
            dialog.add(NameHelper.getActor_player_face(), "The horror.");
            dialog.add("actor_postal_face", "They even have unicorns!!!");
            dialog.add(NameHelper.getActor_player_face(), "Oh?");
            dialog.add("actor_postal_face", "Here, look at this poster!");
            dialog.add("actor_postal_face", "Grand opening tomorrow.");
            dialog.add(NameHelper.getActor_player_face(), "Maybe they're looking to expand..");
            return true;
        }

        if (day == Days.MAGE_COURT) { // druids need health potions. Will trade it for a health tome.
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", null, "actor_postal", 0)
                    .shopperType(Shopper.Type.POSTAL);
            dialog.add("actor_postal_face", "Can I hide in here for a while?");
            dialog.add(NameHelper.getActor_player_face(), "What now Sam.");
            dialog.add("actor_postal_face", "The Elephants collapsed the wizard tower.");
            dialog.add("actor_postal_face", "Who knew crushed elephants explode.");
            dialog.add(NameHelper.getActor_player_face(), "Sam.");
            dialog.add("actor_postal_face", "Explode like giant melons.");
            dialog.add(NameHelper.getActor_player_face(), "Sam!");
            dialog.add("actor_postal_face", "Screaming trumpets. The darnest thing.");
            dialog.add(NameHelper.getActor_player_face(), "SAM!!!!");
            dialog.add("actor_postal_face", "Oh right. Thanks for the breather.");
            dialog.add("actor_postal_face", "Wanna get out of here anyway.");
            dialog.add("actor_postal_face", "Can see a horde of wizards heading this way.");
            dialog.add("actor_postal_face", "Probably have some trinkets to replace!");
            return true;
        }

        if (day == Days.DRAGON_HEART) { // Unicorn chicken for a dragon heart (part of the end).
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", null, "actor_hag", 1).shopperType(Shopper.Type.HAG);

            dialog.add("actor_hag_face", "Did you miss me, future husband?");
            dialog.add(NameHelper.getActor_player_face(), "Like a headache. I got both now.");
            dialog.add("actor_hag_face", "A knight slayed a dragon a fortnite away");
            dialog.add("actor_hag_face", "Perfect reagents for love I would say!");
            dialog.add(NameHelper.getActor_player_face(), "I wish further away you could stay.");
            dialog.add("actor_hag_face", "He searched the world for a dish he faves!");
            dialog.add("actor_hag_face", "A rainbow chicken roast is what he craves!");
            dialog.add("actor_hag_face", "Prepare a chicken prismatic can be!");
            dialog.add("actor_hag_face", "And forge a ring, weddingengly!");
            dialog.add(NameHelper.getActor_player_face(), "Stop stabbing me with bad rhymes.");

            return true;
        }

        if (day == Days.MARRIAGE_NIGHT) { // Marriage night.
            mapEntitySpawnerSystem.spawnShopperWithSpecificItems(gridPosX, gridPosY, "item_talk", null, "actor_postal", 0)
                    .shopperType(Shopper.Type.POSTAL);
            dialog.add(NameHelper.getActor_player_face(), "Oh, hi Sam.");
            dialog.add("actor_postal_face", "Why so glum?");
            dialog.add(NameHelper.getActor_player_face(), "It's the hag with unclear motives...");
            dialog.add(NameHelper.getActor_player_face(), ".. She is forcing me to marry her.");
            dialog.add("actor_postal_face", "Sounds like that hag of clear motives to me.");
            dialog.add("actor_hag_face", "I'm actually pretty nice!");
            dialog.add("actor_postal_face", "What?");
            dialog.add("actor_hag_face", "I made a dungeon for him!");
            dialog.add("actor_postal_face", "Wait that was you?");
            dialog.add("actor_hag_face", "I forgot I'm not in this scene. carry on!");
            dialog.add(NameHelper.getActor_player_face(), "I don't know what to do!");
            dialog.add("actor_postal_face", "Chin up bud. You could have any man!");
            dialog.add(NameHelper.getActor_player_face(), "Wait what?");
            dialog.add("actor_postal_face", "What?");
            dialog.add(NameHelper.getActor_player_face(), "Huh?");
            dialog.add("actor_postal_face", "Oh look at the time, got to run, bye!");
            return true;
        }

        return false;
    }

}
