package lcs.neonproject.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.text.DecimalFormat;

/**
 * Created by Leandro on 9/25/2016.
 */

public class EditTextWatcher implements TextWatcher {

    private boolean increased = false;
    private EditText editText;
    public EditTextWatcher(EditText editText) {
        this.editText = editText;
        editText.setCursorVisible(false);
        editText.setSelection(editText.length());
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d("TextWatcher","beforeTextChange");

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        Log.d("TextWatcher","onTextChange");
        if (before < count)
            increased = true;
        else {
            increased = false;
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        Log.d("TextWatcher","afterTextChange");
        editText.removeTextChangedListener(this);
        String clean = editable.toString().replace("R$ ","").replace(",","");
        if (clean.indexOf('.') != -1)
            clean = clean.replace(".","");
        else {
            if (clean.length() < 3) {
                clean = "0" + clean;
            } else {
                if (increased && clean.charAt(0) == '0') {
                    clean = clean.substring(1);

                }
            }
        }
        editable.clear();
        if (clean.length() > 7)
            clean = clean.substring(0, 7);
        String result = "R$ "+ clean.substring(0,clean.length()-2) +","+clean.substring(clean.length()-2);

        editText.setText(result);
        editText.setSelection(result.length());
        editText.addTextChangedListener(this);
    }
}
