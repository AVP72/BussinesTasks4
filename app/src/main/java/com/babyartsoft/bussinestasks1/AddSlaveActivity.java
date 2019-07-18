package com.babyartsoft.bussinestasks1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.babyartsoft.bussinestasks1.Dialog.DialogSysytemOneBtn;
import com.babyartsoft.bussinestasks1.Interface.Constant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.babyartsoft.bussinestasks1.Public.adapterSlave;
import static com.babyartsoft.bussinestasks1.Public.dbRefSlaves;
import static com.babyartsoft.bussinestasks1.Public.toLog;

public class AddSlaveActivity extends AppCompatActivity
        implements View.OnClickListener, Constant {

    TextView txt_error;
    EditText edit_key_slave;
    Button btn_ok, btn_cancel;
    RecyclerView listSlaves;
    AddSlaveActivity addSlaveActivity;
    FirebaseUser user;
    String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_slave);

        addSlaveActivity = this;

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            idUser = user.getUid();
            Public.dbRefTasks = FirebaseDatabase.getInstance().getReference(TASKS).child(idUser);
            Public.dbRefSlaves = FirebaseDatabase.getInstance().getReference(SLAVES).child(idUser);
        }
            else return;

        txt_error = findViewById(R.id.add_slave_txt_error);
        txt_error.setVisibility(View.INVISIBLE);
        edit_key_slave = findViewById(R.id.add_slave_edit);
        listSlaves = findViewById(R.id.add_slave_listSlaves);

        btn_ok = findViewById(R.id.add_slave_btn_ok);
        btn_cancel = findViewById(R.id.add_slave_btn_cancel);

        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        edit_key_slave.setOnClickListener(this);

        edit_key_slave.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                txt_error.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        DatabaseReference dbRefSlaves = FirebaseDatabase.getInstance().getReference(SLAVES).child(idUser);
        Query query = dbRefSlaves.orderByChild(Slaves.queryByName());
        adapterSlave = new FirebaseRecyclerAdapter<Slaves, SlaveViewHolder>
                (Slaves.class, R.layout.item_slave, SlaveViewHolder.class, query) {

            @Override
            protected void populateViewHolder(SlaveViewHolder viewHolder, Slaves model, int position) {
                viewHolder.displayNameTxt.setText(model.getDisplayNameSlaveEdit());
                viewHolder.profTxt.setText(model.getProf());
                viewHolder.phoneTxt.setText(model.getPhoneNumber());

                viewHolder.send(addSlaveActivity);
            }
        };

        listSlaves.setLayoutManager(new LinearLayoutManager(this));
        listSlaves.setAdapter(adapterSlave);

        getUrlRegUser();    //проверяет, открыт ли с помощью ссылки и если да, то обрабатывает ее

    }

    void getUrlRegUser(){
        // обрабатывает ссылку
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null){
            String url = data.toString();
            char searchChar = '/';
            int k = 0;
            final int n = 3;
            for (int i=0; i<url.length(); i++){
                if (url.charAt(i) == searchChar){
                    ++k;
                    if (k == n) {
                        k = i+1;
                        break;
                    }
                }
            }
            String keySlave = url.substring(k);
//            toLog("Вырезаный код: " + keySlave);

            DatabaseReference dbRefSlave = FirebaseDatabase.getInstance().getReference(USERS).child(keySlave);
            dbRefSlave.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User userSlave = dataSnapshot.getValue(User.class);
                    if (userSlave == null) {
                        toLog("Сбой user == null");
                        return;
                    }

                    String dislayName = ZERO_S;
                    if (userSlave.getDisplayName() != null)
                        dislayName = userSlave.getDisplayName();
                    String phone = ZERO_S;
                    if (userSlave.getPhoneNamber() != null)
                        phone = userSlave.getPhoneNamber();
                    String uid = ZERO_S;
                    if (userSlave.getUid() != null)
                        uid = userSlave.getUid();
                    if (uid.equals(ZERO_S)) {
                        toLog("Сбой uid - Zero");
                        return;
                    }
                    if (uid.equals(idUser)) {
                        toLog("Ссылку на себя нельзя");
                        new DialogSysytemOneBtn(addSlaveActivity, "Сотруднник не добавлен", "Нельзя использовать ссылку на себя");
                    } else {

                        final Slaves slaves = new Slaves(dislayName, phone);
                        slaves.setDisplayNameSlaveEdit(dislayName);

                        final DatabaseReference dbRefSlave = FirebaseDatabase.getInstance()
                                .getReference(SLAVES).child(idUser);
                        final String finalUid = uid;
                        dbRefSlave.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Slaves sl = ds.getValue(Slaves.class);
                                    if (sl != null) {
                                        String key = ds.getKey();

                                        if (key != null) if (key.equals(finalUid)) {
                                            toLog("Такой уже есть");
                                            return;
                                        }
                                    } else return;
                                }
                                dbRefSlave.child(finalUid).setValue(slaves);
                                Toast.makeText(getApplicationContext(), R.string.check_gmail_yes, Toast.LENGTH_LONG).show();
                                new DialogSysytemOneBtn(addSlaveActivity, getString(R.string.add_slave), slaves.getDisplayNameSlave());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    toLog("Сбой");
                }
            });
        }

        toLog("on start: " + data);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_slave_edit:
                txt_error.setVisibility(View.INVISIBLE);
                break;
            case R.id.add_slave_btn_cancel:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.add_slave_btn_ok:

                final String keySlave = edit_key_slave.getText().toString().trim();

                if (keySlave.equals(ZERO_S)){
                    Toast.makeText(getApplicationContext(), R.string.check_zero, Toast.LENGTH_LONG).show();
                    txt_error.setText(R.string.check_zero);
                    txt_error.setVisibility(View.VISIBLE);
                    break;
                }
                SharedPreferences sp = getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
                String self_key = sp.getString(KEY_USER_SELF, ZERO_S);
                if (keySlave.equals(self_key)
                        || keySlave.compareToIgnoreCase(
                        (user.getEmail() == null)?ZERO_S:user.getEmail()) == ZERO){
                    Toast.makeText(getApplicationContext(), R.string.check_self, Toast.LENGTH_LONG).show();
                    txt_error.setText(R.string.check_self);
                    txt_error.setVisibility(View.VISIBLE);
                    break;
                }

                if (keySlave.contains(POINT_STRING)){       // если строка содержит точку (gmail содержит, а ключ не может содержать точку)
                    DatabaseReference dbRefSlave = FirebaseDatabase.getInstance().getReference(USERS);
                    dbRefSlave.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean isGmail = false;
                            for (DataSnapshot ds: dataSnapshot.getChildren()){
                                String mail = ds.child(GMAIL).getValue(String.class);
                                if (mail == null) mail = ZERO_S;

                                if (keySlave.compareToIgnoreCase(mail) == ZERO){

                                    User userSlave = ds.getValue(User.class);

                                    if (userSlave == null) {
                                        Toast.makeText(getApplicationContext(), R.string.check_gmail, Toast.LENGTH_LONG).show();
                                        txt_error.setText(R.string.check_gmail);
                                        txt_error.setVisibility(View.VISIBLE);
                                        return;
                                    }

                                    String dislayName = ZERO_S;
                                    if (userSlave.getDisplayName() != null)
                                        dislayName = userSlave.getDisplayName();
                                    String phone = ZERO_S;
                                    if (userSlave.getPhoneNamber() != null)
                                        phone = userSlave.getPhoneNamber();

                                    Slaves slaves = new Slaves(dislayName, phone);
                                    slaves.setDisplayNameSlaveEdit(dislayName);
                                    dbRefSlaves.child(userSlave.getUid()).setValue(slaves);

/*
                                    Slaves boss = new Slaves(user.getDisplayName(), user.getPhoneNumber());
                                    boss.setDisplayNameSlaveEdit(boss.getDisplayNameSlave());
                                    dbRefBossAll.child(userSlave.getUid()).child(user.getUid()).setValue(boss);
*/

                                    isGmail = true;
                                }
                            }
                            if  (!isGmail){
                                Toast.makeText(getApplicationContext(), R.string.check_gmail, Toast.LENGTH_LONG).show();
                                txt_error.setText(R.string.check_gmail);
                                txt_error.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.check_gmail_yes, Toast.LENGTH_LONG).show();
                                edit_key_slave.setText(ZERO_S);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), R.string.check_gmail_error, Toast.LENGTH_LONG).show();
                            txt_error.setText(R.string.check_gmail_error);
                            txt_error.setVisibility(View.VISIBLE);
                        }

                    });
                } else {
                    DatabaseReference dbRefSlave = FirebaseDatabase.getInstance().getReference(USERS).child(keySlave);
                    dbRefSlave.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            User userSlave = dataSnapshot.getValue(User.class);

                            if (userSlave == null) {
                                Toast.makeText(getApplicationContext(), R.string.check_key, Toast.LENGTH_LONG).show();
                                txt_error.setText(R.string.check_key);
                                txt_error.setVisibility(View.VISIBLE);
                                return;
                            }

                            String dislayName = ZERO_S;
                            if (userSlave.getDisplayName() != null)
                                dislayName = userSlave.getDisplayName();
                            String phone = ZERO_S;
                            if (userSlave.getPhoneNamber() != null)
                                phone = userSlave.getPhoneNamber();

                            Slaves slaves = new Slaves(dislayName, phone);
                            slaves.setDisplayNameSlaveEdit(dislayName);
                            dbRefSlaves.child(userSlave.getUid()).setValue(slaves);

/*
                            Slaves boss = new Slaves(user.getDisplayName(), user.getPhoneNumber());
                            boss.setDisplayNameSlaveEdit(boss.getDisplayNameSlave());
                            dbRefBossAll.child(userSlave.getUid()).child(user.getUid()).setValue(boss);
*/

                            Toast.makeText(getApplicationContext(), R.string.check_gmail_yes, Toast.LENGTH_LONG).show();
                            edit_key_slave.setText(ZERO_S);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), R.string.check_gmail_error, Toast.LENGTH_LONG).show();
                            txt_error.setText(R.string.check_gmail_error);
                            txt_error.setVisibility(View.VISIBLE);
                        }
                    });
                }

                break;
        }

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

}
