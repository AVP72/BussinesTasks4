package com.babyartsoft.bussinestasks1.Help;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.babyartsoft.bussinestasks1.Interface.Constant;
import com.babyartsoft.bussinestasks1.R;

public class SlideActivity extends AppCompatActivity implements Constant {

    ViewPager viewPager;
    ViewPagerBaseAdapter viewPagerAdapter;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        Intent intent = getIntent();
        int k = intent.getIntExtra(HELP_VIEW_PAGE_ADAPTER, ZERO);

        viewPager = findViewById(R.id.viewPager);
        Switch switchOnStart = findViewById(R.id.slide_switch);
        switchOnStart.setVisibility(View.INVISIBLE);

        TextView txtSkip = findViewById(R.id.txt_skip);
        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        switch (k){
            case view_about: {
                Integer[] img =
                        {
                                R.mipmap.slide0,
                                R.mipmap.slide1,
                                R.mipmap.slide2,
                                R.mipmap.slide3,
                                R.mipmap.slide4,
                                R.mipmap.slide5,
                                R.mipmap.slide6,
                                R.mipmap.slide7
                        };
                viewPagerAdapter = new ViewPagerBaseAdapter(this, img);

                txtSkip.setText(R.string.skip);
                switchOnStart.setVisibility(View.VISIBLE);
                switchOnStart.setChecked(getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE).getBoolean(HELP_RUN_ON_START, true));
                switchOnStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SharedPreferences sp = getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean(HELP_RUN_ON_START, isChecked);
                        editor.apply();
                    }
                });
                break;
            }

            case view_add_slave_url: {
                Integer[] img =
                        {
                                R.mipmap.add_slave_url_1,
                                R.mipmap.add_slave_url_2,
                                R.mipmap.add_slave_url_3,
                                R.mipmap.add_slave_url_3_1,
                                R.mipmap.add_slave_url_4,
                                R.mipmap.add_slave_url_5,
                                R.mipmap.add_slave_url_6,
                                R.mipmap.add_slave_url_7,
                                R.mipmap.add_slave_url_8,
                                R.mipmap.add_slave_url_9,
                                R.mipmap.add_slave_url_10,
                                R.mipmap.add_slave_url_11,
                                R.mipmap.add_slave_url_12

                        };
                viewPagerAdapter = new ViewPagerBaseAdapter(this, img);
                break;
            }
            case  view_help_new_task:{
                Integer[] img =
                        {
                                R.mipmap.help_new_task_1,
                                R.mipmap.help_new_task_2,
                                R.mipmap.help_new_task_2_1,
                                R.mipmap.help_new_task_3,
                                R.mipmap.help_new_task_4,
                                R.mipmap.help_new_task_5
                        };
                viewPagerAdapter = new ViewPagerBaseAdapter(this, img);
                break;
            }
        }

        viewPager.setAdapter(viewPagerAdapter);

        TextView txtNext = findViewById(R.id.txt_next);
        TextView txtPrev = findViewById(R.id.txt_preview);

        txtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < viewPagerAdapter.getCount()-1) {
                    position++;
                    viewPager.setCurrentItem(position);
                }
            }
        });
        txtPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > ZERO) {
                    position--;
                    viewPager.setCurrentItem(position);
                }
            }
        });
    }
}
