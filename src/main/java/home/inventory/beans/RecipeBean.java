package home.inventory.beans;

import home.inventory.entities.Ingredient;
import home.inventory.entities.IngredientKey;
import home.inventory.entities.Item;
import home.inventory.entities.Recipe;
import home.inventory.enums.Unit;
import home.inventory.repos.IngredientRepo;
import home.inventory.repos.ItemRepo;
import home.inventory.repos.RecipeRepo;
import io.vavr.collection.Stream;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

/**
 *
 * @author BRudowski
 */
@Named
public class RecipeBean {

    private final List<Recipe> recipes = new ArrayList<>();
    @Inject
    private RecipeRepo recipeRepo;
    @Inject
    private ItemRepo itemRepo;
    @Inject
    private IngredientRepo ingredientRepo;
    private String recipeName;
    private String itemName;
    private double quantity;
    private String selectedRecipe;
    private String selectedItem;
    private String unit;

    @Transactional
    public void createRecipe() {
        Recipe recipe = new Recipe(recipeName);
        recipeRepo.save(recipe);
    }

    @Transactional
    public void addIngredientToRecipe() {
        Recipe recipe = recipeRepo.findBy(selectedRecipe);
        Item item = itemRepo.findBy(selectedItem);
        if (item == null) {
            item = new Item(itemName, 0.0, unit);
            itemRepo.save(item);
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
        Ingredient ingredient = new Ingredient(item, quantity, recipe);
        if (!recipe.getIngredients().contains(ingredient)) {
            ingredientRepo.save(ingredient);
        } else {
            //some message to user/log
        }
    }

    @Transactional
    public void updateIngredientQuantity() {
        Recipe recipe = recipeRepo.findBy(selectedRecipe);
        Item item = itemRepo.findBy(selectedItem);
        final String ingredientName = item.getName();
        Ingredient ingredient = Stream.ofAll(recipe.getIngredients())
                .filter(i -> i.getItem().getName().equals(ingredientName))
                .get();
        if (ingredient != null) {
            ingredient.setQuantity(quantity);
            ingredientRepo.save(ingredient);
        } else {
            //Message to user/log
        }
    }

    @Transactional
    public void removeIngredientFromRecipe() {
//        Recipe recipe = recipeRepo.findBy(selectedRecipe);
        Ingredient ingredient = ingredientRepo.findBy(new IngredientKey(recipeName, itemName));
//        recipe.getIngredients().remove(ingredient);
//        recipeRepo.save(recipe);
        ingredientRepo.remove(ingredient);
    }

    @Transactional
    public void deleteRecipe() {
        Recipe recipe = recipeRepo.findBy(selectedRecipe);
        ingredientRepo.removeAll(recipe.getIngredients());
        recipeRepo.remove(recipe);
        
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
