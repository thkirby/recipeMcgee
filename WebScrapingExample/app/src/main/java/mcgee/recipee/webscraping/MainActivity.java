package mcgee.recipee.webscraping;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button getBtn;
    private TextView result;
    //RelativeLayout rl =new RelativeLayout(this);
    public ArrayList<Ingredient> arrayList = new ArrayList<>();
    CustomAdapter arrayAdapter;

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
                    Document doc = Jsoup.connect("https:/www.allrecipes.com/recipe/255239/hot-chicken-casserole/").get();
                    String title = doc.title();
                    Elements ingredients = doc.select("li[class=ingredients-item]");

                    builder.append(title).append("\n");

                    String[] splitStr = new String[2];
                    for (Element ingredient : ingredients) {
                        //CheckBox ch = new CheckBox(getApplicationContext());
                        //ch.setText(builder.append("\n").append(ingredient.text()));
                        splitStr = Splitter.splitIngr(ingredient.text());

                        arrayList.add(new Ingredient(splitStr[0], Float.parseFloat(splitStr[1]), splitStr[2]));

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
}
