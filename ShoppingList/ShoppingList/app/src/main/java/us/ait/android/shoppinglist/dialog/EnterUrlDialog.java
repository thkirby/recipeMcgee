/** This class is for a dialog which asks for a url and then detects if there is a list of ingredients
 *  that can be added to the list.
 *
 */

package us.ait.android.shoppinglist.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import us.ait.android.shoppinglist.R;
import us.ait.android.shoppinglist.data.Item;

public class EnterUrlDialog extends DialogFragment {

    private CreateAndEditItemDialog.ItemHandler itemHandler;
    private EditText etUrl;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CreateAndEditItemDialog.ItemHandler) {
            itemHandler = (CreateAndEditItemDialog.ItemHandler) context;
        } else {
            throw new RuntimeException(getString(R.string.error_wrong_interface));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Input URL");
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.create_dialog_url, null);

        etUrl = rootView.findViewById(R.id.etUrl);
        builder.setView(rootView);
        builder.setPositiveButton(getString(R.string.btn_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getIngredients(etUrl.getText().toString());
            }
        });
        return builder.create();
    }

    private void getIngredients(String url){
            //TODO Implement this thing
            createItem();
    }

    private void createItem() {
        Item item = new Item(
                "TestItem",
                "An ITem",
                1.00d,
                0,
                false);

        itemHandler.onNewItemCreated(item);
    }
}
