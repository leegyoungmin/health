package com.Health.health.Member;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class weightwatcher implements TextWatcher {
    private EditText mEdittext;
    String text;
    public weightwatcher(EditText editText){
        mEdittext=editText;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        text=mEdittext.toString();
    }

    @Override
    public void afterTextChanged(Editable s) {
        mEdittext.setText(s.toString());
    }
}
