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
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.babyartsoft.bussinestasks1.Interface.Constant;
import com.babyartsoft.bussinestasks1.Message.CallbackOnDataChangeRun;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import static com.babyartsoft.bussinestasks1.Public.dbRefBoss;
import static com.babyartsoft.bussinestasks1.Public.dbRefSlaves;
import static com.babyartsoft.bussinestasks1.Public.dbRefTasks;
import static com.babyartsoft.bussinestasks1.Public.mAuth;
import static com.babyartsoft.bussinestasks1.Public.regAsBoss;

public class NewTaskActivity extends AppCompatActivity implements View.OnClickListener, Constant {

    EditText    editNewOrder;
    long        dateIntoMillis, dateSrok;
    Calendar cSrok;
    TextView txtSlave, txtDate, txtTime;
    String keySlave;
    int statUser;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        context = this;

        Button btnCancel        = findViewById(R.id.new_task_btn_cancel);
        Button btnSave          = findViewById(R.id.new_task_btn_save);
        Button btnSlaveX        = findViewById(R.id.new_task_btn_slave_x);
        Button btnSlavePlus     = findViewById(R.id.new_task_btn_slave_plus);
        txtSlave                = findViewById(R.id.new_task_text_slave);
        txtDate                 = findViewById(R.id.new_task_text_date);
        Button btnDateMinus     = findViewById(R.id.new_task_btn_date_minus);
        Button btnDatePlus      = findViewById(R.id.new_task_btn_date_plus);
        Button btnDatePlus2     = findViewById(R.id.new_task_btn_date_plus2);
        Button btnDateCalindar  = findViewById(R.id.new_task_btn_date_calendar);
        Button btnTime          = findViewById(R.id.new_task_btn_time);
        txtTime                 = findViewById(R.id.new_task_text_time);

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

        editNewOrder = findViewById(R.id.new_task_edit_enter);
        dateIntoMillis = System.currentTimeMillis();
        // тут нужно модуль что бы убрать клавиатуру

        cSrok = Calendar.getInstance();
        dateIntoMillis = Calendar.getInstance().getTimeInMillis();
        cSrok.set(Calendar.HOUR_OF_DAY, 18);
        cSrok.set(Calendar.MINUTE, ZERO);
        cSrok.set(Calendar.SECOND, ZERO);
        cSrok.set(Calendar.MILLISECOND, ZERO);
        txtDate.setText(Public.setTxtDate(cSrok));
        txtTime.setText(Public.setTxtTime(cSrok));

        keySlave = NOT_SLAVE_KEY;
        txtSlave.setText(getString(R.string.setNobody));

