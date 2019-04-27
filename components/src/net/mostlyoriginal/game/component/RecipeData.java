package net.mostlyoriginal.game.component;

/**
 * @author Daan van Yperen
 */
public class RecipeData {
    public String id;
    public Machine.Type machine;
    public String[] ingredients;
    public String[] produces;
    public int ageCost = 0;

    public boolean hasIngredient(String ingredient) {
        for (String s : ingredients) {
            if (ingredient.equals(s)) {
                return true;
            }
        }
        return false;
    }
}
