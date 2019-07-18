package com.babyartsoft.bussinestasks1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.util.ArrayList;
import java.util.Calendar;

import static com.babyartsoft.bussinestasks1.Public.dbRefSlaves;
import static com.babyartsoft.bussinestasks1.Public.dbRefTasks;
import static com.babyartsoft.bussinestasks1.Public.listTask;
import static com.babyartsoft.bussinestasks1.Public.mAuth;
import static com.babyartsoft.bussinestasks1.Public.regAsBoss;
import static com.babyartsoft.bussinestasks1.Public.user;

public class EditTaskActivity extends AppCompatActivity implements View.OnClickListener, Constant {

    EditText    editTask;
    long        dateSrok;
    Calendar cSrok;
    TextView txtSlave, txtDate, txtTime, txtNote;
    int status, oldStatus;
    ImageView imgPusto, imgGo, imgGalka, imgPause, iimgCancel;
    ItemBussinessTask ibt;
    String keyPosition, keySlave, keySlaveOld;
    String idUser;
    boolean fromNotify;
    int statUser;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        context = this;

        Intent intent = getIntent();
        fromNotify = intent.getBooleanExtra(FROM_NOTIFY, false);
        if (!fromNotify){
            int k = intent.getIntExtra(ADAPTER_POSITION, ERROR);
            if (k == ERROR){
                Toast.makeText(this, R.string.error_read_entry, Toast.LENGTH_LONG).show();
                finish();
            }
            idUser = user.getUid();
            ibt = listTask.get(k);
            keyPosition = ibt.getKey();
            new NotifyWork(this, keyPosition).delNotify();
        } else {
            keyPosition = intent.getStringExtra(FROM_NOTIFY_ID_TASK);
            if (keyPosition == null) return;
        }


        Button btnCancel        = findViewById(R.id.edit_task_btn_cancel);
        Button btnSave          = findViewById(R.id.edit_task_btn_save);
        Button btnSlaveX        = findViewById(R.id.edit_task_btn_slave_x);
        Button btnSlavePlus     = findViewById(R.id.edit_task_btn_slave_plus);
        txtSlave                = findViewById(R.id.edit_task_text_slave);
        txtDate                 = findViewById(R.id.edit_task_text_date);
        Button btnDateMinus     = findViewById(R.id.edit_task_btn_date_minus);
        Button btnDatePlus      = findViewById(R.id.edit_task_btn_date_plus);
        Button btnDatePlus2     = findViewById(R.id.edit_task_btn_date_plus2);
        Button btnDateCalindar  = findViewById(R.id.edit_task_btn_calendar);
        Button btnTime          = findViewById(R.id.edit_task_btn_time);
        txtTime                 = findViewById(R.id.edit_task_text_time);
        ImageView imgInfo       = findViewById(R.id.edit_task_btn_info);
        imgPusto                = findViewById(R.id.edit_task_img_pusto);
        imgGo                   = findViewById(R.id.edit_task_img_go);
        imgGalka                = findViewById(R.id.edit_task_img_galka);
        imgPause                = findViewById(R.id.edit_task_img_pause);
        iimgCancel              = findViewById(R.id.edit_task_img_cancel);
        editTask                = findViewById(R.id.edit_task_edit);
        txtNote                 = findViewById(R.id.edit_task_note);

        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnSlaveX.setOnClickListener(this);
        btnSlavePlus.setOnClickListener(this);
        txtSlave.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        btnDateMinus.setOnClickListener(this);
        btnDatePlus.setOnClickListener(this);
        btnDatePlus2.setOnClickListener(this);
        btnDateCalindar.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        imgInfo.setOnClickListener(this);
        imgPusto.setOnClickListener(this);
        imgGo.setOnClickListener(this);
        imgGalka.setOnClickListener(this);
        imgPause.setOnClickListener(this);
        iimgCancel.setOnClickListener(this);

        cSrok = Calendar.getInstance();

        if (!fromNotify){
            installPoles(ibt);
        } else {

            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null) return;
            idUser  = user.getUid();

            dbRefTasks = FirebaseDatabase.getInstance().getReference(TASKS).child(idUser);
            dbRefTasks.keepSynced(true);
            dbRefSlaves = FirebaseDatabase.getInstance().getReference(SLAVES).child(idUser);
            dbRefSlaves.keepSynced(true);

