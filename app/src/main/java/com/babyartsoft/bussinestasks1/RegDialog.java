package com.babyartsoft.bussinestasks1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.babyartsoft.bussinestasks1.Interface.Constant;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.util.Locale;

public class RegDialog extends Dialog implements View.OnClickListener, Constant {

    private Activity activity;
    private GoogleSignInClient mGoogleSignInClient;
    private Context context;
    private LogAnalytic logAnalytic;

    RegDialog(Activity activity, GoogleSignInClient mGoogleSignInClient) {
        super(activity);
        this.activity = activity;
        context = activity.getApplicationContext();
        this.mGoogleSignInClient = mGoogleSignInClient;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_dialog_on_start);
        Button reg = findViewById(R.id.button_start_reg);
        Button demoWork = findViewById(R.id.button_start_autonom);
        reg.setOnClickListener(this);
        demoWork.setOnClickListener(this);
        setCanceledOnTouchOutside(false);        // заблокироват нажимания мимо

        logAnalytic = new LogAnalytic(context, context.getString(R.string.notReg));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_start_reg:
                logAnalytic.setLogReg();
                regUserFromGoogle();            // Запуска регистрации
                dismiss();                      // закрыть диалог
                break;
            case R.id.button_start_autonom:
                String country = Locale.getDefault().getCountry();
                String videoId;
                if (country.equals("RU")) {
                    videoId = "9aCDYnONpBA";
                } else {
                    videoId = "dzl-276_nwo";
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                activity.startActivity(intent);
                break;
            default:
                break;
        }

    }

    private void regUserFromGoogle() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logAnalytic.setLogNotReg();
        activity.finishAffinity ();
    }
}
