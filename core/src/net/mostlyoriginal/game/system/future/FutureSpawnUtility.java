package net.mostlyoriginal.game.system.future;

import com.artemis.E;
import net.mostlyoriginal.api.utils.Preconditions;
import net.mostlyoriginal.game.EntityType;
import net.mostlyoriginal.game.component.inventory.Inventory;

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

    public static E item(String subType, int stackSize, int gridX, int gridY) {
        Preconditions.checkNotNull(subType);
        return of(ITEM, gridX, gridY).futureEntitySubType(subType)
                .futureEntityCount(stackSize);
    }

    public static E shopper(int gridX, int gridY, String anim, String desiredItem, String rewardItem, int count) {
        return of(SHOPPER, gridX, gridY)
                .anim(anim)
                .properties(KEY_DESIRED_ITEM, desiredItem)
                .properties(KEY_REWARD_ITEM, rewardItem)
                .properties(KEY_REWARD_ITEM_COUNT, count);
    }

    public static E slot(int gridX, int gridY, Inventory.Mode mode, Object accepts, int x, int y, String transform) {
        E result = of(EntityType.SLOT, gridX, gridY)
                .properties("mode", mode)
                .properties("accepts", accepts)
                .properties("x", x)
                .properties("y", x);
        if ( transform != null )
            result.properties("transform", transform);
        return result;

    }


    public static E of(int type, int gridX, int gridY) {
        return E()
                .futureEntity(type)
                .gridPos(gridX, gridY);
    }
}
