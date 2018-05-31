package home.inventory.repos;

import java.io.Serializable;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.criteria.CriteriaSupport;

/**
 * A generic EntityRepository that can be extended for all entities in the
 * current project and makes use of the Clearable methods. Built upon the
 * Deltaspike model
 *
 * @author BRudowski
 * @param <E> An entity class
 * @param <PK> The primary key of the entity class
 */
public interface InventoryRepository<E, PK extends Serializable> extends EntityRepository<E, PK>, CriteriaSupport<E>, Clearable {

    default void saveAll(Iterable<? extends E> items) {
        items.forEach(this::save);
    }

    default void removeAll(Iterable<? extends E> items) {
        items.forEach(this::attachAndRemove);
    }
}
