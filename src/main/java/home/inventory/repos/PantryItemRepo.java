package home.inventory.repos;

import home.inventory.entities.PantryItem;
import org.apache.deltaspike.data.api.Repository;

/**
 *
 * @author BRudowski
 */
@Repository
public interface PantryItemRepo extends InventoryRepository<PantryItem, String>{
    
}
