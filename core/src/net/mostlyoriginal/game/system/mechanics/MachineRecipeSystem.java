package net.mostlyoriginal.game.system.mechanics;

import com.artemis.E;
import com.artemis.annotations.All;
import net.mostlyoriginal.api.util.Cooldown;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.ItemData;
import net.mostlyoriginal.game.component.Machine;
import net.mostlyoriginal.game.component.RecipeData;
import net.mostlyoriginal.game.system.common.FluidSystem;
import net.mostlyoriginal.game.system.control.PickupSystem;
import net.mostlyoriginal.game.system.map.MapSpawnerSystem;
import net.mostlyoriginal.game.system.render.ParticleSystem;
import net.mostlyoriginal.game.system.repository.ItemManager;
import net.mostlyoriginal.game.system.repository.RecipeManager;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
@All(Machine.class)
public class MachineRecipeSystem extends FluidSystem {

    private RecipeManager recipeManager;
    private ItemManager itemManager;
    private MapSpawnerSystem mapSpawnerSystem;
    private PlayerAgeSystem playerAgeSystem;
    private PickupSystem pickupSystem;
    private GameScreenAssetSystem gameScreenAssetSystem;
    private ParticleSystem particleSystem;
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
            RecipeData recipe = recipeManager.firstMatching(machine.contents);
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

        if (!playerAgeSystem.attemptPayment(recipe.ageCost)) {
            //System.out.println("Cannot afford payment for " + recipe.id + ".");
            E player = E.withTag("player");
            particleSystem.poof(player.gridPosX() * GameRules.CELL_SIZE + 16,
                    player.gridPosY() * GameRules.CELL_SIZE + 20, 2, 3, ParticleSystem.COLOR_BLACK_TRANSPARENT);
            return;
        }

        consumeIngredients(machine);
        spawnProduce(machineGridPos, recipe);
        System.out.println("Recipe " + recipe.id + " triggered.");
        gameScreenAssetSystem.playSfx("sfx_magic_2", "sfx_magic_1");
    }

    private void spawnProduce(GridPos machineGridPos, RecipeData recipe) {
        boolean first = true;
        for (String producesItem : recipe.produces) {
            if (producesItem.startsWith("item_player_")) continue;
            E item = mapSpawnerSystem.spawnItem(machineGridPos.x, machineGridPos.y, producesItem);
            if (item != null && first) {
                first = false;
                giveItemToPlayerIfHandsEmpty(item);

            }
        }
    }

    private void giveItemToPlayerIfHandsEmpty(E item) {
        E player = E.withTag("player");
        if (!player.hasLifting()) {
            pickupSystem.attemptPickup(player, item);
            particleSystem.poof(player.gridPosX() * GameRules.CELL_SIZE + 16,
                    player.gridPosY() * GameRules.CELL_SIZE + 48, 40, 40, ParticleSystem.COLOR_WHITE_TRANSPARENT);
        }
    }

    private void consumeIngredients(Machine machine) {
        for (int i = 0, s = machine.contents.size(); i < s; i++) {
            final E ingredient = E.E(machine.contents.get(i));
            final ItemData itemData = itemManager.get(ingredient.getItem().type);
            if (itemData.consumed) {
                particleSystem.poof(ingredient.posX() + 16, ingredient.posY() + 16, 40, 40, ParticleSystem.COLOR_WHITE_TRANSPARENT);
                ingredient.deleteFromWorld();
            }
        }
    }
}
