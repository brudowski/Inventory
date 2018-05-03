package home.inventory.repos;

import home.inventory.entities.PantryItem;
import org.apache.deltaspike.data.api.Repository;

/**
 *
 * @author BRudowski
 */
@Repository
public interface ItemRepo extends InventoryRepository<PantryItem, String>{
    
}
