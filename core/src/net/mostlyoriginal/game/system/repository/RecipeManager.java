package net.mostlyoriginal.game.system.repository;

import com.artemis.BaseSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectIntMap;
import net.mostlyoriginal.game.component.Item;
import net.mostlyoriginal.game.component.RecipeData;

import static com.artemis.E.E;

/**
 * @author Daan van Yperen
 */
public class RecipeManager extends BaseSystem {

    public RecipeLibrary recipeLibrary;
    private ItemTypeManager itemManager;

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
                if (itemManager.get(ingredient) == null)
                    throw new RuntimeException("Unknown ingredient " + ingredient);
            }
            for (String product : o.produces) {
                if (itemManager.get(product) == null) throw new RuntimeException("Unknown product " + product);
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

            for (String recipeIngredient : recipe.ingredients) {
                recipeIngredient = itemManager.substitute(recipeIngredient);
                if (recipeIngredient.equals(itemManager.substitute(ingredient))) {
                    if (matchesRecipe(ingredients, recipe)) return recipe;
                    break;
                }
            }
        }

        return null;
    }

    private boolean matchesRecipe(IntBag ingredients, RecipeData recipe) {

        final ObjectIntMap<String> reagents = new ObjectIntMap<>();

        // Take stock of ingredients in hoppers.
        for (int j = 0, s = ingredients.size(); j < s; j++) {
            final String ingredient = itemManager.substitute(E(ingredients.get(j)).getItem().type);
            reagents.getAndIncrement(ingredient, 0, 1);
//            System.out.println("Increment ingredient + 1 " + ingredient);
        }

        // track each reagent, we need to do this for repeated reagents.
        for (String recipeIngredient : recipe.ingredients) {
            recipeIngredient= itemManager.substitute(recipeIngredient);
            if (reagents.get(recipeIngredient, 0) <= 0) {
//                System.out.println("MISSING ingredient "+recipeIngredient );
                return false;
            }
//            System.out.println("has ingredient "+recipeIngredient );
            reagents.getAndIncrement(recipeIngredient, 0, -1);
        }

        // stuff remains. this means there are too many reagents!
        for (String remainingIngredient : reagents.keys()) {
            if (reagents.get(remainingIngredient, 0) > 0) {
//                System.out.println("Ingredient remaining "+remainingIngredient );
                return false;
            }
        }

//        System.out.println("Valid recipe: " +recipe );

        return true;
    }
}
