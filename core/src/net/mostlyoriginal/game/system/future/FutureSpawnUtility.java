package net.mostlyoriginal.game.system.future;

import com.artemis.E;
import net.mostlyoriginal.api.utils.Preconditions;
import net.mostlyoriginal.game.EntityType;

import static com.artemis.E.E;
import static net.mostlyoriginal.game.EntityType.ITEM;
import static net.mostlyoriginal.game.EntityType.SHOPPER;

/**
 * @author Daan van Yperen
 */
public class FutureSpawnUtility {

    public static final String KEY_DESIRED_ITEM = "desiredItem";
    public static final String KEY_REWARD_ITEM = "rewardItem";
    public static final String KEY_REWARD_ITEM_COUNT = "rewardItemCount";

    public static E of(int type, int gridX, int gridY) {
        return E()
                .futureEntity(type)
                .pos(gridX*48, gridY*48);
    }
}
