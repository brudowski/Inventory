package home.inventory;

import home.inventory.entities.Item;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BRudowski
 */
public class InventoryBean {
    
    //need a database accessor for Items
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
    
    public void createItem() {
        Item item = new Item(name, quantity, units);
        //save item
        clear();
    }
    
    private void modifyItemQuantity(double modifier) {
        Item item; //pull from database using "selected" variable
        item.setQuantity(item.getQuantity()+modifier);
        //save item
    }
    
    public void addQuantity() {
        modifyItemQuantity(quantityModifier);
    }
    
    public void removeQuantity() {
        modifyItemQuantity(-quantityModifier);
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
