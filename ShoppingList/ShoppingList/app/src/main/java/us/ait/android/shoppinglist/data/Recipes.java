package us.ait.android.shoppinglist.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import us.ait.android.shoppinglist.R;

@Entity(tableName = "Recipes")
public class Recipes {


    @PrimaryKey(autoGenerate = true)
    private int rid;

    @ColumnInfo(name = "recipe_name")
    public String recipeName;

    @ColumnInfo(name = "ingredient_list")
    public String ingredientList[];

    @ColumnInfo(name = "recipe_link")
    public String recipeLink;

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String[] getIngredientList() {
        return ingredientList;
    }

    public String getRecipeLink() {
        return recipeLink;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setIngredientList(String[] ingredientList) {
        this.ingredientList = ingredientList;
    }

    public void setRecipeLink(String recipeLink) {
        this.recipeLink = recipeLink;
    }
}

