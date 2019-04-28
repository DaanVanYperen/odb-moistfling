package net.mostlyoriginal.game.manager;

import com.artemis.BaseSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectIntMap;
import net.mostlyoriginal.game.component.Item;
import net.mostlyoriginal.game.component.RecipeData;
import net.mostlyoriginal.game.system.ItemLibrary;
import net.mostlyoriginal.game.system.RecipeLibrary;

import java.util.HashMap;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class RecipeRepository extends BaseSystem {

    public RecipeLibrary recipeLibrary;
    private ItemRepository itemRepository;

    @Override
    protected void initialize() {
        super.initialize();
        final Json json = new Json();
        recipeLibrary = json.fromJson(RecipeLibrary.class, Gdx.files.internal("recipes.json"));
        validateRecipes();
    }


    public void validateRecipes() {
        for (RecipeData o : recipeLibrary.recipes) {
            for (String ingredient : o.ingredients) {
                if (itemRepository.get(ingredient) == null)
                    throw new RuntimeException("Unknown ingredient " + ingredient);
            }
            for (String product : o.produces) {
                if (itemRepository.get(product) == null) throw new RuntimeException("Unknown product " + product);
            }
        }
    }


    @Override
    protected void processSystem() {
    }

    public RecipeData firstMatching(IntBag ingredients) {

        Item ingredientItem = E(ingredients.get(0)).getItem();
        final String ingredient = ingredientItem.type;

        for (int j = 0, s2 = recipeLibrary.recipes.length; j < s2; j++) {
            final RecipeData recipe = recipeLibrary.recipes[j];
            if (recipe.hasIngredient(itemRepository.substitute(ingredient))) {
                if (matchesRecipe(ingredients, recipe)) return recipe;
            }
        }

        return null;
    }

    private boolean matchesRecipe(IntBag ingredients, RecipeData recipe) {

        final ObjectIntMap<String> reagents = new ObjectIntMap<>();

        // Take stock of ingredients in hoppers.
        for (int j = 0, s = ingredients.size(); j < s; j++) {
            final String ingredient = E(ingredients.get(j)).getItem().type;
            reagents.getAndIncrement(ingredient, 0, 1);
        }

        // track each reagent, we need to do this for repeated reagents.
        for (String recipeIngredient : recipe.ingredients) {
            recipeIngredient=itemRepository.substitute(recipeIngredient);
            if (reagents.get(recipeIngredient, 0) <= 0) {
                return false;
            }
            reagents.getAndIncrement(recipeIngredient, 0, -1);
        }

        // stuff remains. this means there are too many reagents!
        for (String remainingIngredient : reagents.keys()) {
            if (reagents.get(remainingIngredient, 0) > 0) {
                return false;
            }
        }

        return true;
    }
}
