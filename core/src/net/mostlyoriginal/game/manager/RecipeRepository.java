package net.mostlyoriginal.game.manager;

import com.artemis.BaseSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectIntMap;
import net.mostlyoriginal.game.component.RecipeData;
import net.mostlyoriginal.game.system.ItemLibrary;
import net.mostlyoriginal.game.system.RecipeLibrary;

import java.util.HashMap;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class RecipeRepository extends BaseSystem {

    private RecipeLibrary recipeLibrary;

    @Override
    protected void initialize() {
        super.initialize();
        final Json json = new Json();
        recipeLibrary = json.fromJson(RecipeLibrary.class, Gdx.files.internal("recipes.json"));
    }

    @Override
    protected void processSystem() {
    }

    public RecipeData firstMatching(IntBag ingredients) {
        for (int i = 0, s = ingredients.size(); i < s; i++) {
            final String ingredient = E(ingredients.get(i)).getItem().type;
            RecipeData recipe = recipeLibrary.firstWithIngredient(ingredient);
            if (recipe != null) {
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
