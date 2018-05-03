package home.inventory.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

/**
 *
 * @author BRudowski
 */
@Entity
@IdClass(IngredientKey.class)
public class Ingredient implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @ManyToOne
    private PantryItem item;
    private double quantity;
    @Id
    @ManyToOne
    private Recipe recipe;

    public Ingredient() {
    }

    public Ingredient(PantryItem item, double quantity, Recipe recipe) {
        this.item = item;
        this.quantity = quantity;
        this.recipe = recipe;
    }
    
    public PantryItem getItem() {
        return item;
    }

    public void setItem(PantryItem item) {
        this.item = item;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.item);
        hash = 79 * hash + Objects.hashCode(this.recipe);
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
        final Ingredient other = (Ingredient) obj;
        if (!Objects.equals(this.item, other.item)) {
            return false;
        }
        if (!Objects.equals(this.recipe, other.recipe)) {
            return false;
        }
        return true;
    }




    
}
