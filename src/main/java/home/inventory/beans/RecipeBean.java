package home.inventory.beans;

import home.inventory.beans.data.RecipeLazyLoader;
import home.inventory.entities.Ingredient;
import home.inventory.entities.PantryItem;
import home.inventory.entities.Recipe;
import home.inventory.repos.IngredientRepo;
import home.inventory.repos.RecipeRepo;
import io.vavr.collection.Stream;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import home.inventory.repos.PantryItemRepo;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
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

    @Transactional
    public void createRecipe() {
        if (recipeRepo.findOptionalByName(recipeName) == null) {
            recipe = new Recipe(recipeName);
            recipeRepo.saveAndFlushAndRefresh(recipe);
            clearIngredient();
        } else {
            fctx().addMessage(null, new FacesMessage("Error", "A recipe with that name already exists"));
            clearRecipe();
        }
    }

    @Transactional
    public void addIngredientToRecipe() {
        PantryItem item = itemRepo.findBy(itemName);
        if (item == null) {
            item = new PantryItem(itemName, 0.0, unit);
            itemRepo.saveAndFlush(item);
            fctx().addMessage(null, new FacesMessage("Note", "New Item created for this ingredient. "));
        } else {
            //check if the units can be converted 
            //TODO: for a future build
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
            ingredientRepo.saveAndFlush(ingredient);
            fctx().addMessage(null, new FacesMessage("Success", "Added the following ingredient: " + item.getName()));
            recipeRepo.refresh(recipe);
            clearIngredient();
        } else {
            fctx().addMessage(null, new FacesMessage("Warning", "This recipe already has the ingredient " + item.getName()));
        }
    }
    
    private void clearIngredient(){
        itemName = "";
        quantity = 0.0;
        unit = "";
    }

    @Transactional
    public void updateIngredientQuantity() {
        PantryItem item = itemRepo.findBy(itemName);
        final String ingredientName = item.getName();
        Ingredient ingredient = Stream.ofAll(recipe.getIngredients())
                .filter(i -> i.getItem().getName().equals(ingredientName))
                .get();
        if (ingredient != null) {
            ingredient.setQuantity(quantity);
            ingredientRepo.saveAndFlushAndRefresh(ingredient);
        } else {
            //Message to user/log
        }
    }

    @Transactional
    public void removeIngredientFromRecipe() {
        ingredientRepo.attachAndRemove(selectedIngredient);
        ingredientRepo.flushAndClear();
        recipe = recipeRepo.findBy(recipe.getName());
        fctx().addMessage(null, new FacesMessage("Success", "Ingredient " + itemName + " removed from the recipe"));
    }

    @Transactional
    public void deleteRecipe() {
        Recipe rec = recipeRepo.findOptionalByName(recipe.getName());
        List<Ingredient> ingredients = ingredientRepo.findAllByRecipeName(rec.getName()).getResultList();
        ingredientRepo.removeAll(ingredients);
        recipeRepo.remove(rec);
        clearRecipe();
    }

    @Transactional
    public void makeRecipe() {
        if (canMakeRecipe()) {
            Stream.ofAll(recipe.getIngredients())
                    .map(ingredient -> {
                        PantryItem item = ingredient.getItem();
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

    @Transactional
    public boolean canMakeRecipe() {
        boolean success = false;
        if (recipe != null) {
            success = !Stream.ofAll(recipe.getIngredients())
                    .exists(ingredient -> ingredient.getQuantity() > ingredient.getItem().getQuantity());
        }
        return success;
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

    public void clearRecipe() {
        recipeName = "";
        recipe = null;
    }

    private FacesContext fctx() {
        return FacesContext.getCurrentInstance();
    }
}
