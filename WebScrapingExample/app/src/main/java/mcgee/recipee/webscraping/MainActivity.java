package mcgee.recipee.webscraping;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mcgee.recipee.webscraping.data.GetSimilar;


public class MainActivity extends AppCompatActivity{

    private Button getBtn;
    private Button addOne;
    private TextView result;
    private ListView mDrawerList, suggestedRList, savedRList;
    private ArrayAdapter<String> mAdapter;
    private ArrayAdapter<String> mAdapter2;
    private ArrayList<String> savedRecipes;
    private List<String> suggestedRecipes;

    private GetSimilar suggestRecipes;
    //RelativeLayout rl =new RelativeLayout(this);
    public ArrayList<Ingredient> arrayList = new ArrayList<>();
    CustomAdapter arrayAdapter;
    AddIndividualDialogue addIndividualDialogue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("me", "my");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrayAdapter = new CustomAdapter(arrayList, this);


        final ListView list = findViewById(R.id.list_view); //This might need to be moved into setNavDrawer
        list.setAdapter(arrayAdapter);
        setNavDrawer();

        suggestRecipes = new GetSimilar();
        savedRecipes = new ArrayList<String>();
    }


    private void addDrawerItems() {
        mDrawerList = (ListView)findViewById(R.id.NavList);
        String[] osArray = { "My Shopping List                                           ",
                             "My Current Recipes                                         ",
                             "                                  ",
                             "Add New Ingredient                                         ",
                             "Add New Recipe                                             ",
                             "                                  ",
                             "Suggested Recipes                                          ",
                             "My Saved Shopping Lists                                    "};
        mAdapter = new ArrayAdapter<String>(this, R.layout.navlist, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    private void setNavDrawer(){


        addDrawerItems();
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        setLayoutMain();
                        break;
                    case 1:
                        setLayoutRecipes();
                        break;
                    case 3:
                        addIndividualIngredient();
                        break;
                    case 4:
                        getURL();
                        break;
                    case 6:
                        setLayoutSuggested();
                        break;
                }
            }
        });
    }


    private void setLayoutMain(){
        setContentView(R.layout.activity_main);
        final ListView list = findViewById(R.id.list_view);
        list.setAdapter(arrayAdapter);
        setNavDrawer();

    }

    private void setLayoutSuggested(){
        setContentView(R.layout.display_urls);
        List<String> temp = new ArrayList<>();
        for (Ingredient ingredient : arrayList){
            temp.add(ingredient.getName());
        }
        suggestedRecipes = suggestRecipes.GetLinks(temp);
        List<String> suggestedRecipesNames = new ArrayList<>();
        for (String url:suggestedRecipes){
            try {
                Document doc = Jsoup.connect(url).get();
                suggestedRecipesNames.add(doc.title());}
            catch (IOException e) {
                ;
            }
        } // Add titles instead of Links
        mAdapter2 = new ArrayAdapter<String>(this, R.layout.urldisplay, suggestedRecipesNames);
        suggestedRList = findViewById(R.id.urlList);
        suggestedRList.setAdapter(mAdapter2);
        setNavDrawer();
        suggestedRList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Uri uri = Uri.parse(suggestedRecipes.get(i));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

    private void setLayoutRecipes(){
        setContentView(R.layout.display_urls);
        List<String> temp = new ArrayList<>();

        List<String> savedRecipesNames = new ArrayList<>();
        for (String url:savedRecipes){
            try {
                Document doc = Jsoup.connect(url).get();
                savedRecipesNames.add(doc.title());}
            catch (IOException e) {
                ;
            }
        } // Add titles instead of Links
        mAdapter2 = new ArrayAdapter<String>(this, R.layout.urldisplay, savedRecipesNames);
        savedRList = findViewById(R.id.urlList);
        savedRList.setAdapter(mAdapter2);
        setNavDrawer();
        savedRList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Uri uri = Uri.parse(savedRecipes.get(i));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }


    private void getWebsite(String url) {
        Log.d("getWebsite", "getWebsite: url: " + url);
        new Thread(() -> {
            final StringBuilder builder = new StringBuilder();

            try {
                Document doc = Jsoup.connect(url).get();
                String title = doc.title();
                savedRecipes.add(url);
                List<String> ingredients = Parser.findVar(doc.toString());
                builder.append(title).append("\n");

                String[] splitStr;
                for (String ingredient : ingredients) {
                    Log.d("abrgndscx", "Testing an Ingredient: " + ingredient);
                    splitStr = Splitter.splitIngr(ingredient);
                    Log.d("dfdssdf", splitStr[0] +" , "+splitStr[1] +" , " + splitStr[2]);
                    boolean repeatedIngr = false;
                    for (Ingredient ingr : arrayList){
                        if (splitStr[0].equals(ingr.getName())){
                            Log.d("abrgndscx", "Found a match for " + ingredient);
                            ingr.setQuantity(ingr.getQuantity() + Float.parseFloat(splitStr[1]));
                            repeatedIngr = true;
                            break;
                        }
                    }
                    if (!repeatedIngr && !splitStr[1].equals("")) {
                        Log.d("abrgndscx", "Adding: " + ingredient);
                        arrayList.add(new Ingredient(splitStr[0], Float.parseFloat(splitStr[1]), splitStr[2]));
                    }

                }


            } catch (IOException e) {
                builder.append("Error : ").append(e.getMessage()).append("\n");
            }

            runOnUiThread(() -> {
                arrayAdapter.notifyDataSetChanged();
            });
        }).start();

    }

    private void addIndividualIngredient(){
        AddIndividualDialogue dialogue;
        dialogue = new AddIndividualDialogue(this);
        dialogue.create();
        dialogue.show();
        dialogue.setDialogueResult(retIngr -> {
            boolean repeatedIngr = false;
            for (Ingredient ingr : arrayList){
                if (retIngr.getName().equals(ingr.getName())){
                    ingr.setQuantity(ingr.getQuantity() + retIngr.getQuantity());
                    repeatedIngr = true;
                    break;
                }
            }
            if (!repeatedIngr) {
                arrayList.add(retIngr);
            }
            arrayAdapter.notifyDataSetChanged();
        });


    }



    private void getURL(){
        GetURLDialogue dialogue;
        dialogue = new GetURLDialogue(this);
        dialogue.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogue.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogue.show();
        dialogue.setDialogueResult(retStr -> {
            getWebsite(retStr);
        });

    }


    private Elements getIngredients(Document doc){

        List<String> queries = Arrays.asList("li[class=ingredients-item-name]","li[class=ingredients-item]",
                "li[class=o-Ingredients__a-Ingredient]", "li[class=recipeIngredient]");

        Elements ingredients = doc.select("[type='application/ld+json']");
        try {
            Elements ret_elements = new Elements();
            JSONObject reader = new JSONObject(Splitter.onlyCurlyBracket(ingredients.toString()));
            JSONArray listElement = reader.getJSONArray("@graph");

            JSONArray ingrs = new JSONArray();
            for (int i=0; i < listElement.length(); i++){
                Log.d("sdfdsf", " " + listElement.getJSONObject(i).toString());
                if (listElement.getJSONObject(i).getString("@type").equals("Recipe")){
                    ingrs = listElement.getJSONObject(i).getJSONArray("recipeIngredient");
                    for (int j=0; j<ingrs.length(); j++){
                        Element toAdd = new Element(ingrs.getString(j));
                        ret_elements.add(toAdd);
                    }
                    return ret_elements;
                }
            }
        } catch (JSONException e) {
            Log.d("dsgsdgfsd", "shit is fucked, yo");
            e.printStackTrace();
        }

        ingredients = new Elements();
        for (String query : queries){
            if (ingredients.isEmpty()){
                ingredients = doc.select(query);
            }
            else{
                return ingredients;
            }
        }

        return ingredients;
    }


}
