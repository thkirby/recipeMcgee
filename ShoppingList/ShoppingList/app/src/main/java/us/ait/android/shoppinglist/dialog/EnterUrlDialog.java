/** This class is for a dialog which asks for a url and then detects if there is a list of ingredients
 *  that can be added to the list.
 *
 */

package us.ait.android.shoppinglist.dialog;

import android.widget.EditText;
import android.widget.Spinner;

import us.ait.android.shoppinglist.data.Item;

public class EnterUrlDialog {
    public interface ItemHandler {
        void onNewItemCreated(Item item);

        void onItemUpdated(Item item);
    }

    private CreateAndEditItemDialog.ItemHandler itemHandler;
    private Spinner spinnerItemType;
    private EditText etUrl;
    private Item itemToEdit = null;
}
