package mcgee.recipee.webscraping.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Entity
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private int recipeId;

    @ColumnInfo(name = "customerid")
    private String customerId;

    @ColumnInfo(name = "ingredient")
    private Elements ingredients;

    @ColumnInfo(name = "recipe")
    private String recipeLink;

    public Recipe(Elements ingredients, String recipeLink) {
        this.customerId = customerId;
        this.ingredients = ingredients;
        this.recipeLink = recipeLink;
    }

    public int getIngredientId() {
        return recipeId;
    }

    public String getRecipeLink() { return recipeLink; }

    public String getCustomerId() {
        return customerId;
    }

    public Elements getIngredient() {
        return ingredients;
    }

    public void setIngredientId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setStudentId(String customerId) {
        this.customerId = customerId;
    }

    public void setGrade(Elements ingredients) {
        this.ingredients = ingredients;
    }


}