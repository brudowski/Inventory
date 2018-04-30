package home.inventory.repos;

import java.io.Serializable;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 *
 * @author BRudowski
 * @param <E> An entity class
 * @param <PK> The primary key of the entity class
 */
public interface InventoryRepository<E, PK extends Serializable> extends EntityRepository<E, PK>, CriteriaSupport<E> {

    default void saveAll(Iterable<? extends E> items) {
        items.forEach(this::save);
    }

    default void removeAll(Iterable<? extends E> items) {
        items.forEach(this::remove);
    }
}
