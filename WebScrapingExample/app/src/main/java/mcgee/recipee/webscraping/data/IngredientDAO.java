package mcgee.recipee.webscraping.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface IngredientDAO {

    @Query("SELECT * FROM ingredient WHERE ingredientId = :ingredient")
    List<Ingredient> getSpecificGrades(String ingredient);

    @Query("SELECT * FROM ingredient ")
    List<Ingredient> getAllGrades();

    @Insert
    void insertGrades(Ingredient... ingredients);

    @Delete
    void deleteGrade(Ingredient ingredient);
}