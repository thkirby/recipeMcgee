package mcgee.recipee.webscraping;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button getBtn;
    private ImageButton addOne;
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
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWebsite();
            }
        });
        addOne = findViewById(R.id.addIndividualButton);
        addOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIndividualIngredient();
            }
        });
        final ListView list = findViewById(R.id.list_view);
        arrayAdapter = new CustomAdapter(arrayList, this);
        list.setAdapter(arrayAdapter);

    }

    private void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect("https://www.allrecipes.com/recipe/10549/best-brownies/").get();
                    String title = doc.title();
                    Elements ingredients = getIngredients(doc);

                    builder.append(title).append("\n");

                    String[] splitStr;
                    for (Element ingredient : ingredients) {
                        splitStr = Splitter.splitIngr(ingredient.text());
                        splitStr[0] = splitStr[0].trim().replace("[\n\r]", "");
                        boolean repeatedIngr = false;
                        for (Ingredient ingr : arrayList){
                            if (splitStr[0].equals(ingr.getName())){
                                ingr.setQuantity(ingr.getQuantity() + Float.parseFloat(splitStr[1]));
                                repeatedIngr = true;
                                break;
                            }
                        }
                        if (!repeatedIngr) {
                            arrayList.add(new Ingredient(splitStr[0], Float.parseFloat(splitStr[1]), splitStr[2]));
                        }

                    }


                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(builder.toString());
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }

    private void addIndividualIngredient(){
        AddIndividualDialogue dialogue;
        dialogue = new AddIndividualDialogue(this);
        dialogue.create();
        dialogue.show();
        dialogue.setDialogueResult(new AddIndividualDialogue.OnAddItemResult() {
            @Override
            public void finish(Ingredient retIngr) {
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
            }
        });

    }

    private Elements getIngredients(Document doc){

        List<String> queries = Arrays.asList("li[class=ingredients-item]",
                                             "li[class=recipeIngredient]");

        Elements ingredients = doc.select("li[class=ingredients-item]");
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
