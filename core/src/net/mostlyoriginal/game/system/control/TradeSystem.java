package net.mostlyoriginal.game.system.control;

import com.artemis.E;
import com.artemis.FluidIteratingSystem;
import com.artemis.annotations.All;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Lifter;
import net.mostlyoriginal.game.component.Shopper;
import net.mostlyoriginal.game.manager.ItemRepository;
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
                    }
                } else {
                    // cancel drop if trade can't be made.
                    player.lifterAttemptLifting(true);
                }
            }
        }
    }

    private boolean patronHasntTradedYet(E patron) {
        return patron.getLifter().itemsLifted <= 2;
    }

    private boolean playerWantsToTradeAndHasSomethingToTrade() {
        return !player.lifterAttemptLifting() && player.hasLifting() && player.liftingId() != -1;
    }

}
