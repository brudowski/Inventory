package home.inventory.entities;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

/**
 *
 * @author BRudowski
 */
@Entity
public class PantryItem extends SeqIdEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String name;
    private double quantity;
    private String units;
    @OneToMany(mappedBy="pantryItem")
    private final List<Ingredient> ingredients = new ArrayList<>();

    public PantryItem() {
    }

    public PantryItem(String name, double quantity, String units) {
        this.name = name;
        this.quantity = quantity;
        this.units = units;
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

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
