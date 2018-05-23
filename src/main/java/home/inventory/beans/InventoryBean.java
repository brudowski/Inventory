package home.inventory.beans;

import home.inventory.beans.data.PantryItemLazyLoader;
import home.inventory.entities.PantryItem;
import home.inventory.repos.IngredientRepo;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import home.inventory.repos.PantryItemRepo;
import javax.enterprise.context.SessionScoped;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author BRudowski
 */
@Named
@SessionScoped
public class InventoryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private PantryItemRepo itemRepo;
    @Inject
    private IngredientRepo ingredientRepo;
    private String name;
    private double quantity;
    private String units;
    private double quantityModifier; //used to store how much should be added or removed from an item
    private PantryItem selected;
    private String newName;
    @Inject
    private PantryItemLazyLoader items;

    public void clear() {
        name = "";
        quantity = 0;
        units = "";
        quantityModifier = 0;
        selected = null;
    }

    @Transactional
    public void createItem() {
        if (itemRepo.findOptionalByName(name) == null) {
            PantryItem item = new PantryItem(name, quantity, units);
            itemRepo.save(item);
            fctx().addMessage(null, new FacesMessage("Success", "Created Item " + name));
            clear();
        } else {
            fctx().addMessage(null, new FacesMessage("Failure", "An item with that name already exists"));
        }
    }
    
    @Transactional
    public void renameItem() {
        PantryItem item = itemRepo.findOptionalByName(selected.getName());
        item.setName(newName);
        itemRepo.save(item);
        clear();
    }

    private void modifyItemQuantity(double modifier) {
        PantryItem item = itemRepo.findOptionalByName(selected.getName());
        String action = "";
        String result = "Error";
        if (item != null) {
            if (modifier + item.getQuantity() >= 0) {
                item.setQuantity(item.getQuantity() + modifier);
                itemRepo.save(item);
                result = "Success";
                action += modifier > 0 ? "Added " : "Removed "
                        + String.valueOf(Math.abs(modifier)) + " "
                        + item.getUnits() + (modifier > 0 ? " to " : " from ")
                        + item.getName();
                clear();
            } else {
                action = "Cannot remove more " + item.getUnits() + "(s) than what is there for " + item.getName();
            }
        } else {
            action = "Unable to modify item quantity";
        }
        fctx().addMessage(null, new FacesMessage(result, action));
        clear();
    }

    @Transactional
    public void addQuantity() {
        modifyItemQuantity(quantityModifier);
    }

    @Transactional
    public void removeQuantity() {
        modifyItemQuantity(-quantityModifier);
    }

    @Transactional
    public void deleteItem() {
        PantryItem item = itemRepo.findOptionalByName(selected.getName());
        if(item.getIngredients().isEmpty()){
            itemRepo.remove(item);
            fctx().addMessage(null, new FacesMessage("Success", "Deleted "+item.getName()));
            clear();
        } else {
            fctx().addMessage(null, new FacesMessage("Error", "Selected item has associated ingredients in recipes. Please remove them from those recipes first"));
        }
    }
    @Transactional
    public void updateUnits() {
        PantryItem item = itemRepo.findOptionalByName(selected.getName());
        item.setUnits(units);
        itemRepo.save(item);
        clear();
        fctx().addMessage(null, new FacesMessage("Success", "Updated units to "+units));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public double getQuantityModifier() {
        return quantityModifier;
    }

    public void setQuantityModifier(double quantityModifier) {
        this.quantityModifier = quantityModifier;
    }

    public PantryItem getSelected() {
        return selected;
    }

    public void setSelected(PantryItem selected) {
        this.selected = selected;
    }

    public LazyDataModel<PantryItem> getItems() {
        return items;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    private FacesContext fctx() {
        return FacesContext.getCurrentInstance();
    }
}
