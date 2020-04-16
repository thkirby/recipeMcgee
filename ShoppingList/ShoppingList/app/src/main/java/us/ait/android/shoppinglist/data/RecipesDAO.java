package us.ait.android.shoppinglist.data;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RecipesDAO {
    @Query("SELECT * FROM Recipes")
    List<Recipe> getAll();

    @Insert()
    long insertRecipe(Recipe recipe);

    @Delete()
    void delete(Recipe recipe);

    @Query("DELETE FROM item")
    void deleteAll();

    @Update()
    void update(Recipe recipe);
}
}