            dbRefTasks.child(keyPosition).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    BussinessTask bt = dataSnapshot.getValue(BussinessTask.class);
                    if (bt == null) return;
                    ibt = new ItemBussinessTask(keyPosition, bt);
                    installPoles(ibt);
                    new NotifyWork(getApplicationContext(), keyPosition).delNotify();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    protected void onResume() { // При запуске и при закрытии диалога
        super.onResume();
        SharedPreferences sp = getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
        statUser = sp.getInt(USER_SELF_STAT_BOSS, RegAsBoss.statNoBoss);
    }

    private void installPoles(ItemBussinessTask ibt) {

        keySlave = keySlaveOld = ibt.getIdSlave();
        if (!keySlave.equals(NOT_SLAVE_KEY) && !keySlave.substring(ZERO, 2).equals(IS_DEL)) {
            dbRefSlaves.child(keySlave).child(Slaves.getPoleName_displayNameSlaveEdit())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.getValue(String.class);
                            if (name == null) txtSlave.setText(getString(R.string.setNobody));
                            else txtSlave.setText(name);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else {
            txtSlave.setText(getString(R.string.setNobody));
        }

        editTask.setText(ibt.getTask());
        txtDate.setText(Public.setTxtDate(ibt.getDateSrok()));
        txtTime.setText(Public.setTxtTime(ibt.getDateSrok()));
        cSrok.setTimeInMillis(ibt.getDateSrok());
        String sNote;
        if (ibt.getNoteSlave()==null){
            sNote = getString(R.string.not_note);
        } else {
            sNote = ibt.getNoteSlave();
            if (sNote.equals(ZERO_S)){
                sNote = getString(R.string.not_note);

            }
        }

        txtNote.setText(sNote);

        editTask.setMovementMethod(LinkMovementMethod.getInstance()); // Делает кликабельным телефон (помимо настроек autoLink и

        status = oldStatus = ibt.getStatus();
        switch (status){
            case Status.id_pusto:
                imgPusto.setBackgroundColor(getResources().getColor(R.color.gray25));
                break;
            case Status.id_go:
                imgGo.setBackgroundColor(getResources().getColor(R.color.gray25));
                break;
            case Status.id_galka:
                imgGalka.setBackgroundColor(getResources().getColor(R.color.gray25));
                break;
            case Status.id_pause:
                imgPause.setBackgroundColor(getResources().getColor(R.color.gray25));
                break;
            case Status.id_cancel:
                iimgCancel.setBackgroundColor(getResources().getColor(R.color.gray25));
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_task_btn_cancel:
            {
                finish();
                break;
            }
            case R.id.edit_task_btn_save:
            {
                saveTask();
                finish();
                break;
            }
            case R.id.edit_task_btn_slave_x:
            {
                Public.hideSoftKeyboard(editTask, this);
                if (statUser == RegAsBoss.statIsPay || statUser == RegAsBoss.statTreal){
                    xSlave();
                } else {
                    regAsBoss.chooseDoAtStatBoss();
                }
                break;
            }
            case R.id.edit_task_btn_slave_plus:
            {
                Public.hideSoftKeyboard(editTask, this);
                if (statUser == RegAsBoss.statIsPay || statUser == RegAsBoss.statTreal){
                    plusSlave();
                } else {
                    regAsBoss.chooseDoAtStatBoss();
                }
                break;
            }
            case R.id.edit_task_text_slave:
            {
                Public.hideSoftKeyboard(editTask, this);
                if (statUser == RegAsBoss.statIsPay || statUser == RegAsBoss.statTreal){
                    chooseSlave();
                } else {
                    regAsBoss.chooseDoAtStatBoss();
                }
                break;
            }
            case R.id.edit_task_text_date:
            {
                showCalendar();
                break;
            }
            case R.id.edit_task_btn_date_minus:
            {
                dateMinus();
                break;
            }
            case R.id.edit_task_btn_date_plus:
            {
                datePlus();
                break;
            }
            case R.id.edit_task_btn_date_plus2:
            {
                datePlus2();
                break;
            }
            case R.id.edit_task_btn_calendar:
            {
                showCalendar();
                break;
            }
            case R.id.edit_task_btn_time:
            {
                showTime();
                break;
            }
            case R.id.edit_task_text_time:
            {
                showTime();
                break;
            }
            case R.id.edit_task_btn_info:
            {
                showInfo();
                break;
            }
            case R.id.edit_task_img_pusto:
            {
                changeStatus(Status.id_pusto);
                imgPusto.setBackgroundColor(getResources().getColor(R.color.gray25));
                Public.hideSoftKeyboard(editTask, this);
                break;
            }
            case R.id.edit_task_img_go:
            {
                changeStatus(Status.id_go);
                imgGo.setBackgroundColor(getResources().getColor(R.color.gray25));
                Public.hideSoftKeyboard(editTask, this);
                break;
            }
            case R.id.edit_task_img_galka:
            {
                changeStatus(Status.id_galka);
                imgGalka.setBackgroundColor(getResources().getColor(R.color.gray25));
                Public.hideSoftKeyboard(editTask, this);
                break;
            }
            case R.id.edit_task_img_pause:
            {
                changeStatus(Status.id_pause);
                imgPause.setBackgroundColor(getResources().getColor(R.color.gray25));
                Public.hideSoftKeyboard(editTask, this);
                break;
            }
            case R.id.edit_task_img_cancel:
            {
                changeStatus(Status.id_cancel);
                iimgCancel.setBackgroundColor(getResources().getColor(R.color.gray25));
                Public.hideSoftKeyboard(editTask, this);
                break;
            }
        }

    }

    private void saveTask() {

        long currentTime = System.currentTimeMillis();
        String taskFromEdit = editTask.getText().toString();
        ibt.setTask(taskFromEdit);
        ibt.setTaskForSort(Public.installTaskForSortFromTask(taskFromEdit));
        if (status != oldStatus){
            ibt.setStatus(status);
            ibt.setIdUserEditStatus(idUser);
            ibt.setDateEditStatus(currentTime);
        }
        dateSrok = cSrok.getTimeInMillis();
        ibt.setDateSrok(dateSrok);
        ibt.setDateLastEdit(currentTime);
        ibt.setDateDelivery(ZERO_L);
        ibt.setDateRead(ZERO_L);
        ibt.setIdSlave(keySlave);
        ibt.setDone(Status.id_done_no);
        BussinessTask bt = ibt.getBussinessTask();

        if (fromNotify){

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null) return;
            String idUser  = user.getUid();

            DatabaseReference dbRefTasks = FirebaseDatabase.getInstance().getReference(TASKS).child(idUser);
            dbRefTasks.keepSynced(true);
            dbRefTasks.child(keyPosition).setValue(bt);

        } else {
            dbRefTasks.child(keyPosition).setValue(bt);
        }


    }

