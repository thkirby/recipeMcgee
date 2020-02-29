package mcgee.recipee.webscraping;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import mcgee.recipee.webscraping.data.AppDatabase;
import mcgee.recipee.webscraping.data.Recipe;


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
        getBtn.setOnClickListener(view -> getWebsite());
        addOne = findViewById(R.id.addIndividualButton);
        addOne.setOnClickListener(view -> addIndividualIngredient());
        final ListView list = findViewById(R.id.list_view);
        arrayAdapter = new CustomAdapter(arrayList, this);
        list.setAdapter(arrayAdapter);

    }

    private void getWebsite() {
        new Thread(() -> {
            final StringBuilder builder = new StringBuilder();

            try {
                String url = "https:/www.allrecipes.com/recipe/255239/hot-chicken-casserole/";
                Document doc = Jsoup.connect(url).get();
                String title = doc.title();
                Elements ingredients = doc.select("li[class=ingredients-item]");

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


}
