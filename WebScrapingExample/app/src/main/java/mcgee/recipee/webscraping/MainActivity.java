package mcgee.recipee.webscraping;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mcgee.recipee.webscraping.data.AppDatabase;
import mcgee.recipee.webscraping.data.Recipe;


public class MainActivity extends AppCompatActivity {

    private Button getBtn;
    private Button addOne;
    private TextView result;
    //RelativeLayout rl =new RelativeLayout(this);
    public ArrayList<Ingredient> arrayList = new ArrayList<>();
    CustomAdapter arrayAdapter;
    AddIndividualDialogue addIndividualDialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);
        getBtn = findViewById(R.id.getBtn);
        getBtn.setOnClickListener(view -> getURL());
        addOne = findViewById(R.id.addIndividualButton);
        addOne.setOnClickListener(view -> addIndividualIngredient());
        final ListView list = findViewById(R.id.list_view);
        arrayAdapter = new CustomAdapter(arrayList, this);
        list.setAdapter(arrayAdapter);

    }

    private void getWebsite(String url) {
        Log.d("getWebsite", "getWebsite: url: " + url);
        new Thread(() -> {
            final StringBuilder builder = new StringBuilder();

            try {
                Document doc = Jsoup.connect(url).get();
                String title = doc.title();
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
                result.setText(builder.toString());
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