    private void xSlave() {
        onSelectSlave(NOT_SLAVE_KEY, getString(R.string.setNobody));
    }

    private void plusSlave() {
        Intent intent = new Intent(this, AddSlaveActivity.class);
        startActivityForResult(intent, RESULT_ON_ADD_SLAVE);
    }

    private void chooseSlave() {
        Public.dbRefSlaves.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Slaves> listSlave = new ArrayList<>();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    Slaves slaves = ds.getValue(Slaves.class);
                    if (slaves == null) return;
                    slaves.setUidSvale(ds.getKey());
                    listSlave.add(slaves);
                }

                EditTaskActivity.ListSlaveDialog listSlaveDialog = new EditTaskActivity.ListSlaveDialog();
                listSlaveDialog.setList(listSlave, context);
                FragmentManager fragmentManager = getSupportFragmentManager();
                listSlaveDialog.show(fragmentManager, "listSlave");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static class ListSlaveDialog extends DialogFragment {

        ArrayList<Slaves> listSlave = new ArrayList<>();
        String list[];

        public void setList(ArrayList<Slaves> listSlave, Context context) {
            this.listSlave = listSlave;
            int n = listSlave.size()+1;
            list = new String[n];
            list[0] = context.getString(R.string.add_new_slave);
            for (int i=1; i<n; i++){
                list[i] = listSlave.get(i-1).getDisplayNameSlaveEdit();
            }
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.set_slave)
                    .setItems(list, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Activity thisActivity = getActivity();
                            if (thisActivity != null) {
                                if (which == ZERO){
                                    ((EditTaskActivity) thisActivity).plusSlave();
                                } else {
                                    ((EditTaskActivity) thisActivity).onSelectSlave(
                                            listSlave.get(which-1).getUidSvale(),
                                            listSlave.get(which-1).getDisplayNameSlaveEdit()
                                    );
                                }
                            }
                        }
                    });
            return builder.create();
        }
    }

    void onSelectSlave(String keySlave, String name){
        txtSlave.setText(name);
        this.keySlave = keySlave;
    }

    private void dateMinus() {
        cSrok.add(Calendar.DATE, -1);
        txtDate.setText(Public.setTxtDate(cSrok));
    }

    private void datePlus() {
        cSrok.add(Calendar.DATE, 1);
        txtDate.setText(Public.setTxtDate(cSrok));
    }

    private void datePlus2() {
        int day = cSrok.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.FRIDAY:
                cSrok.add(Calendar.DATE, 3);
                break;
            case Calendar.SATURDAY:
                cSrok.add(Calendar.DATE, 2);
                break;
            case Calendar.SUNDAY:
                cSrok.add(Calendar.DATE, 1);
                break;
            default:
                cSrok.add(Calendar.DATE, 1);
                break;
        }
        txtDate.setText(Public.setTxtDate(cSrok));
    }

    private void showCalendar() {
        DatePickerDialog dial = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        cSrok.set(year, monthOfYear, dayOfMonth);
                        txtDate.setText(Public.setTxtDate(cSrok));
                    }
                    // задаем начальную дату календаря
                },cSrok.get(Calendar.YEAR), cSrok.get(Calendar.MONTH), cSrok.get(Calendar.DATE)
        );
        dial.show();
    }

    private void showTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        cSrok.set(cSrok.get(Calendar.YEAR), cSrok.get(Calendar.MONTH), cSrok.get(Calendar.DATE), h, m);
                        txtTime.setText(Public.setTxtTime(cSrok));
                    }
                }, cSrok.get(Calendar.HOUR_OF_DAY), cSrok.get(Calendar.MINUTE),  true
        );
        timePickerDialog.show();
    }

    private void showInfo() {
        Intent intent = new Intent(getApplicationContext(), InfoTask.class);
        intent.putExtra(INFO_DATA_INTO, ibt.getDateInto());
        intent.putExtra(IFNO_DATA_LAST_EDIT, ibt.getDateLastEdit());
        intent.putExtra(INFO_DATE_DELIVERY, ibt.getDateDelivery());
        intent.putExtra(INFO_DATE_READ, ibt.getDateRead());
        intent.putExtra(INFO_DATE_EDIT_SATUS, ibt.getDateEditStatus());
        intent.putExtra(INFO_USER_EDIT_SATATUS,ibt.getIdUserEditStatus() );
        intent.putExtra(INFO_USER_ID, idUser);
        startActivity(intent);

    }

    private void changeStatus(int currentStatus){
        Public.hideSoftKeyboard(editTask, this);
        imgPusto.setBackgroundColor(Color.TRANSPARENT);
        imgGo.setBackgroundColor(Color.TRANSPARENT);
        imgGalka.setBackgroundColor(Color.TRANSPARENT);
        imgPause.setBackgroundColor(Color.TRANSPARENT);
        iimgCancel.setBackgroundColor(Color.TRANSPARENT);
        status = currentStatus;
    }

    @Override
    public void onBackPressed() {
        openDialog();
    }
    void openDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(EditTaskActivity.this);
        String s = getString(R.string.exit_without_save);
        s += "?";
        dialog.setTitle(s);
        dialog.setCancelable(false);

        dialog.setPositiveButton(R.string.exit_without_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Просто закрывается диалог
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data == null) return;
            switch (requestCode){
                case RESULT_ON_ADD_SLAVE:
                    keySlave = data.getStringExtra(SLAVE_KEY);
                    if (keySlave == null) return;
                    Public.dbRefSlaves.child(keySlave).child(Slaves.getPoleName_displayNameSlaveEdit())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String name = dataSnapshot.getValue(String.class);
                                    if (name == null) name = ZERO_S;
                                    txtSlave.setText(name);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    txtSlave.setText(ZERO_S);
                                }
                            });
                break;
            }
        }
    }
}

