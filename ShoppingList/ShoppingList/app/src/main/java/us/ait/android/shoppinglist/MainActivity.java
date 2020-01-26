package us.ait.android.shoppinglist;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.List;

import us.ait.android.shoppinglist.adapter.ItemAdapter;
import us.ait.android.shoppinglist.data.AppDatabase;
import us.ait.android.shoppinglist.data.Item;
import us.ait.android.shoppinglist.dialog.CreateAndEditItemDialog;
import us.ait.android.shoppinglist.dialog.EnterUrlDialog;
import us.ait.android.shoppinglist.touch.ItemListTouchHelperCallback;

public class MainActivity extends AppCompatActivity implements CreateAndEditItemDialog.ItemHandler{

    public static final String KEY_EDIT = "KEY_EDIT";
    private ItemAdapter itemAdapter;
    private CoordinatorLayout layoutContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutContent = findViewById(R.id.layoutContent);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateItemDialog();
            }
        });

        Button inputUrl = findViewById(R.id.inputUrl);
        inputUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnterUrlDialog();
            }
        });


        RecyclerView recyclerViewItems = findViewById(
                R.id.recyclerViewItems);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        initItems(recyclerViewItems);
    }

    private void initItems(final RecyclerView recyclerView) {
        new Thread() {
            @Override
            public void run() {
                final List<Item> items = AppDatabase.getAppDatabase(MainActivity.this).itemDao().getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeRecyclerView(items, recyclerView);
                    }
                });
            }
        }.start();
    }

    private void makeRecyclerView(List<Item> items, RecyclerView recyclerView) {
        itemAdapter = new ItemAdapter(items, MainActivity.this);
        recyclerView.setAdapter(itemAdapter);
        ItemListTouchHelperCallback touchHelperCallback = new ItemListTouchHelperCallback(
                itemAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(
                touchHelperCallback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void showCreateItemDialog() {
        new CreateAndEditItemDialog().show(getSupportFragmentManager(), "CreateAndEditItemDialog");
    }

    private void showEnterUrlDialog(){
        new EnterUrlDialog().show(getSupportFragmentManager(), "EnterUrlDialog");
    }

    public void showEditItemDialog(Item item) {
        CreateAndEditItemDialog editItemDialog = new CreateAndEditItemDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_EDIT, item);
        editItemDialog.setArguments(bundle);

        editItemDialog.show(getSupportFragmentManager(), "EditDialog");
    }

    private void showSnackBarMessage(String message) {
        Snackbar.make(layoutContent,
                message,
                Snackbar.LENGTH_LONG
        ).setAction(R.string.action_hide, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...
            }
        }).show();
    }

    @Override
    public void onNewItemCreated(final Item item) {
        new Thread() {
            @Override
            public void run() {
                long id = AppDatabase.getAppDatabase(MainActivity.this).
                        itemDao().insertItem(item);
                item.setItemID(id);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemAdapter.addItem(item);
                        showSnackBarMessage(getString(R.string.new_item_added));
                    }
                });
            }
        }.start();
    }

    @Override
    public void onItemUpdated(final Item item) {
        new Thread() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(MainActivity.this).itemDao().update(item);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemAdapter.updateItem(item);
                    }
                });
            }
        }.start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_add:
                showCreateItemDialog();
                return true;
            case R.id.action_delete_all:
                return runDeleteAllThread();
            case R.id.action_price:
                String totalPrice = Double.toString(itemAdapter.getTotalCost());
                showSnackBarMessage(getString(R.string.total_cost_message)+" "+totalPrice);
                return true;
        }
        return true;
    }

    private boolean runDeleteAllThread() {
        new Thread() {
            @Override
            public void run() {
                AppDatabase.getAppDatabase(MainActivity.this).itemDao().deleteAll();
            }
        }.start();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                itemAdapter.clear();
            }
        });

        return true;
    }


}
