package home.inventory.beans;

import home.inventory.entities.PantryItem;
import home.inventory.repos.ItemRepo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.jpa.api.transaction.Transactional;

/**
 *
 * @author BRudowski
 */
@Named
@SessionScoped
public class InventoryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private ItemRepo itemRepo;
    @Inject
    private DatabaseBean dbBean;
    private final List<PantryItem> items = new ArrayList<>();
    private String name;
    private double quantity;
    private String units;
    private double quantityModifier; //used to store how much should be added or removed from an item
    private String selected;

    public void clear() {
        name = "";
        quantity = 0;
        units = "";
        quantityModifier = 0;
        selected = "";
    }

    @Transactional
    public void createItem() {
        if (itemRepo.findBy(name) == null) {
            PantryItem item = new PantryItem(name, quantity, units);
            itemRepo.save(item);
            fctx().addMessage(null, new FacesMessage("Success", "Created Item " + name));
            clear();
            refreshItems();
        } else {
            fctx().addMessage(null, new FacesMessage("Failure", "An item with that name already exists"));
        }
    }

    private void modifyItemQuantity(double modifier) {
        PantryItem item = itemRepo.findBy(name);
        if (item != null) {
            item.setQuantity(item.getQuantity() + modifier);
            itemRepo.save(item);
            fctx().addMessage(null, new FacesMessage("Success", "Added Item " + name));
            clear();
            refreshItems();
        }
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
        PantryItem item = itemRepo.findBy(name);
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

    public List<PantryItem> getItems() {
        return items;
    }

    private FacesContext fctx() {
        return FacesContext.getCurrentInstance();
    }

    @Transactional
    public void refreshItems() {
        items.clear();
        items.addAll(itemRepo.findAll());
    }
}
