package com.babyartsoft.bussinestasks1.Help;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.babyartsoft.bussinestasks1.Interface.Constant;
import com.babyartsoft.bussinestasks1.R;

public class MainHelpActivity extends AppCompatActivity implements View.OnClickListener, Constant {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_help);

        Button btnAbout = findViewById(R.id.btn_main_help_about);
        btnAbout.setOnClickListener(this);
        Button btnAddSlaveUrl = findViewById(R.id.btn_main_help_add_slave_url);
        btnAddSlaveUrl.setOnClickListener(this);
        Button btnNewTask = findViewById(R.id.btn_main_help_new_task);
        btnNewTask.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SlideActivity.class);
        switch (v.getId()){
            case R.id.btn_main_help_about:
                intent.putExtra(HELP_VIEW_PAGE_ADAPTER, view_about);
                break;
            case R.id.btn_main_help_add_slave_url:
                intent.putExtra(HELP_VIEW_PAGE_ADAPTER, view_add_slave_url);
                break;
            case R.id.btn_main_help_new_task:
                intent.putExtra(HELP_VIEW_PAGE_ADAPTER, view_help_new_task);
                break;

        }
        startActivity(intent);
    }
}
