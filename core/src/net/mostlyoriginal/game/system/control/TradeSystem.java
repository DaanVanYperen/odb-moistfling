package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.game.component.Lifter;
import net.mostlyoriginal.game.component.Shopper;
import net.mostlyoriginal.game.system.repository.ItemRepository;
import net.mostlyoriginal.game.system.mechanics.DialogSystem;
import net.mostlyoriginal.game.system.mechanics.TutorialSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;


/**
 * Allow hand-to-hand strade between player and shopper.
 *
 * @author Daan van Yperen
 */
@All({Lifter.class, Pos.class, Shopper.class})
public class TradeSystem extends FluidIteratingSystem {

    private static final int CARRIED_OBJECT_LIFTING_HEIGHT = 0;

    private PickupManager pickupManager;
    private ItemRepository itemRepository;
    private E player;
    private RenderBatchingSystem renderBatchingSystem;
    private GameScreenAssetSystem gameScreenAssetSystem;
    private DialogSystem dialogSystem;
    private TutorialSystem tutorialSystem;

    @Override
    protected void begin() {
        super.begin();
        player = E.withTag("player");
    }

    @Override
    protected void process(E patron) {
        if (playerWantsToTradeAndHasSomethingToTrade() && patronHasntTradedYet(patron)) {

            if ( patron.gridPosOverlaps(player.getGridPos())) {
                if (patron.hasDesire() && pickupManager.isCarrying(player, patron.desireDesiredItem())) {

                    final int playerItemId = player.liftingId();
                    final int patronItemId = patron.hasLifting() ? patron.liftingId() : -1;

                    if (patronItemId != -1) {

                        tutorialSystem.triggerItemTutorial(patronItemId);

                        player.liftingId(patronItemId);
                        player.getLifter().itemsLifted++;
                        player.lifterAttemptLifting(true);
                        renderBatchingSystem.sortedDirty = true;

                        final int goldValue = itemRepository.get(E.E(playerItemId).itemType()).gold;
                        if (goldValue > 0) {
                            patron.paying(goldValue);
                            gameScreenAssetSystem.playSfx("sfx_money_1");
                        }
                    }

                    if (playerItemId != -1) {
                        patron.liftingId(playerItemId);
                        patron.getLifter().itemsLifted++;
                        patron.removeDesire();
                        renderBatchingSystem.sortedDirty = true;

                        if ( E.E(playerItemId).itemType().equals("item_ring")) {
                            startFinalDialog(patron);
                        }

                    }
                } else {
                    // cancel drop if trade can't be made.
                    player.lifterAttemptLifting(true);
                }
            }
        }
    }

    private void startFinalDialog(E patron) {
        boolean isPostal = patron.getAnim().id.equals("actor_postal");
        if (isPostal) {
            dialogSystem.queue("actor_postal_face", "You picked right, lets beat up this hag!");
            dialogSystem.queue("actor_hag_face", "Wait no.");
            dialogSystem.queue("actor_postal_face", "Eat postage you cretin!");
            dialogSystem.queue(NameHelper.getActor_player_face(), "That's my sam!");
        } else {
            dialogSystem.queue("actor_hag_face", "Wow I can't believe that worked.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "Wait what?");
            dialogSystem.queue("actor_hag_face", "Nothing! Now teach me rejuvenation");
            dialogSystem.queue("actor_hag_face", "and I will show you some gyration!");
            dialogSystem.queue("actor_postal_face", "Uhhhhhhhhhhhhhh.");
            dialogSystem.queue(NameHelper.getActor_player_face(), "I think we need arbitration.");
            dialogSystem.queue("actor_postal_face", "I'm out of here!");
        }
        dialogSystem.queue(NameHelper.getActor_player_face(), "Thanks for playing!");
        dialogSystem.queue(NameHelper.getActor_player_face(), "Hope you enjoyed our game.");
        dialogSystem.queue(NameHelper.getActor_player_face(), "I sure did!");
        if ( isPostal ) {
            dialogSystem.queue("actor_hag_face", "I didn't.");
        } else {
            dialogSystem.queue("actor_hag_face", "Me too!");
        }
        E.withTag("player").playerDone(true);
    }

    private boolean patronHasntTradedYet(E patron) {
        return patron.getLifter().itemsLifted <= 2;
    }

    private boolean playerWantsToTradeAndHasSomethingToTrade() {
        return !player.lifterAttemptLifting() && player.hasLifting() && player.liftingId() != -1;
    }

}
