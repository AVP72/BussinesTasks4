package com.babyartsoft.bussinestasks1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.babyartsoft.bussinestasks1.Interface.Constant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import static com.babyartsoft.bussinestasks1.Public.adapterBoss;
import static com.babyartsoft.bussinestasks1.Public.dbRefBoss;

public class EditBossActivity extends AppCompatActivity implements View.OnClickListener, Constant {

    RecyclerView listBoss;
    Button button_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_boss);

        button_ok = findViewById(R.id.edit_boss_button_ok);
        button_ok.setOnClickListener(this);

        listBoss = findViewById(R.id.edit_boss_list_boss);

        Query query = dbRefBoss.orderByChild(Slaves.queryByName());
        adapterBoss = new FirebaseRecyclerAdapter<Slaves, BossViewHolder>
                (Slaves.class, R.layout.item_boss, BossViewHolder.class, query) {

            @Override
            protected void populateViewHolder(BossViewHolder viewHolder, Slaves model, int position) {

                viewHolder.displayNameTxt.setText(model.getDisplayNameSlaveEdit());
                viewHolder.profTxt.setText(model.getProf());
                viewHolder.phoneTxt.setText(model.getPhoneNumber());
            }
        };

        listBoss.setLayoutManager(new LinearLayoutManager(this));
        listBoss.setAdapter(adapterBoss);

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
