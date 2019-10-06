package net.mostlyoriginal.game.system;

import com.artemis.BaseSystem;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.util.Cooldown;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.Swimming;
import net.mostlyoriginal.game.system.future.FutureSpawnUtility;

/**
 * @author Daan van Yperen
 */
public class LootSpawnSystem extends BaseSystem {

    private Cooldown cooldown = Cooldown.withInterval(1f);

    @Override
    protected boolean checkProcessing() {
        return cooldown.ready(world.delta);
    }



    @Override
    protected void processSystem() {
        spawnRandomItem(22);
    }

    private void spawnRandomItem(int y) {
        FutureSpawnUtility.item(randomItem(),1, (int)(MathUtils.random(0f,1f) * (GameRules.SCREEN_WIDTH/32)), y, true);
    }

    private String randomItem() {
        switch (MathUtils.random(0,20)) {

            case 1 : return "item_net";
            case 2 : return "item_barrel";
            case 3 : return MathUtils.random(0,100) > 50f ? "item_driftwood" : "item_coconut";
            case 4 : return MathUtils.random(0,100) > 5f ? "item_driftwood" : "item_coconut_seed";
            case 5 : return MathUtils.random(0,100) > 50f ? "item_driftwood" : "item_citrus";
            case 6 : return MathUtils.random(0,100) > 5f ? "item_barrel" : "item_citrus_seed";
            case 7 : return MathUtils.random(0,100) > 5f ? "item_barrel" : "item_snorkel";
            case 8 : return MathUtils.random(0,100) > 5f ? "item_net" : "item_flippers";
            case 9 : return MathUtils.random(0,100) > 5f ? "item_net" : "item_tiki1";
            case 10: return MathUtils.random(0,100) > 5f ? "item_driftwood" : "item_tiki2";
            case 11: return MathUtils.random(0,100) > 5f ? "item_driftwood" : "item_tiki3";
            case 12: return MathUtils.random(0,100) > 5f ? "item_driftwood" : "item_lampion";
            case 13: return MathUtils.random(0,100) > 5f ? "item_driftwood" : "item_dog";
            case 14: return MathUtils.random(0,100) > 5f ? "item_driftwood" : "item_skull";
            case 15: return MathUtils.random(0,100) > 5f ? "item_driftwood" : "item_ducky";
            case 16: return MathUtils.random(0,100) > 5f ? "item_driftwood" : "item_flamingo";
            case 17: return MathUtils.random(0,100) > 5f ? "item_driftwood" : "item_chest";
            case 18: return MathUtils.random(0,100) > 5f ? "item_driftwood" : "item_starfish";
            case 19: return MathUtils.random(0,100) > 5f ? "item_driftwood" : "item_seashell";
            case 20: return MathUtils.random(0,100) > 5f ? "item_driftwood" : "item_wife";
            default: return "item_driftwood";
        }
    }
}
