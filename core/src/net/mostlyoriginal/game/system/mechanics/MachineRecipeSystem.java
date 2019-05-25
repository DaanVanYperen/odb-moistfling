package net.mostlyoriginal.game.system.mechanics;

import com.artemis.E;
import com.artemis.annotations.All;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.util.Cooldown;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.system.MyParticleEffectStrategy;
import net.mostlyoriginal.game.system.common.FluidSystem;
import net.mostlyoriginal.game.system.future.FutureSpawnUtility;
import net.mostlyoriginal.game.system.repository.RecipeManager;

/**
 * @author Daan van Yperen
 */
@All(Machine.class)
public class MachineRecipeSystem extends FluidSystem {

    private RecipeManager recipeManager;

    private Cooldown systemCooldown = Cooldown.withInterval(UPDATE_EVERY_SECONDS);
    private static final float UPDATE_EVERY_SECONDS = 0.1f;

    @Override
    protected boolean checkProcessing() {
        // don't run this too aggressively.
        return systemCooldown.ready(world.delta);
    }

    @Override
    protected void process(E e) {
        Machine machine = e.getMachine();
        if (!machine.contents.isEmpty()) {
            // we can just bruteforce this as there won't be many machines initially.
            final RecipeData recipe = recipeManager.firstMatching(machine.contents);
            if (recipe != null) {
                machine.warmupAge += UPDATE_EVERY_SECONDS;
                if (machine.warmupAge > 0.5f) {
                    executeRecipe(machine, e.getGridPos(), recipe);
                }
                return;
            }
        }
        machine.warmupAge = 0;
    }

    private void executeRecipe(Machine machine, GridPos machineGridPos, RecipeData recipe) {

        if (!attemptAging(recipe.ageCost)) {
            //System.out.println("Cannot afford payment for " + recipe.id + ".");
            E player = E.withTag("player");
            E.E().particleEffect(MyParticleEffectStrategy.EFFECT_BLACK_SPUTTER).pos(player.gridPosX() * GameRules.CELL_SIZE + 16,
                    player.gridPosY() * GameRules.CELL_SIZE + 20);
            return;
        }

        consumeIngredients(machine);
        spawnProduce(machineGridPos, recipe);
        System.out.println("Recipe " + recipe.id + " triggered.");
        E.E().playSound(MathUtils.randomBoolean() ? "sfx_magic_2" : "sfx_magic_1");
    }

    private void spawnProduce(GridPos machineGridPos, RecipeData recipe) {
        for (String producesItem : recipe.produces) {
            if (producesItem.startsWith("item_player_")) continue;

            E item = FutureSpawnUtility
                    .item(producesItem, 1, machineGridPos.x, machineGridPos.y);
            giveItemToPlayerIfHandsEmpty(item);
            // @todo replace with 'future inventory call'. giveItemToPlayerIfHandsEmpty(item);
        }
    }

    // @todo replace with 'future inventory call'. giveItemToPlayerIfHandsEmpty(item);
    @Deprecated
    private void giveItemToPlayerIfHandsEmpty(E item) {
        E player = E.withTag("player");
        if (!player.hasHolding()) {
            player.actionPickupTarget(item.id());
            //
            E.E().particleEffect(MyParticleEffectStrategy.EFFECT_POOF).pos(player.gridPosX() * GameRules.CELL_SIZE + 16,
                    player.gridPosY() * GameRules.CELL_SIZE + 48);
        }
    }

    private void consumeIngredients(Machine machine) {
        for (int i = 0, s = machine.contents.size(); i < s; i++) {
            final E ingredient = E.E(machine.contents.get(i));
            final ItemData itemData = ingredient.getItemMetadata().data;
            if (itemData.consumed) {
                E.E().particleEffect(MyParticleEffectStrategy.EFFECT_POOF).pos(ingredient.posX() + 16, ingredient.posY() + 16);
                ingredient.deleteFromWorld();
            }
        }
        machine.contents.clear();
    }

    public boolean attemptAging(int ageCost) {
        Player player = E.withTag("player").getPlayer();
        if (ageCost < 0) {
            // de-age
            player.age = MathUtils.clamp(player.age + ageCost, Player.MIN_AGE, Player.MAX_AGE);
            return true;
        }
        if (player.age + ageCost <= Player.MAX_AGE) {
            // age!
            player.age += ageCost;
            return true;
        }
        return false;
    }
}
