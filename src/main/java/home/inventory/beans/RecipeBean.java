package home.inventory.beans;

import home.inventory.beans.data.RecipeLazyLoader;
import home.inventory.entities.Ingredient;
import home.inventory.entities.PantryItem;
import home.inventory.entities.Recipe;
import home.inventory.repos.IngredientRepo;
import home.inventory.repos.RecipeRepo;
import io.vavr.collection.Stream;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import home.inventory.repos.PantryItemRepo;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Manages the creation, modification, and deletion of both Recipes and
 * Ingredients associated with the Recipes
 *
 * @author BRudowski
 */
@Named
@SessionScoped
public class RecipeBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private RecipeRepo recipeRepo;
    @Inject
    private PantryItemRepo itemRepo;
    @Inject
    private IngredientRepo ingredientRepo;
    private String recipeName;
    private String itemName;
    private double quantity;
    private String unit;
    private Recipe recipe;
    private Ingredient selectedIngredient;
    @Inject
    private RecipeLazyLoader recipes;

    /**
     * Creates a new Recipe with the provided recipeName and sets it as the
     * currently selected Recipe
     */
    @Transactional
    public void createRecipe() {
        if (recipeRepo.findOptionalByName(recipeName) == null) {
            recipe = new Recipe(recipeName);
            recipeRepo.save(recipe);
            clearIngredient();
        } else {
            fctx().addMessage(null, new FacesMessage("Error", "A recipe with that name already exists"));
            clearRecipe();
        }
    }

    /**
     * Creates an Ingredient and associates it to the selected Recipe.
     * Additionally this method will automatically create a new PantryItem with
     * 0 quantity if a PantryItem with the provided itemName does not already
     * exist
     */
    @Transactional
    public void addIngredientToRecipe() {
        PantryItem item = itemRepo.findOptionalByName(itemName);
        if (item == null) {
            item = new PantryItem(itemName, 0.0, unit);
            itemRepo.save(item);
            fctx().addMessage(null, new FacesMessage("Note", "New Item created for this ingredient. "));
        } else {
            //check if the units can be converted 
            //This was pushed back to a future build
//            if (!unit.equals(item.getUnits())) {
//                try {
//                    Unit u = Unit.valueOf(unit);
//                } catch (IllegalArgumentException ex) {
//                    //Display some message to the user
//                    return;
//                }
//            }
        }
        Ingredient ingredient = new Ingredient(item, quantity, recipe);
        if (!recipe.getIngredients().contains(ingredient)) {
            ingredientRepo.save(ingredient);
            fctx().addMessage(null, new FacesMessage("Success", "Added the following ingredient: " + item.getName()));
            itemRepo.flushAndClear();
            recipe.getIngredients().add(ingredient);
            clearIngredient();
        } else {
            fctx().addMessage(null, new FacesMessage("Warning", "This recipe already has the ingredient " + item.getName()));
        }
    }

    private void clearIngredient() {
        itemName = "";
        quantity = 0.0;
        unit = "";
    }

    /**
     * Removes a selected Ingredient from the selected Recipe
     */
    @Transactional
    public void removeIngredientFromRecipe() {
        recipe.getIngredients().remove(selectedIngredient);
        ingredientRepo.attachAndRemove(selectedIngredient);
        ingredientRepo.flushAndClear();
        fctx().addMessage(null, new FacesMessage("Success", "Ingredient " + selectedIngredient.getPantryItem().getName() + " removed from the recipe"));
    }

    /**
     * Deletes the selected recipe and all associated Ingredients from the
     * database
     */
    @Transactional
    public void deleteRecipe() {
        recipeRepo.attachAndRemove(recipe);
        clearRecipe();
    }

    /**
     * Removes the quantity of each Ingredient on the selected Recipe from each
     * associated PantryItem. This will not attempt to remove any quantities if
     * the recipe cannot be made. See canMakeRecipe()
     */
    @Transactional
    public void makeRecipe() {
        if (canMakeRecipe()) {
            Stream.ofAll(recipe.getIngredients())
                    .map(ingredient -> {
                        PantryItem item = ingredient.getPantryItem();
                        item.setQuantity(item.getQuantity() - ingredient.getQuantity());
                        return item;
                    }).forEach(item -> {
                itemRepo.save(item);
            });
            itemRepo.flushAndClear();
        } else {
            fctx().addMessage(null, new FacesMessage("Error", "Not enough of an ingredient to make the recipe"));
        }
    }

    /**
     * Checks if enough of each PantryItem associated with each Ingredient on a
     * previously selected Recipe exists to be able to cook the recipe
     *
     * @return True if each PantryItem has a higher quantity than each
     * Ingredient in the Recipe. False if any PantryItems have a lower quantity
     * than each Ingredient in the Recipe
     */
    @Transactional
    public boolean canMakeRecipe() {
        boolean success = false;
        if (recipe != null && !recipe.getIngredients().isEmpty()) {
            success = !Stream.ofAll(recipe.getIngredients())
                    .exists(ingredient -> ingredient.getQuantity() > ingredient.getPantryItem().getQuantity());
        }
        return success;
    }

    /**
     * Changes the value stored for the quantity of the ingredient to use in the
     * associated recipe
     */
    @Transactional
    public void changeIngredientQuantity() {
        Ingredient ingredient = Stream.ofAll(recipe.getIngredients())
                .filter(ing -> ing.equals(selectedIngredient))
                .getOrNull();
        ingredient = ingredientRepo.findBy(ingredient.getId());
        ingredient.setQuantity(quantity);
        ingredientRepo.save(ingredient);
        ingredientRepo.flushAndClear();
        recipe = recipeRepo.findBy(recipe.getId());
        fctx().addMessage(null, new FacesMessage("Success", "Modified ingredient quantity to be " + quantity));
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Recipe getRecipe() {
        return recipe;
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

    public Recipe getSelectedRecipe() {
        return recipe;
    }

    public void setSelectedRecipe(String recipeName) {
        this.recipeName = recipeName;
    }

    public RecipeLazyLoader getRecipes() {
        return recipes;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Ingredient getSelectedIngredient() {
        return selectedIngredient;
    }

    public void setSelectedIngredient(Ingredient selectedIngredient) {
        this.selectedIngredient = selectedIngredient;
    }

    public void clearRecipe() {
        recipeName = "";
        recipe = null;
    }

    private FacesContext fctx() {
        return FacesContext.getCurrentInstance();
    }
}
