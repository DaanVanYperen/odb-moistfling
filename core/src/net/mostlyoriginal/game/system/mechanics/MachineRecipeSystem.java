package net.mostlyoriginal.game.system.mechanics;

import com.artemis.E;
import com.artemis.annotations.All;
import net.mostlyoriginal.game.component.GridPos;
import net.mostlyoriginal.game.component.ItemData;
import net.mostlyoriginal.game.component.Machine;
import net.mostlyoriginal.game.component.RecipeData;
import net.mostlyoriginal.game.manager.ItemRepository;
import net.mostlyoriginal.game.manager.RecipeRepository;
import net.mostlyoriginal.game.system.common.FluidSystem;
import net.mostlyoriginal.game.system.control.PickupSystem;
import net.mostlyoriginal.game.system.map.MapSpawnerSystem;
import net.mostlyoriginal.game.system.view.GameScreenAssetSystem;

/**
 * @author Daan van Yperen
 */
@All(Machine.class)
public class MachineRecipeSystem extends FluidSystem {

    private RecipeRepository recipeRepository;
    private ItemRepository itemRepository;
    private MapSpawnerSystem mapSpawnerSystem;
    private PlayerAgeSystem playerAgeSystem;
    private PickupSystem pickupSystem;
    private GameScreenAssetSystem gameScreenAssetSystem;

    @Override
    protected void process(E e) {
        Machine machine = e.getMachine();
        if (!machine.contents.isEmpty()) {
            // we can just bruteforce this as there won't be many machines initially.
            RecipeData recipe = recipeRepository.firstMatching(machine.contents);
            if (recipe != null) {
                machine.warmupAge += world.delta;
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
            System.out.println("Cannot afford payment for " + recipe.id + ".");
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
        }
    }

    private void consumeIngredients(Machine machine) {
        for (int i = 0, s = machine.contents.size(); i < s; i++) {
            final E ingredient = E.E(machine.contents.get(i));
            final ItemData itemData = itemRepository.get(ingredient.getItem().type);
            if (itemData.consumed) {
                ingredient.deleteFromWorld();
            }
        }
    }
}
