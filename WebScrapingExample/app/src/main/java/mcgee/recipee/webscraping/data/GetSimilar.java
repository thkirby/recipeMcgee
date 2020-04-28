package mcgee.recipee.webscraping.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import mcgee.recipee.webscraping.Parser;
import mcgee.recipee.webscraping.Splitter;

//A class to get a list of recipes with similar ingredients to the current grocery list
public class GetSimilar {

    private List<String> sites; //TODO: Make this actually communicate with the database

    private class stringInt{
        public int i;
        public String s;

        stringInt(int integer, String string){
            i = integer;
            s = string;
        }
    }

    public class stringIntComparator implements Comparator<stringInt>{
        @Override
        public int compare(stringInt x, stringInt y){
            if(x.i > y.i){
                return -1;
            }
            else if(x.i < y.i){
                return 1;
            }
            return 0;
        }
    }

    // constructor, makes sites have actual recipes in it. In an ideal world, this would sync up to a database
    public GetSimilar(){
        sites = Arrays.asList("https://www.allrecipes.com/recipe/9870/easy-sugar-cookies/", "https://www.allrecipes.com/recipe/16354/easy-meatloaf/");
    }

    private List<String> GetIngredients(String url){
        ArrayList<String> ret = new ArrayList<String>();
        try{
            Document doc = Jsoup.connect(url).get();
            List<String> ingredients = Parser.findVar(doc.toString());

            for (String ingredient : ingredients) {
                ret.add(Splitter.getNameOnly(ingredient));
            }
            return ret;
        }
        catch(IOException e) {
            return ret;
        }

    }

    public List<String> GetLinks(List<String> ingredients){
        Comparator<stringInt> comparator = new stringIntComparator();
        PriorityQueue<stringInt> temp = new PriorityQueue<>(sites.size(), comparator);
        for (String site : sites){
            int numCommon = 0; //The number of ingredients a recipe has in common
            for (String ingredient : GetIngredients(site)){
                if (ingredient.contains(ingredient)){
                    numCommon = numCommon + 1;
                }
                temp.add(new stringInt(numCommon, site));
            }


        }

        List<String> ret = new ArrayList<>();
        while (!temp.isEmpty()){
            ret.add(temp.remove().s);
        }

        return ret;
    }




}
