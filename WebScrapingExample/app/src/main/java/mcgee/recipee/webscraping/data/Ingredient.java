package mcgee.recipee.webscraping.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    private int ingredientId;

    @ColumnInfo(name = "studentid")
    private String customerId;

    @ColumnInfo(name = "grade")
    private String ingredient;

    public Ingredient(String studentId, String ingredient) {
        this.customerId = studentId;
        this.ingredient = ingredient;
    }

    public int getGradeId() {
        return ingredientId;
    }

    public String getStudentId() {
        return customerId;
    }

    public String getGrade() {
        return ingredient;
    }

    public void setGradeId(int gradeId) {
        this.ingredientId = ingredientId;
    }

    public void setStudentId(String studentId) {
        this.customerId = customerId;
    }

    public void setGrade(String grade) {
        this.ingredient = ingredient;
    }
}