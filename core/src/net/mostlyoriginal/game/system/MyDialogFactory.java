package net.mostlyoriginal.game.system;

import com.artemis.annotations.Wire;
import net.mostlyoriginal.game.component.dialog.DialogSingleton;
import net.mostlyoriginal.game.system.control.NameHelper;
import net.mostlyoriginal.game.system.mechanics.DialogUISystem;

/**
 * Handles assembling entities based on spawn requests.
 *
 * @author Daan van Yperen
 */
@Wire
public class MyDialogFactory implements DialogUISystem.DialogFactory {

    public static final String DIALOG_FINAL_POSTMAN = "DIALOG_FINAL_POSTMAN";
    public static final String DIALOG_FINAL_HAG = "DIALOG_FINAL_HAG";
    public static final String DIALOG_FIRST_DAY_IN_SHOP = "DIALOG_FIRST_DAY_IN_SHOP";
    public static final String DIALOG_DAY_ENCHANTED_BOW_BUYER = "DIALOG_DAY_ENCHANTED_BOW_BUYER";
    public static final String DIALOG_DAY_DRUID_PACKAGE = "DIALOG_DAY_DRUID_PACKAGE";
    public static final String DIALOG_DAY_CURIOUS_IMP = "DIALOG_DAY_CURIOUS_IMP";
    public static final String DIALOG_DAY_SPLINTERS_EVERYWHERE = "DIALOG_DAY_SPLINTERS_EVERYWHERE";
    public static final String DIALOG_DAY_DUNGEON_DELVED = "DIALOG_DAY_DUNGEON_DELVED";
    public static final String DIALOG_DAY_TRAVELING_CIRCUS = "DIALOG_DAY_TRAVELING_CIRCUS";
    public static final String DIALOG_DAY_MAGE_COURT = "DIALOG_DAY_MAGE_COURT";
    public static final String DIALOG_DAY_DRAGON_HEART = "DIALOG_DAY_DRAGON_HEART";
    public static final String DIALOG_DAY_MARRIAGE_NIGHT = "DIALOG_DAY_MARRIAGE_NIGHT";
    public static final String DIALOG_PATRONS_WANT_STICKS = "DIALOG_PATRONS_WANT_STICKS";
    public static final String DIALOG_PATRONS_DAY_SLOWING_DOWN = "DIALOG_PATRONS_DAY_SLOWING_DOWN";

    private DialogSingleton dialog;

    @Override
    public void startDialog(String dialogId) {
        switch (dialogId) {
            case DIALOG_FINAL_POSTMAN:
                startFinalDialogPostman();
                break;
            case DIALOG_FINAL_HAG:
                startFinalDialogHag();
                break;
            case DIALOG_FIRST_DAY_IN_SHOP:
                startDialogFirstDayInShop();
                break;
            case DIALOG_DAY_ENCHANTED_BOW_BUYER:
                startDialogEnchantedBowBuyer();
                break;
            case DIALOG_DAY_DRUID_PACKAGE:
                startDialogDayDruidPackage();
                break;
            case DIALOG_DAY_CURIOUS_IMP:
                startDialogDayCuriousImp();
                break;
            case DIALOG_DAY_SPLINTERS_EVERYWHERE:
                startDialogDaySplintersEverywhere();
                break;
            case DIALOG_DAY_DUNGEON_DELVED:
                startDialogDayDungeonDelved();
                break;
            case DIALOG_DAY_TRAVELING_CIRCUS:
                startDialogDayTravelingCircus();
                break;
            case DIALOG_DAY_MAGE_COURT:
                startDialogDayMageCourt();
                break;
            case DIALOG_DAY_DRAGON_HEART:
                startDialogDayDragonHeart();
                break;
            case DIALOG_DAY_MARRIAGE_NIGHT:
                startDialogDayMarriageNight();
                break;
            case DIALOG_PATRONS_WANT_STICKS:
                startDialogPatronsWantSticks();
                break;
            case DIALOG_PATRONS_DAY_SLOWING_DOWN:
                startDialogPatronsDaySlowingDown();
                break;

            default:
                throw new RuntimeException("No such dialog " + dialogId);
        }
    }

    private void startDialogPatronsDaySlowingDown() {
        dialog.add(NameHelper.getActor_player_face(), "When things slow down or out of stock,");
        dialog.add(NameHelper.getActor_player_face(), "just close the door and patrons will leave");
    }

    private void startDialogPatronsWantSticks() {
        dialog.add(NameHelper.getActor_player_face(), "Patrons want to buy specific items!");
        dialog.add(NameHelper.getActor_player_face(), "All these guys want sticks!");
        dialog.add(NameHelper.getActor_player_face(), "Use space near the sticks to pick one up.");
        dialog.add(NameHelper.getActor_player_face(), "Stand at a patron and use space to trade.");
    }

    private void startDialogDayMarriageNight() {
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
    }

    private void startDialogDayDragonHeart() {
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
    }

    private void startDialogDayMageCourt() {
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
    }

    private void startDialogDayTravelingCircus() {
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
    }

