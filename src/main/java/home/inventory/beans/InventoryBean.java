package home.inventory.beans;

import home.inventory.beans.data.PantryItemLazyLoader;
import home.inventory.entities.PantryItem;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import home.inventory.repos.PantryItemRepo;
import java.util.ArrayList;
import java.util.List;
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
    private String name;
    private double quantity;
    private String units;
    private double quantityModifier; //used to store how much should be added or removed from an item
    private String selected;
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
        if (itemRepo.findBy(name) == null) {
            PantryItem item = new PantryItem(name, quantity, units);
            itemRepo.save(item);
            fctx().addMessage(null, new FacesMessage("Success", "Created Item " + name));
            clear();
        } else {
            fctx().addMessage(null, new FacesMessage("Failure", "An item with that name already exists"));
        }
    }

    private void modifyItemQuantity(double modifier) {
        PantryItem item = itemRepo.findBy(selected);
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
        PantryItem item = itemRepo.findBy(selected);
        //need some query to check that an item isn't part of a recipe
        itemRepo.remove(item);
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

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public LazyDataModel<PantryItem> getItems() {
        return items;
    }

    private FacesContext fctx() {
        return FacesContext.getCurrentInstance();
    }
}
