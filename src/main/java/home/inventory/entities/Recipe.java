package home.inventory.entities;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

/**
 *
 * @author BRudowski
 */
@Entity
public class Recipe extends SeqIdEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String name;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE)
    private final List<Ingredient> ingredients = new ArrayList<>(); 

    public Recipe() {
    }

    public Recipe(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }    
}
