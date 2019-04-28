package net.mostlyoriginal.game.system.mechanics;

import com.artemis.E;
import com.artemis.EntitySubscription;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import net.mostlyoriginal.api.component.graphics.Tint;
import net.mostlyoriginal.game.GameRules;
import net.mostlyoriginal.game.component.*;
import net.mostlyoriginal.game.manager.ItemRepository;
import net.mostlyoriginal.game.manager.RecipeRepository;
import net.mostlyoriginal.game.system.common.FluidSystem;
import net.mostlyoriginal.game.system.map.MapSpawnerSystem;

/**
 * @author Daan van Yperen
 */
@All(Machine.class)
public class RecipeIngredientHintSystem extends FluidSystem {

    private static final float HINT_SCALE = 0.8f;
    private static final int ICON_OFFSET_X = 4;
    private static final int ICON_OFFSET_Y = 4;
    private static final int SPACING_BETWEEN_ITEMS = 10;
    private RecipeRepository recipeRepository;
    private ItemRepository itemRepository;
    private MapSpawnerSystem mapSpawnerSystem;
    private PlayerAgeSystem playerAgeSystem;

    @All(RecipeIngredientHint.class)
    public EntitySubscription recipeIngredientHints;

    @Override
    protected void process(E e) {
        Machine machine = e.getMachine();
        if (!machine.contents.isEmpty()) {
            purgeRecipesForMachine(e.id());
            displayValidRecipes(e.id(), machine.contents);
        } else {
            purgeRecipesForMachine(e.id());
        }
    }

    private void purgeRecipesForMachine(int machineId) {
        IntBag hints = recipeIngredientHints.getEntities();
        for (int i = 0, s = hints.size(); i < s; i++) {
            E recipeIngredientHint = E.E(hints.get(i));
            if ( recipeIngredientHint.recipeIngredientHintMachineId() == machineId ) {
                recipeIngredientHint.removeRecipeIngredientHint().deleteFromWorld();
            }
        }
    }

    private void displayValidRecipes(int machineId, IntBag contents) {

        int y = 100;
        RecipeData[] recipes = recipeRepository.recipeLibrary.recipes;
        for (int j = 0, s2 = recipes.length; j < s2; j++) {
            final RecipeData recipe = recipes[j];
            if (hasIngredients(recipe, contents)) {
                createIngridientHints(recipe, 32, y, machineId);
                y += 20;
            }
        }

    }

    private static final Tint hintTint = new Tint(1f,1f,1f,0.8f);

    private void createIngridientHints(RecipeData recipe, int startX, int startY, int machineId) {

        int x = startX;
        int y = startY;

        for (String ingredient : recipe.produces) {
            E.E()
                    .pos(x- ICON_OFFSET_X,y- ICON_OFFSET_Y)
                    .anim(itemRepository.get(ingredient).sprite)
                    .recipeIngredientHintMachineId(machineId)
                    .tint(hintTint)
                    .scale(HINT_SCALE)
                    .renderLayer(GameRules.LAYER_INGREDIENT_HINTS);
            x+=SPACING_BETWEEN_ITEMS;
        }
        E.E()
                .pos(x,y)
                .anim("equals")
                .tint(hintTint)
                .scale(0.5f)
                .recipeIngredientHintMachineId(machineId)
                .renderLayer(GameRules.LAYER_INGREDIENT_HINTS+1);
        x+=SPACING_BETWEEN_ITEMS;

        int count=0;
        for (String ingredient : recipe.ingredients) {
            E.E()
                    .pos(x- ICON_OFFSET_X,y- ICON_OFFSET_Y)
                    .tint(hintTint)
                    .anim(itemRepository.get(ingredient).sprite)
                    .recipeIngredientHintMachineId(machineId)
                    .scale(HINT_SCALE)
                    .renderLayer(GameRules.LAYER_INGREDIENT_HINTS+count*2);
            x+=SPACING_BETWEEN_ITEMS;
            if ( count < recipe.ingredients.length-1 ) {
                E.E()
                        .pos(x, y)
                        .tint(hintTint)
                        .anim("plus")
                        .recipeIngredientHintMachineId(machineId)
                        .scale(0.5f)
                        .renderLayer(GameRules.LAYER_INGREDIENT_HINTS+count*2+1);
                x +=
                        SPACING_BETWEEN_ITEMS;
            }
            count++;
        }

    }

    private boolean hasIngredients(RecipeData recipe, IntBag contents) {
        for (int i = 0, s = contents.size(); i < s; i++) {
            Item ingredientItem = E.E(contents.get(0)).getItem();
            final String ingredient = ingredientItem.type;
            if (!recipe.hasIngredient(ingredient)) return false;
            if ( "item_player".equals(ingredient) ) return false;
        }
        return true;
    }
}