    private void startDialogDayDungeonDelved() {
        dialog.add("actor_hag_face", "I opened a dungeon deep under your lawn.");
        dialog.add("actor_hag_face", "Its gates bursting forth my demonic spawn");
        dialog.add(NameHelper.getActor_player_face(), "Please don't say burst. or spawn.");
        dialog.add("actor_hag_face", "The village will need protection from harm.");
        dialog.add("actor_hag_face", "Don't prepare anything protective or arm.");
        dialog.add(NameHelper.getActor_player_face(), "Yes, I will avoid making armor, and boots.");
        dialog.add(NameHelper.getActor_player_face(), "Or bows. And lots and lots of swords!");
        dialog.add("actor_hag_face", "Thank you my lovely. My thanks I will show.");
        dialog.add("actor_hag_face", "A magical door to the imps down below!");
    }

    private void startDialogDaySplintersEverywhere() {
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
    }

    private void startDialogDayCuriousImp() {
        dialog.add(NameHelper.getActor_player_face(), "Not you again.");
        dialog.add("actor_hag_face", "My familiar is too familiar with me.");
        dialog.add("actor_hag_face", "Impish behaviour i wish not to see.");
        dialog.add(NameHelper.getActor_player_face(), "Leave it here you old hag,");
        dialog.add(NameHelper.getActor_player_face(), "I'll skin it for free!");
        dialog.add("actor_hag_face", "Teee heee heee heee. Now I will Flee!");
    }

    private void startDialogDayDruidPackage() {
        dialog.add(NameHelper.getActor_player_face(), "Hi there Sam! Another delivery?");
        dialog.add("actor_postal_face", "You look.. different!");
        dialog.add(NameHelper.getActor_player_face(), "Ate a few kids.");
        dialog.add("actor_postal_face", "... uh.");
        dialog.add(NameHelper.getActor_player_face(), "Just kidding Sam! I prefer adults.");
        dialog.add(NameHelper.getActor_player_face(), "What did you bring me?");
        dialog.add("actor_postal_face", "Package from the druids. See ya!");
    }

    private void startDialogEnchantedBowBuyer() {
        dialog.add(NameHelper.getActor_player_face(), "Pfew, good thing I stocked up on sticks.");
        dialog.add(NameHelper.getActor_player_face(), "Not you again.");
        dialog.add("actor_hag_face", "Hello future husband.");
        dialog.add("actor_hag_face", "Prepare an enchanted blue bow,");
        dialog.add("actor_hag_face", "and tomorrow, riches you will know!");
        dialog.add(NameHelper.getActor_player_face(), "Your rhymes really blow.");
        dialog.add("actor_hag_face", "They are only for show.");
        dialog.add("actor_hag_face", "Now I will go..w! g.. never mind.");
    }

    private void startDialogFirstDayInShop() {
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
        dialog.add(NameHelper.getActor_player_face(), "...");
        dialog.add(NameHelper.getActor_player_face(), "Well that's a bummer.");
        dialog.add(NameHelper.getActor_player_face(), "No time to dwell on it.");
        dialog.add(NameHelper.getActor_player_face(), "I need some stock to deal with this.");
        dialog.add(NameHelper.getActor_player_face(), "I'll start by selling some items!");
        dialog.add(NameHelper.getActor_player_face(), "Time to open the shop.");
        dialog.add(NameHelper.getActor_player_face(), "ADSW or cursors to move, space to interact.");
        dialog.add(NameHelper.getActor_player_face(), "Space on the door to start the day.");
    }

    private void startFinalDialogPostman() {
        dialog.add("actor_postal_face", "You picked right, lets beat up this hag!");
        dialog.add("actor_hag_face", "Wait no.");
        dialog.add("actor_postal_face", "Eat postage you cretin!");
        dialog.add(NameHelper.getActor_player_face(), "That's my sam!");
        dialog.add(NameHelper.getActor_player_face(), "Thanks for playing!");
        dialog.add(NameHelper.getActor_player_face(), "Hope you enjoyed our game.");
        dialog.add(NameHelper.getActor_player_face(), "I sure did!");
        dialog.add("actor_hag_face", "I didn't.");
    }

    private void startFinalDialogHag() {
        dialog.add("actor_hag_face", "Wow I can't believe that worked.");
        dialog.add(NameHelper.getActor_player_face(), "Wait what?");
        dialog.add("actor_hag_face", "Nothing! Now teach me rejuvenation");
        dialog.add("actor_hag_face", "and I will show you some gyration!");
        dialog.add("actor_postal_face", "Uhhhhhhhhhhhhhh.");
        dialog.add(NameHelper.getActor_player_face(), "I think we need arbitration.");
        dialog.add("actor_postal_face", "I'm out of here!");
        dialog.add(NameHelper.getActor_player_face(), "Thanks for playing!");
        dialog.add(NameHelper.getActor_player_face(), "Hope you enjoyed our game.");
        dialog.add(NameHelper.getActor_player_face(), "I sure did!");
        dialog.add("actor_hag_face", "Me too!");
    }
}
