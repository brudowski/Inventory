package home.inventory.repos;

import home.inventory.entities.Recipe;
import org.apache.deltaspike.data.api.Repository;

/**
 *
 * @author BRudowski
 */
@Repository
public interface RecipeRepo extends InventoryRepository<Recipe, String>{
    
    Recipe findOptionalByName(String name);
}
