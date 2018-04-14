package home.inventory.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author BRudowski
 */
public class IngredientKey implements Serializable, Comparable<IngredientKey> {

    private static final long serialVersionUID = 1L;
    private String recipe;
    private String item;

    public IngredientKey() {
    }

    public IngredientKey(String recipe, String item) {
        this.recipe = recipe;
        this.item = item;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.recipe);
        hash = 97 * hash + Objects.hashCode(this.item);
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
        final IngredientKey other = (IngredientKey) obj;
        if (!Objects.equals(this.recipe, other.recipe)) {
            return false;
        }
        if (!Objects.equals(this.item, other.item)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(IngredientKey o) {
        int c = recipe.compareTo(recipe);
        if (c == 0) {
            c = item.compareTo(item);
        }
        return c;
    }

}
