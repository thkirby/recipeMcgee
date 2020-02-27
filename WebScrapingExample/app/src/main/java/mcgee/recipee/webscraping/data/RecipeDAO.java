package mcgee.recipee.webscraping.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface RecipeDAO {

    @Insert
    void insertRecipe(Recipe recipe);

    @Delete
    void deleteRecipe(Recipe recipe);
}