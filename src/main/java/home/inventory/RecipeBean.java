package home.inventory;

import home.inventory.entities.Ingredient;
import home.inventory.entities.IngredientKey;
import home.inventory.entities.Item;
import home.inventory.entities.Recipe;
import home.inventory.enums.Unit;
import io.vavr.collection.Stream;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

/**
 *  
 * @author BRudowski
 */

@Named
public class RecipeBean {

    @Inject
    private EntityManager em;
    private String recipeName;
    private String itemName;
    private double quantity;
    private String selectedRecipe;
    private String selectedItem;
    private String unit;

    private final List<Recipe> recipes = new ArrayList<>();

    @Transactional
    public void createRecipe() {
        Recipe recipe = new Recipe(recipeName);
        em.persist(recipe);
    }
    
    @Transactional
    public void addIngredientToRecipe() {
        Recipe recipe = getRecipe(); //get recipe from database using selectedRecipe
        Item item = em.find(Item.class, selectedItem);
        if (item == null) {
            item = new Item(itemName, 0.0, unit);
            em.persist(item);
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
            recipe.getIngredients().add(ingredient);
            em.persist(ingredient);
        } else {
            final String ingredientName = item.getName();
            ingredient = Stream.ofAll(recipe.getIngredients())
                    .filter(i -> i.getItem().getName().equals(ingredientName))
                    .getOrElse(ingredient);
            ingredient.setQuantity(quantity);
            em.merge(ingredient);
        }
    }
    
    public void removeIngredientFromRecipe() {
        Recipe recipe = getRecipe();
        Ingredient ingredient = em.find(Ingredient.class, new IngredientKey(recipeName, itemName));
        recipe.getIngredients().remove(ingredient);
        em.merge(recipe);
        em.remove(ingredient);
    }
    
    @Transactional
    public void deleteRecipe() {
        Recipe recipe = getRecipe();
        em.remove(recipe);
    }

    
    private Recipe getRecipe() {
        return em.find(Recipe.class, recipeName);
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
