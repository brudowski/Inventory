package home.inventory.beans.data;

import home.inventory.entities.Recipe;
import home.inventory.repos.RecipeRepo;
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
public class RecipeLazyLoader extends LazyDataModel<Recipe> {

    @Inject
    private RecipeRepo recipeRepo;
    
    @Override
    public Object getRowKey(Recipe recipe) {
        return recipe.getName();
    }
    
    @Override
    @Transactional
    public Recipe getRowData(String rowKey) {
        return recipeRepo.findBy(rowKey);
    }

    @Override
    @Transactional
    public List<Recipe> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<Recipe> recipes = recipeRepo.findAll();
        int size = recipes.size();
        this.setRowCount(size);
        if(size > pageSize){
            recipes = recipes.subList(first, first + pageSize);
        }
        return recipes;
    }
}
