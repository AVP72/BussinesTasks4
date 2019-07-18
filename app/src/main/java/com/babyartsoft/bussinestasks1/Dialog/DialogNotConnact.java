package com.babyartsoft.bussinestasks1.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.babyartsoft.bussinestasks1.R;

public class DialogNotConnact extends DialogOneButton {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String massage =
                getString(R.string.dialog_not_contact1) + "\n"+ "\n"+
                        getString(R.string.dialog_not_contact2)+ "\n"+"\n"+
                        getString(R.string.dialog_not_contact3);

        massageTxt.setText(massage);

    }
}
