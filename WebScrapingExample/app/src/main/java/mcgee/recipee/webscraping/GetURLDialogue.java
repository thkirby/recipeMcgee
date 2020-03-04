package mcgee.recipee.webscraping;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GetURLDialogue extends Dialog implements
        android.view.View.OnClickListener{
    public Activity c;
    public Dialog d;
    public Button enter, cancel, paste;
    public EditText webAddress;
    GetURLDialogue.OnAddURLResult mOnAddURLResult;

    ClipboardManager clipboard;
    String retStr = "", pasteData;

    public GetURLDialogue(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.enter_url);
        enter = findViewById(R.id.enter);
        cancel = findViewById(R.id.cancel);
        paste = findViewById(R.id.paste);
        webAddress = findViewById(R.id.webAddress);

        enter.setOnClickListener(this);
        cancel.setOnClickListener(this);
        paste.setOnClickListener(this);

        clipboard = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter:
                if(webAddress.getText().toString().startsWith("http")){
                    retStr = webAddress.getText().toString();
                    mOnAddURLResult.finish(retStr);
                }
                dismiss();
                break;

            case R.id.cancel:
                dismiss();
                break;

            case R.id.paste:
                Log.d("pasting", "Made it to the case2");
                ClipData clipData = clipboard.getPrimaryClip();
                if (clipData != null) {
                    pasteData = clipData.getItemAt(0).getText().toString();
                    Log.d("pasting", "pastData: " + pasteData);
                    webAddress.setText(pasteData, TextView.BufferType.EDITABLE);
                }
                break;

            default:
                break;
        }
    }

    public void setDialogueResult(GetURLDialogue.OnAddURLResult result){
        mOnAddURLResult = result;
    }

    public interface OnAddURLResult{
        void finish(String retStr);
    }

}
