package mcgee.recipee.webscraping;

public class Ingredient {
    String name;
    float quantity;
    String measurement;

    public Ingredient(String name, float quantity, String measurement){
        this.name = name;
        this.quantity = quantity;
        this.measurement = measurement;
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
}


