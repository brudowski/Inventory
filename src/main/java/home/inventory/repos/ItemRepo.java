package home.inventory.repos;

import home.inventory.entities.Item;
import org.apache.deltaspike.data.api.Repository;

/**
 *
 * @author BRudowski
 */
@Repository
public interface ItemRepo extends InventoryRepository<Item, String>{
    
}
