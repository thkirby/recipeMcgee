package mcgee.recipee.webscraping;

import android.content.Intent;
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
    public ArrayList<String> arrayList = new ArrayList<>();

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
        Button next = (Button) findViewById(R.id.button2);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Main2Activity.class);
                startActivityForResult(myIntent, 0);
            }
        });


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listitem, arrayList);
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

                    for (Element ingredient : ingredients) {
                        //CheckBox ch = new CheckBox(getApplicationContext());
                        //ch.setText(builder.append("\n").append(ingredient.text()));
                        arrayList.add(ingredient.text());
                    }
                } catch (IOException e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(builder.toString());
                    }
                });
            }
        }).start();
    }
}
