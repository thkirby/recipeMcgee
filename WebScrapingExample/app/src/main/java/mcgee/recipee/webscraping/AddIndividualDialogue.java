package mcgee.recipee.webscraping;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class AddIndividualDialogue extends Dialog implements
        android.view.View.OnClickListener {

public Activity c;
public Dialog d;
public Button enter, cancel;
public TextInputEditText ingrName;
public EditText ingrAmount;
public Spinner ingrType;
OnAddItemResult mOnAddItemResult;

public AddIndividualDialogue(Activity a) {
        super(a);
        this.c = a;
        }

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.enter_item);
        enter = findViewById(R.id.enter);
        cancel = findViewById(R.id.cancel);
        ingrName = findViewById(R.id.inputTextName);
        ingrAmount = findViewById(R.id.ingrAmount);
        ingrType = findViewById(R.id.ingrType);

        enter.setOnClickListener(this);
        cancel.setOnClickListener(this);

        }

@Override
public void onClick(View v) {
    switch (v.getId()) {
        case R.id.enter:
            if(!ingrName.getText().toString().equals("") &&
               !ingrAmount.getText().toString().equals("")){
                Ingredient retIngr = new Ingredient(ingrName.getText().toString().trim().toLowerCase().replace("[\n\r]", ""),
                                                    Float.parseFloat(ingrAmount.getText().toString()),
                                                    ingrType.getSelectedItem().toString());
                mOnAddItemResult.finish(retIngr);

            }
            dismiss();
        break;

        case R.id.cancel:
            dismiss();
        break;
        default:
        break;
    }
    dismiss();
}

public void setDialogueResult(OnAddItemResult result){
    mOnAddItemResult = result;
}

public interface OnAddItemResult{
        void finish(Ingredient retIngr);
    }
}


