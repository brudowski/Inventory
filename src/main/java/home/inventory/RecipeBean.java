package home.inventory;

import home.inventory.entities.Ingredient;
import home.inventory.entities.Item;
import home.inventory.entities.Recipe;
import home.inventory.enums.Unit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BRudowski
 */
public class RecipeBean {

    private String recipeName;
    private String itemName;
    private double quantity;
    private String selectedRecipe;
    private String selectedItem;
    private String unit;

    private final List<Recipe> recipes = new ArrayList<>();

    public void createRecipe() {
        Recipe recipe = new Recipe(recipeName);
        //save recipe
    }

    public void addIngredientToRecipe() {
        Recipe recipe; //get recipe from database using selectedRecipe
        Item item = null;//get item from database
        if (item != null) {
            item = new Item(itemName, 0.0, unit);
            //save item to database
        } else {
            //check if the units can be converted
            if (!unit.equals(item.getUnits())) {
                try {
                    Unit u = Unit.valueOf(unit);
                } catch (IllegalArgumentException ex) {
                    //Display some message to the user
                    return;
                }
            }
        }
        Ingredient ingredient; //get from database using item name & quantity
        if () {
            ingredient = new Ingredient(itemName, quantity);
        }
        recipe.getIngredients().add(e)
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getSelectedRecipe() {
        return selectedRecipe;
    }

    public void setSelectedRecipe(String selectedRecipe) {
        this.selectedRecipe = selectedRecipe;
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(String selectedItem) {
        this.selectedItem = selectedItem;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
