package com.babyartsoft.bussinestasks1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.babyartsoft.bussinestasks1.Interface.Constant;
import com.babyartsoft.bussinestasks1.Message.NotifyWork;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.babyartsoft.bussinestasks1.Public.dbRefBoss;
import static com.babyartsoft.bussinestasks1.Public.dbRefSlaves;
import static com.babyartsoft.bussinestasks1.Public.dbRefTasks;
import static com.babyartsoft.bussinestasks1.Public.listTask;
import static com.babyartsoft.bussinestasks1.Public.mAuth;
import static com.babyartsoft.bussinestasks1.Public.user;

public class EditStatusActivity extends AppCompatActivity implements View.OnClickListener, Constant {

    TextView txtTask, txtDate, txtTime, txtAuthor;
    ImageView imgGo, imgGalka, imgPusto, imgPause, imgCancel, imgExit;
    EditText editNote;
    int newStatus;
    String keyPosition, oldNote;
    ItemBussinessTask ibt;
    String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_status);

        txtTask = findViewById(R.id.edit_status_txt_task);
        txtDate = findViewById(R.id.edit_status_txt_date);
        txtTime = findViewById(R.id.edit_status_txt_time);
        txtAuthor = findViewById(R.id.edit_status_txt_author);
        editNote = findViewById(R.id.edit_status_edit_note);

        imgGo       =   findViewById(R.id.edit_status_img_go);
        imgGalka    =   findViewById(R.id.edit_status_img_galka);
        imgPusto    =   findViewById(R.id.edit_status_img_pusto);
        imgPause    =   findViewById(R.id.edit_status_img_pause);
        imgCancel   =   findViewById(R.id.edit_status_img_cancel);
        imgExit     =   findViewById(R.id.edit_status_img_exit);

        imgGo.setOnClickListener(this);
        imgGalka.setOnClickListener(this);
        imgPusto.setOnClickListener(this);
        imgPause.setOnClickListener(this);
        imgCancel.setOnClickListener(this);
        imgExit.setOnClickListener(this);

        Intent intent = getIntent();
        boolean fromNotify = intent.getBooleanExtra(FROM_NOTIFY, false);
        if (!fromNotify) {
            int k = intent.getIntExtra(ADAPTER_POSITION, ERROR);
            if (k == ERROR) {
                Toast.makeText(this, R.string.error_read_entry, Toast.LENGTH_LONG).show();
                finish();
            }
            idUser = user.getUid();
            ibt = listTask.get(k);
            keyPosition = ibt.getKey();
            sendAnswer();
            installPoles(ibt);
            new NotifyWork(getApplicationContext(), keyPosition).delNotify();
        } else {
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null) return;
            idUser  = user.getUid();
            dbRefTasks = FirebaseDatabase.getInstance().getReference(TASKS).child(idUser);
            dbRefTasks.keepSynced(true);                  // настройка для записи без органичений
            dbRefSlaves = FirebaseDatabase.getInstance().getReference(SLAVES).child(idUser);
            dbRefSlaves.keepSynced(true);
            dbRefBoss = FirebaseDatabase.getInstance().getReference(BOSS).child(idUser);
            dbRefBoss.keepSynced(true);

            keyPosition = intent.getStringExtra(FROM_NOTIFY_ID_TASK);
            dbRefTasks.child(keyPosition).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    BussinessTask bt = dataSnapshot.getValue(BussinessTask.class);
                    if (bt == null) return;
                    ibt = new ItemBussinessTask(keyPosition, bt);
                    sendAnswer();
                    installPoles(ibt);
                    new NotifyWork(getApplicationContext(), keyPosition).delNotify();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    finish();
                }
            });
        }
    }

    void sendAnswer(){
        // отчет автору о прочтении и запись времени прочтения
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;
        String idUser  = user.getUid();
        DatabaseReference dbRefTasks = FirebaseDatabase.getInstance().getReference(TASKS).child(idUser);
        dbRefTasks.keepSynced(true);

        long dateread = ibt.getDateRead();
        if (dateread == ZERO_L) {
            long currentTime = System.currentTimeMillis();
            ibt.setDateRead(currentTime);
            ibt.setDone(Status.id_done_no);
            dbRefTasks.child(keyPosition).setValue(ibt.getBussinessTask());
        }
    }

    private void installPoles(final ItemBussinessTask ibt) {

        txtTask.setText(ibt.getTask());
        txtDate.setText(Public.setTxtDate(ibt.getDateSrok()));
        txtTime.setText(Public.setTxtTime(ibt.getDateSrok()));
        oldNote = ibt.getNoteSlave()==null?"":ibt.getNoteSlave();
        editNote.setText(oldNote);

        dbRefBoss.child(ibt.getIdAuthor())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Slaves boss = dataSnapshot.getValue(Slaves.class);
                        String s;
                        if (boss == null) return;
                        s = boss.getDisplayNameSlaveEdit();
                        txtAuthor.setText(s);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        int currentStatus = ibt.getStatus();
        switch (currentStatus){
            case Status.id_go:
                imgGo.setBackgroundColor(getResources().getColor(R.color.gray25));
                break;
            case Status.id_galka:
                imgGalka.setBackgroundColor(getResources().getColor(R.color.gray25));
                break;
            case Status.id_pusto:
                imgPusto.setBackgroundColor(getResources().getColor(R.color.gray25));
                break;
            case Status.id_pause:
                imgPause.setBackgroundColor(getResources().getColor(R.color.gray25));
                break;
            case Status.id_cancel:
                imgCancel.setBackgroundColor(getResources().getColor(R.color.gray25));
                break;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.edit_status_img_go:
                newStatus = Status.id_go;
                break;
            case R.id.edit_status_img_galka:
                newStatus = Status.id_galka;
                break;
            case R.id.edit_status_img_pusto:
                newStatus = Status.id_pusto;
                break;
            case R.id.edit_status_img_pause:
                newStatus = Status.id_pause;
                break;
            case R.id.edit_status_img_cancel:
                newStatus = Status.id_cancel;
                break;
            case R.id.edit_status_img_exit:
                saveNote();
                finish();
                return;
        }


        ibt.setStatus(newStatus);
        ibt.setIdUserEditStatus(idUser);
        long currentTime = System.currentTimeMillis();
        ibt.setDateLastEdit(currentTime);
        ibt.setDateEditStatus(currentTime);
        ibt.setDone(Status.id_done_no);
//        toLog(editNote.getText().toString());
        ibt.setNoteSlave(editNote.getText().toString());
        final BussinessTask bt = ibt.getBussinessTask();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;
        String idUser  = user.getUid();
        final DatabaseReference dbRefTasks = FirebaseDatabase.getInstance().getReference(TASKS).child(idUser);
        dbRefTasks.keepSynced(true);

        dbRefTasks.child(keyPosition).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    dbRefTasks.child(keyPosition).setValue(bt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        finish();
    }

    @Override
    public void onBackPressed() {
        saveNote();
        super.onBackPressed();
    }

    void saveNote(){
        if (!oldNote.equals(editNote.getText().toString())){
            long currentTime = System.currentTimeMillis();
            ibt.setDateLastEdit(currentTime);
            ibt.setNoteSlave(editNote.getText().toString());
            ibt.setDone(Status.id_done_no);
            BussinessTask bt = ibt.getBussinessTask();
            dbRefTasks.child(keyPosition).setValue(bt);
        }
    }

}
