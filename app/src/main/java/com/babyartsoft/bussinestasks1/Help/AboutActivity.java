package com.babyartsoft.bussinestasks1.Help;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.babyartsoft.bussinestasks1.BuildConfig;
import com.babyartsoft.bussinestasks1.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView verTxt = findViewById(R.id.about_ver);
        String numVer = BuildConfig.VERSION_NAME;
        String ver = getString(R.string.about_ver) + ": " + numVer;
        verTxt.setText(ver);

        TextView goPlayTxt = findViewById(R.id.about_go_to_play);
        goPlayTxt.setOnClickListener(this);

        TextView politicTxt = findViewById(R.id.about_politic);
        politicTxt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.about_go_to_play:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.babyartsoft.bussinestasks1"));
                break;
            case R.id.about_politic:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/businesstasks/main"));
                break;
        }
        if (intent != null) startActivity(intent);
    }

}
