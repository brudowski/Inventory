package home.inventory.repos;

import home.inventory.entities.*;
import org.apache.deltaspike.data.api.*;

/**
 *
 * @author BRudowski
 */
@Repository
public interface IngredientRepo extends InventoryRepository<Ingredient, Long> {

    @Query("select i from Ingredient i where i.pantryitem=?1")
    QueryResult<Ingredient> findAllByPantryItem(PantryItem pantryItem);

    @Query("select ing from Ingredient ing where ing.recipe=?1")
    QueryResult<Ingredient> findAllByRecipe(Recipe recipe);
}
