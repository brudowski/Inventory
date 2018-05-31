package home.inventory.repos;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.apache.deltaspike.data.spi.DelegateQueryHandler;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;

/**
 * Adds a method to flush and clear the EntityManager
 * @author BRudowski
 */
@TransactionScoped
public class ClearableImpl implements Clearable, DelegateQueryHandler{

    @Inject
    private EntityManager em;
    
    /**
     * Calls flush() and clear() on the injected EntityManager
     */
    @Override
    public void flushAndClear() {
        em.flush();
        em.clear();
    }
}
