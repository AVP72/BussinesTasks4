package com.babyartsoft.bussinestasks1.Dialog;

import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.babyartsoft.bussinestasks1.R;

public class DialogSysytemOneBtn {

    public DialogSysytemOneBtn(Context context, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setIcon(R.mipmap.ic_massage_large)
                .setCancelable(false)
                .setNegativeButton(context.getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
