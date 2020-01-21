package us.ait.android.shoppinglist.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import us.ait.android.shoppinglist.MainActivity;
import us.ait.android.shoppinglist.R;
import us.ait.android.shoppinglist.data.Item;

public class CreateAndEditItemDialog extends DialogFragment {

    public interface ItemHandler {
        void onNewItemCreated(Item item);

        void onItemUpdated(Item item);
    }

    private ItemHandler itemHandler;
    private Spinner spinnerItemType;
    private EditText etPrice;
    private EditText etItem;
    private EditText etItemDesc;
    private Item itemToEdit = null;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ItemHandler) {
            itemHandler = (ItemHandler) context;
        } else {
            throw new RuntimeException(getString(R.string.error_wrong_interface));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.new_item_created);
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.create_dialog_item, null);

        initSpinner(rootView);

        etItem = rootView.findViewById(R.id.etItem);
        etItemDesc = rootView.findViewById(R.id.etItemDesc);
        etPrice = rootView.findViewById(R.id.etPrice);

        if (getArguments() != null &&
                getArguments().containsKey(MainActivity.KEY_EDIT)) {
            editItemInShoppingRow(builder);
        }
        builder.setView(rootView);
        builder.setPositiveButton(getString(R.string.btn_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    private void editItemInShoppingRow(AlertDialog.Builder builder) {
        builder.setTitle(R.string.update_item_title);
        Item itemToEdit = (Item) getArguments().getSerializable(MainActivity.KEY_EDIT);
        etItem.setText(itemToEdit.getItemName());
        etItemDesc.setText(itemToEdit.getDescription());
        etPrice.setText(Double.toString(itemToEdit.getEstimatedPrice()));
        spinnerItemType.setSelection(itemToEdit.getItemTypeAsEnum().getValue());
    }

    private void initSpinner(View rootView) {
        spinnerItemType = rootView.findViewById(R.id.spinnerItemType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.itemtypes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItemType.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(etItem.getText())) {
                        if (getArguments() != null && getArguments().containsKey(MainActivity.KEY_EDIT)) {
                            editItem();
                        } else {
                            createItem();
                        }
                        d.dismiss();
                    } else {
                        etItem.setError(getString(R.string.error_empty_field));
                    }
                }
            });
        }
    }

    private void createItem() {
        Item item = new Item(
                etItem.getText().toString(),
                etItemDesc.getText().toString(),
                Double.parseDouble(etPrice.getText().toString()),
                spinnerItemType.getSelectedItemPosition(),
                false);

        itemHandler.onNewItemCreated(item);
    }

    private void editItem() {
        Item itemToEdit = (Item) getArguments().getSerializable(MainActivity.KEY_EDIT);
        itemToEdit.setItemName(etItem.getText().toString());
        itemToEdit.setDescription(etItemDesc.getText().toString());
        itemToEdit.setItemType(spinnerItemType.getSelectedItemPosition());
        itemToEdit.setEstimatedPrice(Double.parseDouble(etPrice.getText().toString()));
        itemHandler.onItemUpdated(itemToEdit);
    }
}
