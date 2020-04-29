package mcgee.recipee.webscraping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Main3Activity extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();
    }
    private void addDrawerItems() {
        String[] osArray = { "My Shopping List", "Find a Recipe", "My Saved Shopping Lists"};
        mAdapter = new ArrayAdapter<String>(this, R.layout.navlist, osArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0) {
                    Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                    startActivityForResult(myIntent, 0);
                }
                else if (position==1) {
                    Intent myIntent = new Intent(view.getContext(), Main2Activity.class);
                    startActivityForResult(myIntent, 0);
                }
                else if (position==2) {
                    Intent myIntent = new Intent(view.getContext(), Main3Activity.class);
                    startActivityForResult(myIntent, 0);
                }

            }
        });
    }
}
