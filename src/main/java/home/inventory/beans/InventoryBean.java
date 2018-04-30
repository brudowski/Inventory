package home.inventory.beans;

import home.inventory.entities.Item;
import home.inventory.repos.ItemRepo;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

/**
 *
 * @author BRudowski
 */
@ApplicationScoped
@Named
public class InventoryBean {

    @Inject
    private ItemRepo itemRepo;
    private final List<Item> items = new ArrayList<>();
    private String name;
    private double quantity;
    private String units;
    private double quantityModifier; //used to store how much should be added or removed from an item
    private String selected;

    private void clear() {
        name = "";
        quantity = 0;
        units = "";
        quantityModifier = 0;
        selected = "";
    }

    @Transactional
    public void createItem() {
        if (itemRepo.findBy(name) == null) {
            Item item = new Item(name, quantity, units);
            itemRepo.save(item);
            clear();
        } else {
            //send some warning/error message to the user
        }
    }

    @Transactional
    private void modifyItemQuantity(double modifier) {
        Item item = itemRepo.findBy(name);
        item.setQuantity(item.getQuantity() + modifier);
        itemRepo.save(item);
    }

    public void addQuantity() {
        modifyItemQuantity(quantityModifier);
    }

    public void removeQuantity() {
        modifyItemQuantity(-quantityModifier);
    }

    @Transactional
    public void deleteItem() {
        Item item = itemRepo.findBy(name);
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

    public List<Item> getItems() {
        return items;
    }

}