        Intent intent = getIntent();
        String action = intent.getAction();
        if (action != null){                // если открыто по share снаружи
            String type = intent.getType();
            if (Intent.ACTION_SEND.equals(action) && type != null){

                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                editNewOrder.setText(sharedText);

                mAuth = FirebaseAuth.getInstance();
                Public.user = mAuth.getCurrentUser();
                if (Public.user == null){           // регистрации не было
                    btnSave.setEnabled(false);
                    showDialogNotReg();
                    return;
                }

                String uid = Public.user.getUid();
                dbRefTasks = FirebaseDatabase.getInstance().getReference(TASKS).child(uid);
                dbRefTasks.keepSynced(true);
                dbRefSlaves = FirebaseDatabase.getInstance().getReference(SLAVES).child(uid);
                dbRefSlaves.keepSynced(true);
                dbRefBoss = FirebaseDatabase.getInstance().getReference(BOSS).child(uid);
                dbRefBoss.keepSynced(true);
                regAsBoss = new RegAsBoss(this, this);
                regAsBoss.checkStatBoss(this);

                ListActivity.getLoopAndNonify(context, new CallbackOnDataChangeRun().getCallbackOnDataChange());
//                toLog("From NEW: "+"action= "+action+ " type= "+type + " sharedText = "+sharedText);
            }
        }
    }

    private void showDialogNotReg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name)
                .setMessage(R.string.you_not_reg)
                .setIcon(R.mipmap.ic_massage_large)
                .setCancelable(false)
                .setNegativeButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() { // При запуске и при закрытии диалога
        super.onResume();
        SharedPreferences sp = getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
        statUser = sp.getInt(USER_SELF_STAT_BOSS, RegAsBoss.statNoBoss);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.new_task_btn_cancel:
            {
                finish();
                break;
            }
            case R.id.new_task_btn_save:
            {
                saveTask();
                finish();
                break;
            }
            case R.id.new_task_btn_slave_x:
            {
                Public.hideSoftKeyboard(editNewOrder, this);
                if (statUser == RegAsBoss.statIsPay || statUser == RegAsBoss.statTreal){
                    xSlave();
                } else {
                    regAsBoss.chooseDoAtStatBoss();
                }
                break;
            }
            case R.id.new_task_btn_slave_plus:
            {
                Public.hideSoftKeyboard(editNewOrder, this);
                if (statUser == RegAsBoss.statIsPay || statUser == RegAsBoss.statTreal){
                    plusSlave();
                } else {
                    regAsBoss.chooseDoAtStatBoss();
                }
                break;
            }
            case R.id.new_task_text_slave:
            {
                Public.hideSoftKeyboard(editNewOrder, this);
                if (statUser == RegAsBoss.statIsPay || statUser == RegAsBoss.statTreal){
                    chooseSlave();
                } else {
                    regAsBoss.chooseDoAtStatBoss();
                }
                break;
            }
            case R.id.new_task_text_date:
            {
                showCalendar();
                break;
            }
            case R.id.new_task_btn_date_minus:
            {
                dateMinus();
                break;
            }
            case R.id.new_task_btn_date_plus:
            {
                datePlus();
                break;
            }
            case R.id.new_task_btn_date_plus2:
            {
                datePlus2();
                break;
            }
            case R.id.new_task_btn_date_calendar:
            {
                showCalendar();
                break;
            }
            case R.id.new_task_btn_time:
            {
                showTime();
                break;
            }
            case R.id.new_task_text_time:
            {
                showTime();
                break;
            }
        }

    }

    private void saveTask() {
        String s = ""+ editNewOrder.getText().toString();
        dateSrok = cSrok.getTimeInMillis();
        BussinessTask task = new BussinessTask(s, dateIntoMillis, dateSrok, keySlave);
//        final String keyNewTask = Public.dbRefTasks.push().getKey();
        Public.dbRefTasks.push().setValue(task);
    }

    private void xSlave() {
        onSelectSlave(NOT_SLAVE_KEY, getString(R.string.setNobody));
        Public.hideSoftKeyboard(editNewOrder, this);
    }

    private void plusSlave() {
        Intent intent = new Intent(this, AddSlaveActivity.class);
        startActivityForResult(intent, RESULT_ON_ADD_SLAVE);
    }

    private void chooseSlave() {
        Public.hideSoftKeyboard(editNewOrder, this);
        Public.dbRefSlaves.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Slaves> listSlave = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Slaves slaves = ds.getValue(Slaves.class);
                    if (slaves == null) return;
                    slaves.setUidSvale(ds.getKey());
                    listSlave.add(slaves);
                }

                NewTaskActivity.ListSlaveDialog listSlaveDialog = new NewTaskActivity.ListSlaveDialog();
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
                                    ((NewTaskActivity) thisActivity).plusSlave();
                                } else {
                                    ((NewTaskActivity) thisActivity).onSelectSlave(
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
        this.keySlave = keySlave;
        txtSlave.setText(name);
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

    @Override
    public void onBackPressed() {
        openDialog();
    }
    void openDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(NewTaskActivity.this);
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

        if (requestCode == RESULT_ON_ZVON) {
            if (resultCode == RESULT_OK) {
                String res = "";
                res += data.getStringExtra("taskFromCall");
                String old = "";
                old += editNewOrder.getText().toString();
                res = old + res;
                editNewOrder.setText(res);
                editNewOrder.setSelection(editNewOrder.getText().length());
                dateIntoMillis = data.getLongExtra("dateIntoMillis", dateIntoMillis);
            } else {
                String res = "";
                res += editNewOrder.getText().toString();
                editNewOrder.setText(res);
            }
        }
    }
}
