package net.mostlyoriginal.game.system;

import net.mostlyoriginal.game.component.RecipeData;

import java.io.Serializable;

/**
 * @author Daan van Yperen
 */
public class RecipeLibrary implements Serializable {
    public RecipeData[] recipes;

    public RecipeLibrary() {
    }

    /**
     * Return concept, or <code>null</code> if empty.
     */
    public RecipeData getById(String id) {
        for (RecipeData recipe : recipes) {
            if (recipe.id != null && recipe.id.equals(id)) return recipe;
        }
        return null;
    }

    public RecipeData firstWithIngredient(String ingredient) {
        for (RecipeData recipe : recipes) {
            if (recipe.hasIngredient(ingredient)) return recipe;
        }
        return null;
    }
}
