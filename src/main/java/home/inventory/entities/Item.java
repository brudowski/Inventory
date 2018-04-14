package home.inventory.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author BRudowski
 */
@Entity
public class Item implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    private String name;
    private double quantity;
    private String units; //Do I want this to be an enum?
    @OneToMany(mappedBy="item")
    private final List<Ingredient> ingredients = new ArrayList<>();

    public Item() {
    }

    public Item(String name, double quantity, String units) {
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    
}
