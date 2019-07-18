package com.babyartsoft.bussinestasks1.Dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.babyartsoft.bussinestasks1.AddSlaveActivity;
import com.babyartsoft.bussinestasks1.Interface.Constant;
import com.babyartsoft.bussinestasks1.LogAnalytic;
import com.babyartsoft.bussinestasks1.R;
import com.babyartsoft.bussinestasks1.User;

import static com.babyartsoft.bussinestasks1.Public.dbRefUser;
import static com.babyartsoft.bussinestasks1.Public.user;
import static com.babyartsoft.bussinestasks1.RegAsBoss.statTreal;

public class DialogStartTreal extends AppCompatActivity implements View.OnClickListener, Constant {

    LogAnalytic logAnalytic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.activity_dialog_start_treal);

        Button btnActive = findViewById(R.id.dialog_start_treal_btn_active);
        Button btnCancel = findViewById(R.id.dialog_start_treal_btn_cancel);

        btnActive.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        logAnalytic = new LogAnalytic(this, user.getUid());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_start_treal_btn_active:
                setStatTreal();
                startAddActivity();
                logAnalytic.setLogSetAsBoss();
                break;
            case R.id.dialog_start_treal_btn_cancel:
                logAnalytic.setLogCancelAsBoss();
                break;
        }
        finish();
    }

    private void setStatTreal(){
        if (dbRefUser != null) {
            dbRefUser.child(User.sDateRegAsBossTreal).setValue(System.currentTimeMillis());
            dbRefUser.child(User.sStatAsBoss).setValue(statTreal);
        }
    }

    private void startAddActivity(){
        Intent intent = new Intent(this, AddSlaveActivity.class);
        startActivityForResult(intent, RESULT_ON_EDIT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logAnalytic.setLogCancelAsBoss();
    }
}
