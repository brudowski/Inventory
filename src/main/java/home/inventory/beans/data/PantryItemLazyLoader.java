package home.inventory.beans.data;

import home.inventory.entities.PantryItem;
import home.inventory.repos.PantryItemRepo;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author BRudowski
 */
@Dependent
public class PantryItemLazyLoader extends LazyDataModel<PantryItem> {

    @Inject
    private PantryItemRepo itemRepo;
    
    @Override
    public Object getRowKey(PantryItem item) {
        return item.getName();
    }

    @Override
    @Transactional
    public List<PantryItem> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<PantryItem> items = itemRepo.findAll();
        int size = items.size();
        this.setRowCount(size);
        if(size > pageSize){
            items = items.subList(first, first + pageSize);
        }
        return items;
    }
}
