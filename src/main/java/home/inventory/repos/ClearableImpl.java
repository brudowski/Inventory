/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.inventory.repos;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.apache.deltaspike.data.spi.DelegateQueryHandler;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;

/**
 *
 * @author BRudowski
 */
@TransactionScoped
public class ClearableImpl implements Clearable, DelegateQueryHandler{

    @Inject
    private EntityManager em;
    
    @Override
    public void flushAndClear() {
        em.flush();
        em.clear();
    }
}
