package mcgee.recipee.webscraping.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Entity
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    public int recipeId;

    @ColumnInfo(name = "customerid")
    private String customerId;

    @ColumnInfo(name = "ingredient")
    private String ingredients;

    @ColumnInfo(name = "recipe")
    private String recipeLink;

    public Recipe(String customerId, String ingredients, String recipeLink) {
        this.customerId = customerId;
        this.ingredients = ingredients;
        this.recipeLink = recipeLink;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getRecipeLink() { return recipeLink; }

    public String getCustomerId() {
        return customerId;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }


}