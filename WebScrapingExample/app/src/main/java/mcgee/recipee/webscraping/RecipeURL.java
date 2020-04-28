package mcgee.recipee.webscraping;

public class RecipeURL {
    String name;
    String url;

    public RecipeURL(String n, String u){
        this.name = n;
        this.url = u;
    }

    public String getUrl(){
        return url;
    }

    public String getName(){
        return name;
    }
}
