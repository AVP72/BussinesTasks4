package com.babyartsoft.bussinestasks1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.babyartsoft.bussinestasks1.Interface.Constant;

import static com.babyartsoft.bussinestasks1.Public.dbRefBoss;

public class EditBossItemActivity extends AppCompatActivity implements Constant {

    TextView txtName;
    EditText editNameEdit, editProf, editPhone;
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_boss_item);

        Intent intent = getIntent();
        final String name = intent.getStringExtra(SLAVE_DISPLAY_NAME);
        String nameEdit = intent.getStringExtra(SLAVE_DISPLAY_NAME_EDIT);
        String prof = intent.getStringExtra(SLAVE_PROF);
        String phone = intent.getStringExtra(SLAVE_PHONE);
        final String key = intent.getStringExtra(SLAVE_KEY);

        txtName = findViewById(R.id.edit_boss_item_displayName);
        editNameEdit = findViewById(R.id.edit_boss_item_displayNameEdit);
        editProf = findViewById(R.id.edit_boss_item_prof);
        editPhone = findViewById(R.id.edit_boss_item_phone);

        txtName.setText(name);
        editNameEdit.setText(nameEdit);
        editProf.setText(prof);
        editPhone.setText(phone);

        btnOk = findViewById(R.id.edit_boss_item_btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Slaves boss = new Slaves(
                        name,
                        editNameEdit.getText().toString(),
                        editPhone.getText().toString(),
                        editProf.getText().toString()
                );
                dbRefBoss.child(key).setValue(boss);
                finish();
            }
        });
    }
}
