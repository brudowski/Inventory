package home.inventory.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author BRudowski
 */
@Entity
public class Recipe implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    private String name;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE)
    private final List<Ingredient> ingredients = new ArrayList<>(); 
//    private final List<String> categories = new ArrayList<>(); //TO BE IMPLEMENTED IN A FUTURE BUILD
    private String instructions; //CLOB in database

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

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Recipe other = (Recipe) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    
}
