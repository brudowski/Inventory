package home.inventory.repos;

import home.inventory.entities.Ingredient;
import home.inventory.entities.IngredientKey;
import org.apache.deltaspike.data.api.QueryResult;
import org.apache.deltaspike.data.api.Repository;

/**
 *
 * @author BRudowski
 */
@Repository
public interface IngredientRepo extends InventoryRepository<Ingredient, IngredientKey>{
    
    QueryResult<Ingredient> findAllByItemName(String itemName);
    QueryResult<Ingredient> findAllByRecipeName(String recipeName);
}
