package com.babyartsoft.bussinestasks1.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.babyartsoft.bussinestasks1.R;

public class DialogOneButton extends AppCompatActivity {

    TextView titleTxt, massageTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.dialog_one_button);

        Button button = findViewById(R.id.dialog_one_button_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleTxt = findViewById(R.id.dialog_one_button_title);
        massageTxt = findViewById(R.id.dialog_one_button_massage);

        titleTxt.setText(getString(R.string.podpiska));
    }

}
