package com.babyartsoft.bussinestasks1.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.babyartsoft.bussinestasks1.Dialog.DialogOneButton;
import com.babyartsoft.bussinestasks1.R;

public class DialError extends DialogOneButton {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        massageTxt.setText(R.string.dialog_is_error);
    }
}