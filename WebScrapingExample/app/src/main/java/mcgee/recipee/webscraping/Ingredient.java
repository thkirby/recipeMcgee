package mcgee.recipee.webscraping;

public class Ingredient {
    String name;
    float quantity;
    String measurement;
    boolean checked;

    public Ingredient(String name, float quantity, String measurement){
        this.name = name;
        this.quantity = quantity;
        this.measurement = measurement;
        this.checked = false;
    }

    public String getName(){
        return name;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    public boolean getChecked(){
        return checked;
    }

    public void setChecked(boolean checked){
        this.checked = checked;
    }

    public void setQuantity(float quantity){
        this.quantity = quantity;
    }
}


