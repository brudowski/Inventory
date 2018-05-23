package home.inventory.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author BRudowski
 */
@Entity
public class Ingredient extends SeqIdEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @ManyToOne
    @JoinColumn(name="PANTRYITEM", referencedColumnName="ID")
    private PantryItem pantryItem;
    private double quantity;
    @ManyToOne
    @JoinColumn(name="RECIPE", referencedColumnName="ID")
    private Recipe recipe;

    public Ingredient() {
    }

    public Ingredient(PantryItem item, double quantity, Recipe recipe) {
        this.pantryItem = item;
        this.quantity = quantity;
        this.recipe = recipe;
    }
    
    public PantryItem getPantryItem() {
        return pantryItem;
    }

    public void setPantryItem(PantryItem pantryItem) {
        this.pantryItem = pantryItem;
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
}
