package mcgee.recipee.webscraping.data;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;

import mcgee.recipee.webscraping.AddIndividualDialogue;
import mcgee.recipee.webscraping.Ingredient;
import mcgee.recipee.webscraping.R;

public class GetURLDialogue extends Dialog implements
        android.view.View.OnClickListener{
    public Activity c;
    public Dialog d;
    public Button enter, cancel, paste;
    public EditText webAddress;
    GetURLDialogue.OnAddURLResult mOnAddURLResult;

    ClipboardManager clipboard;
    String pasteData = "";

    public GetURLDialogue(Activity a) {
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
        paste = findViewById(R.id.cancel);
        webAddress = findViewById(R.id.webAddress);

        enter.setOnClickListener(this);
        cancel.setOnClickListener(this);

        clipboard = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter:
                if(webAddress.toString().startsWith("http")){
                    String retStr = webAddress.toString();
                }
                dismiss();
                break;

            case R.id.cancel:
                dismiss();
                break;

            //case R.id.pasteURL:
                //do stuff
                //break;
            default:
                break;
        }
    }

    public void setDialogueResult(GetURLDialogue.OnAddURLResult result){
        mOnAddURLResult = result;
    }

    public interface OnAddURLResult{
        void finish(Ingredient retStr);
    }

}
