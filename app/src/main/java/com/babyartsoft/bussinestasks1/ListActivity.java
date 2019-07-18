package com.babyartsoft.bussinestasks1;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.babyartsoft.bussinestasks1.Help.AboutActivity;
import com.babyartsoft.bussinestasks1.Help.AboutUserActivity;
import com.babyartsoft.bussinestasks1.Help.MainHelpActivity;
import com.babyartsoft.bussinestasks1.Help.SlideActivity;
import com.babyartsoft.bussinestasks1.Interface.CallbackOnDataChange;
import com.babyartsoft.bussinestasks1.Interface.Constant;
import com.babyartsoft.bussinestasks1.Message.CallbackOnDataChangeRun;
import com.babyartsoft.bussinestasks1.Message.Device;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import static com.babyartsoft.bussinestasks1.Public.dbRefBoss;
import static com.babyartsoft.bussinestasks1.Public.dbRefTasks;
import static com.babyartsoft.bussinestasks1.Public.dbRefUser;
import static com.babyartsoft.bussinestasks1.Public.dbRefUsers;
import static com.babyartsoft.bussinestasks1.Public.dbRefSlaves;
import static com.babyartsoft.bussinestasks1.Public.listSlave;
import static com.babyartsoft.bussinestasks1.Public.listTask;
import static com.babyartsoft.bussinestasks1.Public.listTaskNotFilter;
import static com.babyartsoft.bussinestasks1.Public.mAuth;
import static com.babyartsoft.bussinestasks1.Public.regAsBoss;
import static com.babyartsoft.bussinestasks1.Public.toLog;
import static com.babyartsoft.bussinestasks1.Public.user;
import static com.babyartsoft.bussinestasks1.Public.adapter;
import static com.babyartsoft.bussinestasks1.R.drawable.ic_filter_off_white_36dp;
import static com.babyartsoft.bussinestasks1.R.drawable.ic_status_cancel_white_36dp;
import static com.babyartsoft.bussinestasks1.R.drawable.ic_status_galka_white_36dp;
import static com.babyartsoft.bussinestasks1.R.drawable.ic_status_go_white_36dp;
import static com.babyartsoft.bussinestasks1.R.drawable.ic_status_lock_white_36dp;
import static com.babyartsoft.bussinestasks1.R.drawable.ic_status_pause_white_36dp;
import static com.babyartsoft.bussinestasks1.R.drawable.ic_status_pusto_white_36dp;
import static com.babyartsoft.bussinestasks1.R.drawable.ic_status_unlock_white_36dp;

public class ListActivity extends AppCompatActivity
        implements Constant, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    GoogleSignInClient mGoogleSignInClient;
    RecyclerView recyclerView;
    ImageView imgArrow, imgStatus, imgDate, imgFindTask, imgDrawer, imgAddNew;
    LinearLayoutManager mLayoutManager;
    TextView txtNotData, txtSort, txtStringFilter;
    Setting setting;
    Filter filter;
    Comparator<ItemBussinessTask> sortComparator;
    Calendar calendarSrokBegin = Calendar.getInstance();
    Calendar calendarSrokEnd = Calendar.getInstance();
    String filterStatus, filterSrok, filterSlave;
    int currentPeriod;
    BroadcastReceiver broadcastEveryMinute;
    BroadcastReceiver broadcastChangeDate;
    FrameLayout baseFrameLayout;
    FrameLayout.LayoutParams baseLayoutParams;
    EditText editFind;
    boolean runFing = false;
    ProgressBar progressBar;


    final int MENU_SLAVE_ALL= View.generateViewId();
    final int MENU_SLAVE_ADD  = View.generateViewId();
    final int MENU_SLAVE_EDIT  = View.generateViewId();
    final int MENU_BOSS_EDIT  = View.generateViewId();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        setting = new Setting(this);
        filter = new Filter();

        txtNotData = findViewById(R.id.list_text_no_data);
        txtSort = findViewById(R.id.list_text_sort);
        txtStringFilter = findViewById(R.id.txtStringFilter);
        imgArrow = findViewById(R.id.list_image_arrow);
        imgStatus = findViewById(R.id.imgStatus);
        imgDate = findViewById(R.id.imgDate);
        imgFindTask = findViewById(R.id.imgFindTask);
        imgDrawer = findViewById(R.id.imgDrawer);
        imgAddNew = findViewById(R.id.list_btn_add_task);
        progressBar = findViewById(R.id.progressBar);

        setClickableMainView(false);

        recyclerView = findViewById(R.id.list_recycler);
        recyclerView.setHasFixedSize(true);

        baseFrameLayout = findViewById(R.id.list_base);
        baseLayoutParams= (FrameLayout.LayoutParams) recyclerView.getLayoutParams();

        editFind = findViewById(R.id.list_edit_find);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        getFirebaseAuth();          // соединение с сервером  Firebase для регистрации User и задает все переменные для доступа

        startCurruntUserOrGer();    // Запускает проверку пользователя, если нет регистрации, то регистрирует (по выбору из диалога)

    }

    private void setClickableMainView(boolean b) {
        txtSort.setClickable(b);
        imgArrow.setClickable(b);
        imgStatus.setClickable(b);
        imgDate.setClickable(b);
        imgFindTask.setClickable(b);
        imgDrawer.setClickable(b);
        imgAddNew.setClickable(b);
        progressBar.setVisibility(b?View.INVISIBLE:View.VISIBLE);
//        toLog("setClickableMainView - " + b);
    }

    private void setFilterString(String filterStatus, String filterSrok, String filterSlave) {
        String filterHead;
        filterHead = getString(R.string.filter_string_start);
        filterHead += ": ";
        filterHead += filterStatus;
        filterHead += ", ";
        filterHead += filterSrok;
        filterHead += ", ";
        filterHead += filterSlave;
        txtStringFilter.setText(filterHead);
    }

    // region   Методы привязаные к методу onClick в layout

    public void openDrawer(View v){
        drawer.openDrawer(GravityCompat.START);
    }

    public void addNewTask(View v){
        Intent newTaskIntent = new Intent(this, NewTaskActivity.class);
//        startActivityForResult(newTaskIntent, RESULT_ON_NEW);
        startActivity(newTaskIntent);
    }

    public void statusPopup(View v){


        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater != null ? layoutInflater.inflate(R.layout.popup_win_status, null) : null;
        if (view == null) return;
        final PopupWindow popupWindow = new PopupWindow(getApplicationContext());
//        final PopupWindow popupWindow = new PopupWindow(view);
        popupWindow.setContentView(view);
        popupWindow.setFocusable(true);

        View.OnClickListener atPopup = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.img_popup_status_reset:
                    case R.id.txt_popup_status_reset:
                        imgStatus.setImageResource(ic_filter_off_white_36dp);
                        filter.setStatusHead(FILTER_STATUS_OFF);
                        setting.setFilterStatusHead(FILTER_STATUS_OFF);
                        filterStatus = new Status().getNameStatusFilter(getApplicationContext(), FILTER_STATUS_OFF);
                        break;
                    case R.id.img_popup_status_unlock:
                    case R.id.txt_popup_status_unlock:
                        imgStatus.setImageResource(ic_status_unlock_white_36dp);
                        filter.setStatusHead(FILTER_STATUS_UNLOCK);
                        setting.setFilterStatusHead(FILTER_STATUS_UNLOCK);
                        filterStatus = new Status().getNameStatusFilter(getApplicationContext(), FILTER_STATUS_UNLOCK);
                        break;
                    case R.id.img_popup_status_lock:
                    case R.id.txt_popup_status_lock:
                        imgStatus.setImageResource(ic_status_lock_white_36dp);
                        filter.setStatusHead(FILTER_STATUS_LOCK);
                        setting.setFilterStatusHead(FILTER_STATUS_LOCK);
                        filterStatus = new Status().getNameStatusFilter(getApplicationContext(), FILTER_STATUS_LOCK);
                        break;
                    case R.id.img_popup_status_go:
                    case R.id.txt_popup_status_go:
                        imgStatus.setImageResource(ic_status_go_white_36dp);
                        filter.setStatusHead(FILTER_STATUS_GO);
                        setting.setFilterStatusHead(FILTER_STATUS_GO);
                        filterStatus = new Status().getNameStatusFilter(getApplicationContext(), FILTER_STATUS_GO);
                        break;
                    case R.id.img_popup_status_galka:
                    case R.id.txt_popup_status_galka:
                        imgStatus.setImageResource(ic_status_galka_white_36dp);
                        filter.setStatusHead(FILTER_STATUS_GALKA);
                        setting.setFilterStatusHead(FILTER_STATUS_GALKA);
                        filterStatus = new Status().getNameStatusFilter(getApplicationContext(), FILTER_STATUS_GALKA);
                        break;
                    case R.id.img_popup_status_pusto:
                    case R.id.txt_popup_status_pusto:
                        imgStatus.setImageResource(ic_status_pusto_white_36dp);
                        filter.setStatusHead(FILTER_STATUS_PUSTO);
                        setting.setFilterStatusHead(FILTER_STATUS_PUSTO);
                        filterStatus = new Status().getNameStatusFilter(getApplicationContext(), FILTER_STATUS_PUSTO);
                        break;
                    case R.id.img_popup_status_pause:
                    case R.id.txt_popup_status_pause:
                        imgStatus.setImageResource(ic_status_pause_white_36dp);
                        filter.setStatusHead(FILTER_STATUS_PAUSE);
                        setting.setFilterStatusHead(FILTER_STATUS_PAUSE);
                        filterStatus = new Status().getNameStatusFilter(getApplicationContext(), FILTER_STATUS_PAUSE);
                        break;
                    case R.id.img_popup_status_cancel:
                    case R.id.txt_popup_status_cancel:
                        imgStatus.setImageResource(ic_status_cancel_white_36dp);
                        filter.setStatusHead(FILTER_STATUS_CANCEL);
                        setting.setFilterStatusHead(FILTER_STATUS_CANCEL);
                        filterStatus = new Status().getNameStatusFilter(getApplicationContext(), FILTER_STATUS_CANCEL);
                        break;
                }
                filter.refresh();
                setFilterString(filterStatus, filterSrok, filterSlave);
                sortListTask();
                adapter.notifyDataSetChanged();
                seeSizeList();

                popupWindow.dismiss();
            }
        };
        view.findViewById(R.id.img_popup_status_reset).setOnClickListener(atPopup);
        view.findViewById(R.id.img_popup_status_unlock).setOnClickListener(atPopup);
        view.findViewById(R.id.img_popup_status_lock).setOnClickListener(atPopup);
        view.findViewById(R.id.img_popup_status_go).setOnClickListener(atPopup);
        view.findViewById(R.id.img_popup_status_galka).setOnClickListener(atPopup);
        view.findViewById(R.id.img_popup_status_pusto).setOnClickListener(atPopup);
        view.findViewById(R.id.img_popup_status_pause).setOnClickListener(atPopup);
        view.findViewById(R.id.img_popup_status_cancel).setOnClickListener(atPopup);

        view.findViewById(R.id.txt_popup_status_reset).setOnClickListener(atPopup);
        view.findViewById(R.id.txt_popup_status_unlock).setOnClickListener(atPopup);
        view.findViewById(R.id.txt_popup_status_lock).setOnClickListener(atPopup);
        view.findViewById(R.id.txt_popup_status_go).setOnClickListener(atPopup);
        view.findViewById(R.id.txt_popup_status_galka).setOnClickListener(atPopup);
        view.findViewById(R.id.txt_popup_status_pusto).setOnClickListener(atPopup);
        view.findViewById(R.id.txt_popup_status_pause).setOnClickListener(atPopup);
        view.findViewById(R.id.txt_popup_status_cancel).setOnClickListener(atPopup);

        popupWindow.showAsDropDown(v);
//        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
//        PopupWindowCompat.showAsDropDown(popupWindow, v, 0, 0, Gravity.NO_GRAVITY);
//        Public.toLog("statusPopup " + "End");

    }

    public void datePopup(View v){

        final Context context = this;
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater != null ? layoutInflater.inflate(R.layout.popup_win_date, null) : null;
        if (view == null) return;
        final PopupWindow popupWindow = new PopupWindow(getApplicationContext());
        popupWindow.setContentView(view);
        popupWindow.setFocusable(true);
        final long oldCalendarBegin = calendarSrokBegin.getTimeInMillis();
        final long oldCalendarEnd = calendarSrokEnd.getTimeInMillis();

        final TextView txtDateBegin = view.findViewById(R.id.txt_popup_win_date_begin);
        final TextView txtDateEnd = view.findViewById(R.id.txt_popup_win_date_end);
        final TextView txtDateHeadBegin = view.findViewById(R.id.txt_popup_win_date_head_begin);
        final TextView txtDateHeadEnd = view.findViewById(R.id.txt_popup_win_date_head_end);
        Button btnYesterday = view.findViewById(R.id.btn_popup_win_date_yesterday);
        Button btnToday = view.findViewById(R.id.btn_popup_win_date_today);
        Button btnTomorrow = view.findViewById(R.id.btn_popup_win_date_tomorrow);
        Button btnWeek = view.findViewById(R.id.btn_popup_win_date_week);
        Button btnMonth = view.findViewById(R.id.btn_popup_win_date_month);
        Button btnYear = view.findViewById(R.id.btn_popup_win_date_year);
        Button btnDay = view.findViewById(R.id.btn_popup_win_date_day);
        Button btnMinus = view.findViewById(R.id.btn_popup_win_date_minus);
        Button btnPlus = view.findViewById(R.id.btn_popup_win_date_plus);
        Button btnReset = view.findViewById(R.id.btn_popup_win_date_reset);
        Button btnYesterdayMore = view.findViewById(R.id.btn_popup_win_date_yesterday_more);
        Button btnTodayMore = view.findViewById(R.id.btn_popup_win_date_today_more);
        Button btnSave = view.findViewById(R.id.btn_popup_win_date_save);
        Button btnCancel = view.findViewById(R.id.btn_popup_win_date_cancel);

        calendarSrokBegin.setTimeInMillis(setting.getFilterSrokBegin());
        calendarSrokEnd.setTimeInMillis(setting.getFilterSrokEnd());
        if (setting.getFilterSrokBegin() == Long.MIN_VALUE) txtDateBegin.setText(R.string.infinity);
            else txtDateBegin.setText(Public.setTxtDate(calendarSrokBegin));
        if (setting.getFilterSrokEnd() == Long.MAX_VALUE) txtDateEnd.setText(R.string.infinity);
            else txtDateEnd.setText(Public.setTxtDate(calendarSrokEnd));

        final String filterSrokOld = filterSrok;
        currentPeriod = filter.getPeriod();

        View.OnClickListener clkDatePopup = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                        case R.id.txt_popup_win_date_begin:
                        case R.id.txt_popup_win_date_head_begin:
                            if (calendarSrokBegin.getTimeInMillis() == Long.MIN_VALUE) {
                                calendarSrokBegin = Public.getSrokTodayBegin();
                            }
                            DatePickerDialog dialBegin = new DatePickerDialog(context,
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            calendarSrokBegin.set(year, monthOfYear, dayOfMonth);
                                            txtDateBegin.setText(Public.setTxtDate(calendarSrokBegin));
                                            setFilterSrokString();
                                            currentPeriod = FILTER_DATE_NOT;
                                        }
                                        // задаем начальную дату календаря
                                    }, calendarSrokBegin.get(Calendar.YEAR), calendarSrokBegin.get(Calendar.MONTH), calendarSrokBegin.get(Calendar.DATE)
                            );
                            dialBegin.show();
                        break;
                    case R.id.txt_popup_win_date_end:
                    case R.id.txt_popup_win_date_head_end:
                        if (calendarSrokEnd.getTimeInMillis() == Long.MAX_VALUE){
                            calendarSrokEnd = Public.getSrokTodayEnd();
                        }
                        DatePickerDialog dialEnd = new DatePickerDialog(context,
                                new DatePickerDialog.OnDateSetListener(){
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        calendarSrokEnd.set(year, monthOfYear, dayOfMonth, 23, 59, 59);
                                        txtDateEnd.setText(Public.setTxtDate(calendarSrokEnd));
                                        setFilterSrokString();
                                        currentPeriod = FILTER_DATE_NOT;
                                    }
                                    // задаем начальную дату календаря
                                },calendarSrokEnd.get(Calendar.YEAR), calendarSrokEnd.get(Calendar.MONTH), calendarSrokEnd.get(Calendar.DATE)
                        );
                        dialEnd.show();
                        break;
                    case R.id.btn_popup_win_date_yesterday:
                        calendarSrokBegin = Public.getSrokTodayBegin();
                        calendarSrokEnd = Public.getSrokTodayEnd();
                        calendarSrokBegin.add(Calendar.DATE, -1);
                        calendarSrokEnd.add(Calendar.DATE, -1);
                        txtDateBegin.setText(Public.setTxtDate(calendarSrokBegin));
                        txtDateEnd.setText(Public.setTxtDate(calendarSrokEnd));
                        filterSrok = getString(R.string.yesterday);
                        currentPeriod = FILTER_DATE_YESTERDAY;
                        break;
                    case R.id.btn_popup_win_date_today:
                        calendarSrokBegin = Public.getSrokTodayBegin();
                        calendarSrokEnd = Public.getSrokTodayEnd();
                        txtDateBegin.setText(Public.setTxtDate(calendarSrokBegin));
                        txtDateEnd.setText(Public.setTxtDate(calendarSrokEnd));
                        filterSrok = getString(R.string.today);
                        currentPeriod = FILTER_DATE_TODAY;
                        break;
                    case R.id.btn_popup_win_date_tomorrow:
                        calendarSrokBegin = Public.getSrokTodayBegin();
                        calendarSrokEnd = Public.getSrokTodayEnd();
                        calendarSrokBegin.add(Calendar.DATE, 1);
                        calendarSrokEnd.add(Calendar.DATE, 1);
                        txtDateBegin.setText(Public.setTxtDate(calendarSrokBegin));
                        txtDateEnd.setText(Public.setTxtDate(calendarSrokEnd));
                        filterSrok = getString(R.string.tomorrow);
                        currentPeriod = FILTER_DATE_TOMORROW;
                        break;
                    case R.id.btn_popup_win_date_week:
                        calendarSrokBegin = Public.getSrokTodayBegin();
                        calendarSrokEnd = Public.getSrokTodayEnd();
                        calendarSrokBegin.set(Calendar.DAY_OF_WEEK, calendarSrokBegin.getFirstDayOfWeek());
                        calendarSrokEnd.set(Calendar.DAY_OF_WEEK, calendarSrokEnd.getFirstDayOfWeek());
                        calendarSrokEnd.add(Calendar.DATE, 6);
                        txtDateBegin.setText(Public.setTxtDate(calendarSrokBegin));
                        txtDateEnd.setText(Public.setTxtDate(calendarSrokEnd));
                        filterSrok = getString(R.string.week);
                        currentPeriod = FILTER_DATE_WEEK;
                        break;
                    case R.id.btn_popup_win_date_month:
                        calendarSrokBegin = Public.getSrokTodayBegin();
                        calendarSrokEnd = Public.getSrokTodayEnd();
                        calendarSrokBegin.set(calendarSrokBegin.get(Calendar.YEAR), calendarSrokBegin.get(Calendar.MONTH), 1);
                        calendarSrokEnd.set(calendarSrokEnd.get(Calendar.YEAR), calendarSrokEnd.get(Calendar.MONTH), 1);
                        calendarSrokEnd.add(Calendar.MONTH, 1);
                        calendarSrokEnd.add(Calendar.DATE,-1 );
                        txtDateBegin.setText(Public.setTxtDate(calendarSrokBegin));
                        txtDateEnd.setText(Public.setTxtDate(calendarSrokEnd));
                        filterSrok = getString(R.string.month);
                        currentPeriod = FILTER_DATE_MONTH;
                        break;
                    case R.id.btn_popup_win_date_year:
                        calendarSrokBegin = Public.getSrokTodayBegin();
                        calendarSrokEnd = Public.getSrokTodayEnd();
                        calendarSrokBegin.set(calendarSrokBegin.get(Calendar.YEAR), Calendar.JANUARY, 1);
                        calendarSrokEnd.set(calendarSrokEnd.get(Calendar.YEAR), Calendar.JANUARY, 1);
                        calendarSrokEnd.add(Calendar.YEAR, 1);
                        calendarSrokEnd.add(Calendar.DATE,-1 );
                        txtDateBegin.setText(Public.setTxtDate(calendarSrokBegin));
                        txtDateEnd.setText(Public.setTxtDate(calendarSrokEnd));
                        filterSrok = getString(R.string.year);
                        currentPeriod = FILTER_DATE_YEAR;
                        break;
                    case R.id.btn_popup_win_date_day:
                        calendarSrokBegin = Public.getSrokTodayBegin();
                        calendarSrokEnd = Public.getSrokTodayEnd();
                        DatePickerDialog dialDay = new DatePickerDialog(context,
                                new DatePickerDialog.OnDateSetListener(){
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        calendarSrokBegin.set(year, monthOfYear, dayOfMonth);
                                        calendarSrokEnd.set(year, monthOfYear, dayOfMonth, 23, 59, 59);
                                        txtDateBegin.setText(Public.setTxtDate(calendarSrokBegin));
                                        txtDateEnd.setText(Public.setTxtDate(calendarSrokEnd));
                                        setFilterSrokString();
                                        currentPeriod = FILTER_DATE_NOT;
                                    }
                                    // задаем начальную дату календаря
                                },calendarSrokBegin.get(Calendar.YEAR), calendarSrokBegin.get(Calendar.MONTH), calendarSrokBegin.get(Calendar.DATE)
                        );
                        dialDay.show();
                        break;
                    case R.id.btn_popup_win_date_minus:
                        if (calendarSrokBegin.getTimeInMillis() > Long.MIN_VALUE) {
                            calendarSrokBegin.add(Calendar.DATE, -1);
                            txtDateBegin.setText(Public.setTxtDate(calendarSrokBegin));
                            setFilterSrokString();
                            currentPeriod = FILTER_DATE_NOT;
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.limit_date, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.btn_popup_win_date_plus:
                        if (calendarSrokEnd.getTimeInMillis() < Long.MAX_VALUE) {
                            calendarSrokEnd.add(Calendar.DATE, 1);
                            txtDateEnd.setText(Public.setTxtDate(calendarSrokEnd));
                            setFilterSrokString();
                            currentPeriod = FILTER_DATE_NOT;
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.limit_date, Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.btn_popup_win_date_reset:
                        calendarSrokBegin = Public.getSrokTodayBegin();
                        calendarSrokEnd = Public.getSrokTodayEnd();
                        calendarSrokBegin.setTimeInMillis(Long.MIN_VALUE);
                        calendarSrokEnd.setTimeInMillis(Long.MAX_VALUE);
                        txtDateBegin.setText(R.string.infinity);
                        txtDateEnd.setText(R.string.infinity);
                        setFilterSrokString();
                        currentPeriod = FILTER_DATE_NOT;
                        break;
                    case R.id.btn_popup_win_date_yesterday_more:
                        calendarSrokBegin = Public.getSrokTodayBegin();
                        calendarSrokEnd = Public.getSrokTodayEnd();
                        calendarSrokBegin.setTimeInMillis(Long.MIN_VALUE);
                        calendarSrokEnd.add(Calendar.DATE, -1);
                        txtDateBegin.setText(R.string.infinity);
                        txtDateEnd.setText(Public.setTxtDate(calendarSrokEnd));
                        filterSrok = getString(R.string.yesterday_and_more);
                        currentPeriod = FILTER_DATE_YESTERDAY_MORE;
                        break;
                    case R.id.btn_popup_win_date_today_more:
                        calendarSrokBegin = Public.getSrokTodayBegin();
                        calendarSrokEnd = Public.getSrokTodayEnd();
                        calendarSrokEnd.setTimeInMillis(Long.MAX_VALUE);
                        txtDateBegin.setText(Public.setTxtDate(calendarSrokBegin));
                        txtDateEnd.setText(R.string.infinity);
                        filterSrok = getString(R.string.today_and_more);
                        currentPeriod = FILTER_DATE_TODAY_MORE;
                        break;
                    case R.id.btn_popup_win_date_cancel:
                        calendarSrokBegin.setTimeInMillis(oldCalendarBegin);
                        calendarSrokEnd.setTimeInMillis(oldCalendarEnd);
                        filterSrok = filterSrokOld;
                        popupWindow.dismiss();
                        break;
                    case R.id.btn_popup_win_date_save:
                        if (calendarSrokBegin.getTimeInMillis() <= calendarSrokEnd.getTimeInMillis()) {
                            setting.setFilterSrokBegin(calendarSrokBegin.getTimeInMillis());
                            setting.setFilterSrokEnd(calendarSrokEnd.getTimeInMillis());
                            setting.setFilterSrokString(filterSrok);
                            setting.setFilterDatePeriod(currentPeriod);
                            setFilterString(filterStatus, filterSrok, filterSlave);
                            filter.setSrokBegin(setting.getFilterSrokBegin());
                            filter.setSrokEnd(setting.getFilterSrokEnd());
                            filter.setPeriod(currentPeriod);

                            filter.refresh();
                            setFilterString(filterStatus, filterSrok, filterSlave);
                            sortListTask();
                            adapter.notifyDataSetChanged();
                            seeSizeList();

                            popupWindow.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.not_correct_date, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };

        txtDateBegin.setOnClickListener(clkDatePopup);
        txtDateEnd.setOnClickListener(clkDatePopup);
        txtDateHeadBegin.setOnClickListener(clkDatePopup);
        txtDateHeadEnd.setOnClickListener(clkDatePopup);
        btnYesterday.setOnClickListener(clkDatePopup);
        btnToday.setOnClickListener(clkDatePopup);
        btnTomorrow.setOnClickListener(clkDatePopup);
        btnWeek.setOnClickListener(clkDatePopup);
        btnMonth.setOnClickListener(clkDatePopup);
        btnYear.setOnClickListener(clkDatePopup);
        btnDay.setOnClickListener(clkDatePopup);
        btnMinus.setOnClickListener(clkDatePopup);
        btnPlus.setOnClickListener(clkDatePopup);
        btnReset.setOnClickListener(clkDatePopup);
        btnYesterdayMore.setOnClickListener(clkDatePopup);
        btnTodayMore.setOnClickListener(clkDatePopup);
        btnSave.setOnClickListener(clkDatePopup);
        btnCancel.setOnClickListener(clkDatePopup);

        popupWindow.showAsDropDown(v);
    }

    public void findTask(View v){

        editFind.setVisibility(View.VISIBLE);
        editFind.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) // запускаем клавиатуру
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, ZERO);
        int height = editFind.getHeight();
        int bottom = (int) getResources().getDimension(R.dimen.recycler_bottom);
        baseLayoutParams.setMargins(ZERO, height, ZERO, bottom);
        recyclerView.setLayoutParams(baseLayoutParams);
        runFing = true;

        editFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter.setTask(s.toString());
                filter.refresh();
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void sortPopup(View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popup_sort);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.item_sort_task:
                        sortComparator = getComparatorForSortListTask(SORT_TASK);
                        setting.setSortLiesTask(SORT_TASK);
                        txtSort.setText(R.string.sort_by_task);
                        setting.setStrSort(getString(R.string.sort_by_task));
                        break;
                    case R.id.item_sort_into:
                        sortComparator = getComparatorForSortListTask(SORT_INTO);
                        setting.setSortLiesTask(SORT_INTO);
                        txtSort.setText(R.string.sort_by_into);
                        setting.setStrSort(getString(R.string.sort_by_into));
                        break;
                    case R.id.item_sort_srok:
                        sortComparator = getComparatorForSortListTask(SORT_SROK);
                        setting.setSortLiesTask(SORT_SROK);
                        txtSort.setText(R.string.sort_by_srok);
                        setting.setStrSort(getString(R.string.sort_by_srok));
                        break;
                    case R.id.item_sort_slave:
                        sortComparator = getComparatorForSortListTask(SORT_SLAVE);
                        setting.setSortLiesTask(SORT_SLAVE);
                        txtSort.setText(R.string.sort_by_slave);
                        setting.setStrSort(getString(R.string.sort_by_slave));
                        break;
                    case R.id.item_sort_status:
                        sortComparator = getComparatorForSortListTask(SORT_STATUS);
                        setting.setSortLiesTask(SORT_STATUS);
                        txtSort.setText(R.string.sort_by_status);
                        setting.setStrSort(getString(R.string.sort_by_status));
                        break;
                    case R.id.item_sort_by_last_edit:
                        sortComparator = getComparatorForSortListTask(SORT_LAST_EDIT);
                        setting.setSortLiesTask(SORT_LAST_EDIT);
                        txtSort.setText(R.string.sort_by_last_edit);
                        setting.setStrSort(getString(R.string.sort_by_last_edit));
                        break;
                    case R.id.item_sort_by_date_status:
                        sortComparator = getComparatorForSortListTask(SORT_DATE_STATUS);
                        setting.setSortLiesTask(SORT_DATE_STATUS);
                        txtSort.setText(R.string.sort_by_date_status);
                        setting.setStrSort(getString(R.string.sort_by_date_status));
                        break;
                }

                if (sortComparator != null && listTask != null) {
                    Collections.sort(listTask,sortComparator);
                    if (setting.isArrowListTask()) Collections.reverse(listTask);
                }
                adapter.notifyDataSetChanged();
                seeSizeList();

                return false;
            }
        });
        popupMenu.show();
    }

    Comparator<ItemBussinessTask> getComparatorForSortListTask(int sort){
        switch (sort){
            case SORT_TASK:
                return new Comparator<ItemBussinessTask>() {
                    @Override
                    public int compare(ItemBussinessTask o1, ItemBussinessTask o2) {
                        return o1.getTaskForSort().compareTo(o2.getTaskForSort());
                    }
                };
            case SORT_INTO:
                return new Comparator<ItemBussinessTask>() {
                    @Override
                    public int compare(ItemBussinessTask o1, ItemBussinessTask o2) {
                        if (o1.getDateInto() < o2.getDateInto()) return MINUS_1;
                        if (o1.getDateInto() > o2.getDateInto()) return PLUS_1;
                        return ZERO;
                    }
                };
            case SORT_SROK:
                return new Comparator<ItemBussinessTask>() {
                    @Override
                    public int compare(ItemBussinessTask o1, ItemBussinessTask o2) {
                        if (o1.getDateSrok() < o2.getDateSrok()) return MINUS_1;
                        if (o1.getDateSrok() > o2.getDateSrok()) return PLUS_1;
                        return o1.getTaskForSort().compareTo(o2.getTaskForSort());
                    }
                };
            case SORT_SLAVE:
                return new Comparator<ItemBussinessTask>() {
                    @Override
                    public int compare(ItemBussinessTask o1, ItemBussinessTask o2) {
                        String idSlave1 = o1.getIdSlave();
                        String idSlave2 = o2.getIdSlave();
                        String name1 = "", name2 = "";
                        for (int i=0; i<listSlave.size();i++){
                            String keyFind = listSlave.get(i).getKey();
                            if (idSlave1.equals(keyFind)){
                                if (idSlave1.equals(user.getUid())){
                                    name1 = FOR_MY_KEY;
                                } else if (idSlave1.equals(NOT_SLAVE_KEY)){
                                    name1 = NOT_SLAVE_KEY;
                                } else {
                                    name1 = listSlave.get(i).getDisplayNameSlaveEdit();
                                }
                                break;
                            }
                        }
                        for (int i=0; i<listSlave.size();i++){
                            String keyFind = listSlave.get(i).getKey();
                            if (idSlave2.equals(keyFind)){
                                if (idSlave2.equals(user.getUid())){
                                    name2 = FOR_MY_KEY;
                                } else if (idSlave2.equals(NOT_SLAVE_KEY)){
                                    name2 = NOT_SLAVE_KEY;
                                } else {
                                    name2 = listSlave.get(i).getDisplayNameSlaveEdit();
                                }
                                break;
                            }
                        }
                        int res = name1.compareTo(name2);
                        if (res != ZERO) return res;
                        else return  o1.getTaskForSort().compareTo(o2.getTaskForSort());
                    }
                };
            case SORT_STATUS:
                return new Comparator<ItemBussinessTask>() {
                    @Override
                    public int compare(ItemBussinessTask o1, ItemBussinessTask o2) {
                        if (o1.getStatus() < o2.getStatus()) return MINUS_1;
                        if (o1.getStatus() > o2.getStatus()) return PLUS_1;
                        return o1.getTaskForSort().compareTo(o2.getTaskForSort());
                    }
                };
            case SORT_LAST_EDIT:
                return  new Comparator<ItemBussinessTask>() {
                    @Override
                    public int compare(ItemBussinessTask o1, ItemBussinessTask o2) {
                        if (o1.getDateLastEdit() < o2.getDateLastEdit()) return MINUS_1;
                        if (o1.getDateLastEdit() > o2.getDateLastEdit()) return PLUS_1;
                        return o1.getTaskForSort().compareTo(o2.getTaskForSort());
                    }
                };
            case SORT_DATE_STATUS:
                return  new Comparator<ItemBussinessTask>() {
                    @Override
                    public int compare(ItemBussinessTask o1, ItemBussinessTask o2) {
                        if (o1.getDateEditStatus() < o2.getDateEditStatus()) return MINUS_1;
                        if (o1.getDateEditStatus() > o2.getDateEditStatus()) return PLUS_1;
                        return o1.getTaskForSort().compareTo(o2.getTaskForSort());
                    }
                };

        }
        return null;
    }

    public void clkArrow(View v){

        setting.setArrowListTask(!setting.isArrowListTask());
        drowArrow(setting.isArrowListTask());


        if (sortComparator != null) {
            Collections.sort(listTask,sortComparator);
            if (setting.isArrowListTask()) Collections.reverse(listTask);
        }
        adapter.notifyDataSetChanged();

    }

    // endregion

    void setFilterSrokString(){
        filterSrok = " ";
        if (calendarSrokBegin.getTimeInMillis() != Long.MIN_VALUE){
            filterSrok += Public.setTxtDate(calendarSrokBegin);
        } else {
            filterSrok += getString(R.string.infinity);
        }
        filterSrok += " - ";
        if (calendarSrokEnd.getTimeInMillis() != Long.MAX_VALUE){
            filterSrok += Public.setTxtDate(calendarSrokEnd);
        } else {
            filterSrok += getString(R.string.infinity);
        }
    }

    void drowArrow(boolean direct){
        if (direct)    imgArrow.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
            else imgArrow.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
    }

    void setDrawerMenu(){
        // Формирует боковое меню
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // кнопка Справки в заголовке навигации
        Button btnHeadHelp = navigationView.getHeaderView(ZERO).findViewById(R.id.btn_head_help);
        btnHeadHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLog("===btnHeadHelp ===");
                Intent intentHelp = new Intent(getApplicationContext(), MainHelpActivity.class);
                startActivity(intentHelp);
            }
        });

        // Основное меню
        Menu navMenu = navigationView.getMenu();
        MenuItem itemSlave = navMenu.getItem(ZERO); // берем первый пункт (номер у него ZERO)
        SubMenu subMenuSlave = itemSlave.getSubMenu();
        subMenuSlave.clear();
        subMenuSlave.add(R.id.group_slave, MENU_SLAVE_ALL, ZERO, getString(R.string.allSlave))
                .setIcon(R.drawable.ic_drawer_slave_black_24dp);
        MenuItem menuItem;
        for (int i=0; i<listSlave.size(); i++) {
            int id = View.generateViewId();         // генерит id точно не совпадающий
            menuItem = subMenuSlave.add(R.id.group_slave, id, ZERO, listSlave.get(i).getDisplayNameSlaveEdit());
            if (filter.getSlave() != null) {
                if ((filter.getSlave()).equals(listSlave.get(i).getKey())) {
                    menuItem.setIcon(R.drawable.ic_cheese_item_black_24dp);
                }
            }
            listSlave.get(i).setIdItemMenu(id);
        }
        subMenuSlave.add(R.id.group_slave, MENU_SLAVE_ADD, ZERO, getString(R.string.addSlave))
                .setIcon(R.drawable.ic_add_circle_black_24dp);
        subMenuSlave.add(R.id.group_slave, MENU_SLAVE_EDIT, ZERO, getString(R.string.editSlave))
                .setIcon(R.drawable.ic_edit_black_24dp);
        subMenuSlave.add(R.id.group_slave, MENU_BOSS_EDIT, ZERO, getString(R.string.editBoss))
                .setIcon(R.drawable.ic_boss_fill_circle);
    }


    private void getFirebaseAuth() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
    }

    void startingSetting(FirebaseUser currentUser){

        user = currentUser;
        String idUser  = user.getUid();

        dbRefTasks = FirebaseDatabase.getInstance().getReference(TASKS).child(idUser);
        dbRefTasks.keepSynced(true);                  // настройка для записи без органичений

        dbRefSlaves = FirebaseDatabase.getInstance().getReference(SLAVES).child(idUser);
        dbRefSlaves.keepSynced(true);
        dbRefBoss = FirebaseDatabase.getInstance().getReference(BOSS).child(idUser);
        dbRefBoss.keepSynced(true);

        regAsBoss = new RegAsBoss(this, this);
        regAsBoss.checkStatBoss(this);


        setRefUser();

//        FirebaseMessaging.getInstance().subscribeToTopic(user.getUid());

        createAdapter();

        helpRunOnstart();

    }

    void helpRunOnstart(){
        SharedPreferences sp = getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
        boolean isRun = sp.getBoolean(HELP_RUN_ON_START, true);
        if (isRun){
            Intent intentRunOnStart = new Intent(this, SlideActivity.class);
            intentRunOnStart.putExtra(HELP_VIEW_PAGE_ADAPTER, view_about);
            startActivity(intentRunOnStart);
        }
    }

    protected void setRefUser(){

        SharedPreferences sp = getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
        String keyUser = sp.getString(KEY_USER_SELF, ZERO_S);
        SharedPreferences.Editor editor = sp.edit();

        DatabaseReference dbDevices = FirebaseDatabase.getInstance().getReference(DEVICES).child(user.getUid());
        dbDevices.keepSynced(true);

        boolean firstRun = sp.getBoolean(FIRST_RUN, true);
        boolean isToken = sp.getBoolean(IT_IS_NEW_TOKEN, false);

        if (firstRun){
            String token = sp.getString(ID_TOKEN_THIS_DEVICE, ZERO_S);
            long currentTime = System.currentTimeMillis();
            String name = Build.MANUFACTURER + " " + Build.MODEL;
            int version = BuildConfig.VERSION_CODE;
            Device device= new Device(currentTime, name, token, version);
            String keyDevice = dbDevices.push().getKey();
            if (keyDevice != null){
                dbDevices.child(keyDevice).setValue(device);
                editor.putString(KEY_DEVICE, keyDevice);
                editor.putBoolean(FIRST_RUN, false);
                editor.apply();
            }
        } else if (isToken) {
            String keyDevice = sp.getString(KEY_DEVICE, ZERO_S);
            if (!keyDevice.equals(ZERO_S)){
                String token = sp.getString(ID_TOKEN_THIS_DEVICE, ZERO_S);
                dbDevices.child(keyDevice).child("token").setValue(token);
                editor.putBoolean(IT_IS_NEW_TOKEN, false);
                editor.apply();
            }
        }

        if (!keyUser.equals(ZERO_S)) {
            dbRefUser = FirebaseDatabase.getInstance().getReference(USERS).child(keyUser);
            dbRefUser.keepSynced(true);

            dbRefUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User u = dataSnapshot.getValue(User.class);
                    final SharedPreferences sp = getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
                    if (u != null) {
                        SharedPreferences.Editor editor = sp.edit();
                        int stat = sp.getInt(USER_SELF_STAT_BOSS, ZERO);
                        if (stat != u.getStatAsBoss()) {
                            editor.putInt(USER_SELF_STAT_BOSS, u.getStatAsBoss());
                        }
                        long date = sp.getLong(USER_SELF_DATE_BOSS, ZERO_L);
                        if (date != u.getDateRegAsBossTreal()) {
                            editor.putLong(USER_SELF_DATE_BOSS, u.getDateRegAsBossTreal());
                        }
                        String token = sp.getString(USER_SELF_PurchaseToken, ZERO_S);
                        if (!token.equals(u.getPurchaseToken())) {
                            editor.putString(USER_SELF_PurchaseToken, u.getPurchaseToken());
                        }
                        editor.apply();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {        // Если ключа нет, значить пусть перерегистрируется
            boolean isInet = isOnline(this);    // есть ли интернет

            if (isInet) {
                RegDialog regDialog = new RegDialog(this, mGoogleSignInClient);
                regDialog.show();
            } else {
                showDialErrorReg();
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        setting.getSettingFromFile(this);

        txtSort.setText(setting.getStrSort());
        drowArrow(setting.isArrowListTask());
        setImgStatusOnHead();

        filter.setStatusHead(setting.getFilterStatusHead());
        filter.setSrokBegin(setting.getFilterSrokBegin());
        filter.setSrokEnd(setting.getFilterSrokEnd());
        filter.setSlave(setting.getFilterSlave());
        filter.setPeriod(setting.getFilterDatePeriod());

        filterStatus = new Status().getNameStatusFilter(this, setting.getFilterStatusHead());
        filterSrok = setting.getFilterSrokString();
        filterSlave = setting.getFilterSlaveString();

        setFilterString(filterStatus, filterSrok, filterSlave);

        checkDateFilter();

        // обновляем список каждую минуту, слушая системные часы
        broadcastEveryMinute = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (user != null) if (intent != null) if (intent.getAction() != null)
                    if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == ZERO){
                        adapter.notifyDataSetChanged();
                    }
            }
        };
        registerReceiver(broadcastEveryMinute, new IntentFilter(Intent.ACTION_TIME_TICK));

        broadcastChangeDate = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
// следит за сменой даты и обновляет список в полноч или просто при переводе даты руками

                if (user != null) if (intent != null) if (intent.getAction() != null) {
                    checkDateFilter();
                }
            }
        };
        IntentFilter broadcastFilter = new IntentFilter();
        broadcastFilter.addAction(Intent.ACTION_DATE_CHANGED);
        broadcastFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver(broadcastChangeDate, broadcastFilter);

        getLoopAndNonify(this, new CallbackOnDataChangeRun().getCallbackOnDataChange());

    }

    private void checkDateFilter(){
        switch (filter.getPeriod()) {
            case FILTER_DATE_YESTERDAY:
                shiftOneDay(-1);
                break;
            case FILTER_DATE_TODAY:
                shiftOneDay(0);
                break;
            case FILTER_DATE_TOMORROW:
                shiftOneDay(1);
                break;
            case FILTER_DATE_WEEK: {
                Calendar week = Public.getSrokTodayBegin();
                week.set(Calendar.DAY_OF_WEEK, week.getFirstDayOfWeek());
                long t = week.getTimeInMillis();
                if (filter.getSrokBegin() != t) {
                    filter.setSrokBegin(t);
                    setting.setFilterSrokBegin(t);
                    week = Public.getSrokTodayEnd();
                    week.set(Calendar.DAY_OF_WEEK, week.getFirstDayOfWeek());
                    week.add(Calendar.DATE, 6);
                    t = week.getTimeInMillis();
                    filter.setSrokEnd(t);
                    setting.setFilterSrokEnd(t);
                }
                break;
            }
            case FILTER_DATE_MONTH: {
                Calendar month = Public.getSrokTodayBegin();
                month.set(month.get(Calendar.YEAR), month.get(Calendar.MONTH), 1);
                long t = month.getTimeInMillis();
                if (filter.getSrokBegin() != t){
                    filter.setSrokBegin(t);
                    setting.setFilterSrokBegin(t);
                    month = Public.getSrokTodayEnd();
                    month.set(month.get(Calendar.YEAR), month.get(Calendar.MONTH), 1);
                    month.add(Calendar.MONTH, 1);
                    month.add(Calendar.DATE, -1);
                    t = month.getTimeInMillis();
                    filter.setSrokEnd(t);
                    setting.setFilterSrokEnd(t);
                }
                break;
            }
            case FILTER_DATE_YEAR:{
                Calendar year = Public.getSrokTodayBegin();
                year.set(year.get(Calendar.YEAR), Calendar.JANUARY, 1);
                long t = year.getTimeInMillis();
                if (filter.getSrokBegin() != t){
                    filter.setSrokBegin(t);
                    setting.setFilterSrokBegin(t);
                    year = Public.getSrokTodayEnd();
                    year.set(year.get(Calendar.YEAR), Calendar.JANUARY, 1);
                    year.add(Calendar.YEAR, 1);
                    year.add(Calendar.DATE,-1 );
                    t = year.getTimeInMillis();
                    filter.setSrokEnd(t);
                    setting.setFilterSrokEnd(t);
                }
                break;
            }
            case FILTER_DATE_TODAY_MORE:{
                Calendar dayMore = Public.getSrokTodayBegin();
                long t = dayMore.getTimeInMillis();
                if (filter.getSrokBegin() != t){
                    filter.setSrokBegin(t);
                    setting.setFilterSrokBegin(t);
                    t = Long.MAX_VALUE;
                    filter.setSrokEnd(t);
                    setting.setFilterSrokEnd(t);
                }
                break;
            }
            case FILTER_DATE_YESTERDAY_MORE:{
                Calendar yesterday = Public.getSrokTodayEnd();
                yesterday.add(Calendar.DATE, -1);
                long t = yesterday.getTimeInMillis();
                if (filter.getSrokEnd() != t){
                    filter.setSrokEnd(t);
                    setting.setFilterSrokEnd(t);
                    t = Long.MIN_VALUE;
                    filter.setSrokBegin(t);
                    setting.setFilterSrokBegin(t);
                }
                break;
            }
            case FILTER_DATE_NOT:
                return;
            default:
                return;
        }

        filter.refresh();
        if (sortComparator != null && listTask != null) {
            Collections.sort(listTask,sortComparator);
            if (setting.isArrowListTask()) Collections.reverse(listTask);
        }
        adapter.notifyDataSetChanged();
        seeSizeList();
    }

    private void shiftOneDay(int shift){
        Calendar todayBegin = Public.getSrokTodayBegin();
        todayBegin.add(Calendar.DATE, shift);
        long t = todayBegin.getTimeInMillis();
        if (filter.getSrokBegin() != t){
            Calendar tmp = Public.getSrokTodayBegin();
            tmp.add(Calendar.DATE, shift);
            filter.setSrokBegin(tmp.getTimeInMillis());
            setting.setFilterSrokBegin(tmp.getTimeInMillis());
            tmp = Public.getSrokTodayEnd();
            tmp.add(Calendar.DATE, shift);
            filter.setSrokEnd(tmp.getTimeInMillis());
            setting.setFilterSrokEnd(tmp.getTimeInMillis());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        setting.setSettingToFile();

        if (broadcastEveryMinute != null)
            unregisterReceiver(broadcastEveryMinute);
        if (broadcastChangeDate != null)
            unregisterReceiver(broadcastChangeDate);

//        regAsBoss.stopConnect();
    }

    public static void getLoopAndNonify(final Context context, final CallbackOnDataChange callbackOnDataChange){

        // запускает "сервис" который следит за изменениями и информирут о доставке
        // тк создается свой loop, то работает в том числе и при не активной активности

        Handler handler = new Handler(Looper.getMainLooper());  // берем loop основного потока
        handler.post(new Runnable() {
            @Override
            public void run() {

                if (Public.childEventListener == null) {

                    FirebaseAuth mAuth;
                    final DatabaseReference dbRefTasks;

                    mAuth = FirebaseAuth.getInstance();
                    if (mAuth == null){
                        callbackOnDataChange.onCallbackBad(context);
                        return;
                    }

                    final FirebaseUser user = mAuth.getCurrentUser();
                    if (user == null){
                        callbackOnDataChange.onCallbackBad(context);
                        return;
                    }

                    dbRefTasks = FirebaseDatabase.getInstance().getReference(TASKS).child(user.getUid());
                    dbRefTasks.keepSynced(true);

                    Public.childEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            String key = dataSnapshot.getKey();
                            if (key == null){
                                callbackOnDataChange.onCallbackBad(context);
                                return;
                            }

                            BussinessTask bt = dataSnapshot.getValue(BussinessTask.class);
                            if (bt == null){
                                callbackOnDataChange.onCallbackBad(context);
                                return;
                            }

                            // отчет автору о доставке
                            String author = bt.getIdAuthor();
                            if (author != null) {
                                if (!author.equals(user.getUid()) && bt.getDateDelivery() == ZERO_L) {

                                    long timeNaw = System.currentTimeMillis();
                                    bt.setDateDelivery(timeNaw);
                                    bt.setDone(Status.id_done_no);
                                    dbRefTasks.child(key).setValue(bt);

                                    String title = context.getString(R.string.recive_new_task);
                                    String task = bt.getTask();
                                    String IdUserEditStatus = bt.getIdUserEditStatus();
                                    callbackOnDataChange.onCallback(context, key, title, task, false,
                                            IdUserEditStatus, author.equals(user.getUid()),
                                            false);
                                }

                            } else {
                                callbackOnDataChange.onCallbackBad(context);
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            String key = dataSnapshot.getKey();
                            if (key == null){
                                callbackOnDataChange.onCallbackBad(context);
                                return;
                            }

                            BussinessTask bt = dataSnapshot.getValue(BussinessTask.class);
                            if (bt == null){
                                callbackOnDataChange.onCallbackBad(context);
                                return;
                            }

                            // отчет автору о доставке
                            String author = bt.getIdAuthor();
                            if (author != null) {
                                    if (!author.equals(user.getUid()) && bt.getDateDelivery() == ZERO_L) {
                                    long timeNaw = System.currentTimeMillis();
                                    bt.setDateDelivery(timeNaw);
                                    bt.setDone(Status.id_done_no);
                                    dbRefTasks.child(key).setValue(bt);

                                    String title = context.getString(R.string.recive_edit_task);
                                    String task = bt.getTask();
                                    String IdUserEditStatus = bt.getIdUserEditStatus();
                                    callbackOnDataChange.onCallback(context, key, title, task, false,
                                            IdUserEditStatus, author.equals(user.getUid()), false);
                                }
                            } else {
                                callbackOnDataChange.onCallbackBad(context);
                            }


                            // отчет автору о изменении статуса of slave (подчиненным)
                            String slaveStatus = bt.getIdUserEditStatus();
                            long editLastEdit = bt.getDateLastEdit();
                            long editStatusDate = bt.getDateEditStatus();
                            String task = bt.getTask();
                            if (author != null && slaveStatus != null && task != null){
                                if (    author.equals(user.getUid())
                                        && !slaveStatus.equals(user.getUid())
                                        && editLastEdit == editStatusDate
                                ) {
                                    String title = context.getString(R.string.notif_status);
                                    title += " " + context.getString(new Status().getNameFromId(bt.getStatus())) ;
                                    String IdUserEditStatus = bt.getIdUserEditStatus();
                                    callbackOnDataChange.onCallback(context, key, title, task, true,
                                            IdUserEditStatus, author.equals(user.getUid()), false);
                                }
                            }
                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            String key = dataSnapshot.getKey();
                            if (key == null){
                                callbackOnDataChange.onCallbackBad(context);
                                return;
                            }

                            BussinessTask bt = dataSnapshot.getValue(BussinessTask.class);
                            if (bt == null){
                                callbackOnDataChange.onCallbackBad(context);
                                return;
                            }
                            String author = bt.getIdAuthor();
                            if (author != null) {
                                if (!author.equals(user.getUid())) {
                                    String title = context.getString(R.string.recive_del_task);
                                    String task = bt.getTask();
                                    String IdUserEditStatus = bt.getIdUserEditStatus();

                                    callbackOnDataChange.onCallback(context, key, title, task, false,
                                            IdUserEditStatus, author.equals(user.getUid()),
                                            true);

                                }
                            } else {
                                callbackOnDataChange.onCallbackBad(context);
                            }

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            callbackOnDataChange.onCallbackBad(context);
                        }
                    };

                    dbRefTasks.addChildEventListener(Public.childEventListener);
//                    dbRefTasks.removeEventListener(Public.childEventListener);
                }
            }
        });
    }

    private void setImgStatusOnHead() {
        int currentStatus = setting.getFilterStatusHead();
        switch (currentStatus){
            case FILTER_STATUS_OFF:
                imgStatus.setImageResource(R.drawable.ic_filter_off_white_36dp);
                break;
            case FILTER_STATUS_UNLOCK:
                imgStatus.setImageResource(R.drawable.ic_status_unlock_white_36dp);
                break;
            case FILTER_STATUS_LOCK:
                imgStatus.setImageResource(R.drawable.ic_status_lock_white_36dp);
                break;
            case FILTER_STATUS_GO:
                imgStatus.setImageResource(R.drawable.ic_status_go_white_36dp);
                break;
            case FILTER_STATUS_GALKA:
                imgStatus.setImageResource(R.drawable.ic_status_galka_white_36dp);
                break;
            case FILTER_STATUS_PUSTO:
                imgStatus.setImageResource(R.drawable.ic_status_pusto_white_36dp);
                break;
            case FILTER_STATUS_PAUSE:
                imgStatus.setImageResource(R.drawable.ic_status_pause_white_36dp);
                break;
            case FILTER_STATUS_CANCEL:
                imgStatus.setImageResource(R.drawable.ic_status_cancel_white_36dp);
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if (runFing){
            int bottom = (int) getResources().getDimension(R.dimen.recycler_bottom);
            baseLayoutParams.setMargins(ZERO, ZERO,ZERO , bottom);
            recyclerView.setLayoutParams(baseLayoutParams);
            editFind.setText(ZERO_S);
            editFind.setVisibility(View.INVISIBLE);
            runFing = false;
        } else {
            if (drawer.isDrawerOpen(GravityCompat.START)){
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
                finishAffinity ();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        toLog("resultCode= "+resultCode);
        if (resultCode == RESULT_OK) {
            toLog("requestCode= "+requestCode);
            switch (requestCode){
                case RC_SIGN_IN:
                {
                    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
                    Task<GoogleSignInAccount> myTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        GoogleSignInAccount account = myTask.getResult(ApiException.class);
                        if (isOnline(this) && account != null){
                            firebaseAuthWithGoogle(account);
                        } else {
                            toLog("on else");
                            showDialErrorReg();
                        }

                    } catch (ApiException e) {
                        // Google Sign In failed, update UI appropriately
                        toLog("on catch");
                        showDialErrorReg();
                    }
                    break;
                }


                case RESULT_ON_EDIT:
                {
//                    Public.toLog("RESULT_ON_EDIT "+data.getStringExtra(SLAVE_KEY));
                    break;
                }
                default:
                    toLog("on default");
                    showDialErrorReg();
                    break;

            }
        }  else {
            toLog("RESULT NOT OK");
            showDialErrorReg();
        }
    }

    private void showDialErrorReg(){
        String title = getString(R.string.error_reg_title);
        String massage = getString(R.string.error_reg_massage);
        AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
        builder.setTitle(title)
                .setMessage(massage)
                .setCancelable(false)
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        closeThis();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        LogAnalytic logAnalytic = new LogAnalytic(this, getString(R.string.notReg));
        logAnalytic.setLogErorr();
    }

    private void startCurruntUserOrGer() {

        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {

            boolean isInet = isOnline(this);    // есть ли интернет

            if (isInet) {
                RegDialog regDialog = new RegDialog(this, mGoogleSignInClient);
                regDialog.show();
            } else {
                showDialErrorReg();
            }

        } else {
            updateUI(user);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    saveUserName(user);
                                    updateUI(user);
                                }
                            }
                        }
                    });
    }

    void closeThis(){
        this.finishAffinity();
    }

    private void saveUserName(final FirebaseUser user) {

        dbRefUsers = FirebaseDatabase.getInstance().getReference(USERS);
        dbRefUsers.keepSynced(true);

// Запрашиваем и проверям есть ли запись с текущим gmail, если да то читаем данные,
// если нет, то формируем новую запись
        dbRefUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String m = ds.child("gmail").getValue(String.class);
                    if (m != null) if (m.equals(user.getEmail())){
                        // Записываем displayName в файл, этот параметр можно будет менять как отображение user
                        SharedPreferences sp = getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(NAME_USER_SELF, ds.child("displayName").getValue(String.class));
                        editor.putString(KEY_USER_SELF, ds.getKey());
                        User u = ds.getValue(User.class);
                        if (u != null){
                            editor.putLong(USER_SELF_DATE_BOSS, u.getDateRegAsBossTreal());
                            editor.putInt(USER_SELF_STAT_BOSS, u.getStatAsBoss());
                        }
                        editor.apply();
                        setRefUser();

                        return;
                    }
                }

                User newUser = new User(
                        user.getUid(),
                        user.getEmail(),
                        user.getDisplayName(),
                        user.getPhoneNumber(),
                        System.currentTimeMillis()
                );

                String key = dbRefUsers.push().getKey();    // получаем код заранее, а потом туда вставляем
                if (key == null) return;
                dbRefUsers.child(key).setValue(newUser);

                // Записываем displayName в файл, этот параметр можно будет менять как отображение user
                SharedPreferences sp = getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(NAME_USER_SELF, user.getDisplayName());
                editor.putString(KEY_USER_SELF, key);
                editor.apply();

                setRefUser();

                addHelpTasksToList(user.getUid());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addHelpTasksToList(String key) {
        // когда программа запускаетс первый раз этот метод добавляет 3 новых данания с описанием

        DatabaseReference dbRefTasks;
        dbRefTasks = FirebaseDatabase.getInstance().getReference(TASKS).child(key);
        long time = System.currentTimeMillis();
        long srok = time + 3600*1000L;
        String task = getString(R.string.addStartTask1);
        BussinessTask bt = new BussinessTask(task, time, srok, NOT_SLAVE_KEY);
        dbRefTasks.push().setValue(bt);

        task = getString(R.string.addStartTask2);
        bt = new BussinessTask(task, time, srok, NOT_SLAVE_KEY);
        dbRefTasks.push().setValue(bt);

        task = getString(R.string.addStartTask3);
        bt = new BussinessTask(task, time, srok, NOT_SLAVE_KEY);
        dbRefTasks.push().setValue(bt);

    }

    public void updateUI(FirebaseUser currentUser) {

        if (currentUser != null)
            startingSetting(currentUser);
        else
            Public.toLog("currentUser from updateUser == Null");
    }

    void createAdapter(){

// Формируем список Slave (подчиненных) для отобажения в navigationView
// добавляем "мне" и "не назначено"
        listSlave = new ArrayList<>();
        ItemSlaves forMy = new ItemSlaves(user.getUid(), getString(R.string.setForMy));
        forMy.setDisplayNameSlaveEdit(getString(R.string.setForMy));
        ItemSlaves notSlave = new ItemSlaves(NOT_SLAVE_KEY, getString(R.string.setNobody));
        notSlave.setDisplayNameSlaveEdit(getString(R.string.setNobody));
        listSlave.add(forMy);
        listSlave.add(notSlave);
        setDrawerMenu();            // Создает боковое меню

        Public.dbRefSlaves.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Slaves slave = dataSnapshot.getValue(Slaves.class);
                if (slave != null){
                    String key = dataSnapshot.getKey();
                    if (key == null) return;
                    ItemSlaves itemSlave = new ItemSlaves(key, slave.getDisplayNameSlave());
                    itemSlave.setDisplayNameSlaveEdit(slave.getDisplayNameSlaveEdit());
                    listSlave.add(itemSlave);
                    setDrawerMenu();            // Создает боковое меню
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                Slaves slave = dataSnapshot.getValue(Slaves.class);
                if (slave != null){
                    String key = dataSnapshot.getKey();
                    if (key == null) return;
                    for (int i=0; i<listSlave.size();i++){
                        String keyFind = listSlave.get(i).getKey();
                        if (keyFind == null) break;
                        if (key.equals(keyFind)){
                            ItemSlaves itemSlave = new ItemSlaves(key, slave.getDisplayNameSlave());
                            itemSlave.setDisplayNameSlaveEdit(slave.getDisplayNameSlaveEdit());
                            listSlave.set(i, itemSlave);
                            setDrawerMenu();            // Создает боковое меню
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Slaves slave = dataSnapshot.getValue(Slaves.class);
                if (slave != null){
                    String key = dataSnapshot.getKey();
                    if (key == null) return;
                    for (int i=0; i<listSlave.size();i++){
                        String keyFind = listSlave.get(i).getKey();
                        if (key.equals(keyFind)){
                            listSlave.remove(i);
                            break;
                        }
                    }
                    setDrawerMenu();            // Создает боковое меню
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        listTaskNotFilter = new ArrayList<>();
        listTask = new ArrayList<>();
        Public.adapter = new TaskAdapter(listTask);
        recyclerView.setAdapter(adapter);
        Public.nTasksForMy = ZERO;

        Public.dbRefTasks.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                BussinessTask bt = dataSnapshot.getValue(BussinessTask.class);
                if (bt != null){
                    String key = dataSnapshot.getKey();
//                    Public.toLog("onChildAdded, key = "+key);
                    // иногда запись дублируется (возможно из-за функции на сервере)
                    // этот цикл проверяет есть ли запись с таким ключом в списке и если есть выходит
                    if (key == null) return;
                    for (ItemBussinessTask i: listTaskNotFilter){
                        if (key.equals(i.getKey())) return;
                    }

                    // добавить запись в список без фильтра и пропуситив через фильтр добавить в текущий список
                    ItemBussinessTask ibt = new ItemBussinessTask(key, bt);
                    listTaskNotFilter.add(ibt);
//                    Public.toLog("Uid= "+user.getUid() + " ibt.key "+ ibt.getKey() + " IdAuthor = " + ibt.getIdAuthor());
                    if (ibt.getDateRead() == ZERO_L && !ibt.getIdAuthor().equals(user.getUid()))
                        Public.nTasksForMy++;       // ститаем количество назначеных и не прочитаных
                    filter.addToList(ibt);
                    sortListTask();
                    adapter.notifyDataSetChanged();
                }
                seeSizeList();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                BussinessTask bt = dataSnapshot.getValue(BussinessTask.class);
                if (bt != null){
                    String key = dataSnapshot.getKey();
                    if (key == null) return;
                    for (int i = 0; i < listTaskNotFilter.size(); i++) {
                        String keyFind = listTaskNotFilter.get(i).getKey();
                        if (key.equals(keyFind)) {
                            ItemBussinessTask ibt = new ItemBussinessTask(key, bt);
                            listTaskNotFilter.set(i, ibt);
                            filter.setToList(ibt);
                            sortListTask();
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
                seeSizeList();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                BussinessTask bt = dataSnapshot.getValue(BussinessTask.class);
                if (bt != null){
                    String key = dataSnapshot.getKey();
                    if (key == null) return;
                    for (int i=0; i< listTaskNotFilter.size();i++){
                        String keyFind = listTaskNotFilter.get(i).getKey();
                        if (key.equals(keyFind)){
                            listTaskNotFilter.remove(i);
                            filter.removeFromList(key);
                            sortListTask();
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
                seeSizeList();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        // Разово запрашиваем все записи и когда они обновляются, то можно жать на кнопки и отключить статус бар
        // вне зависимости от резутьтата, даже если сбой иначе повиснет все ....
        Public.dbRefTasks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setClickableMainView(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                setClickableMainView(true);
            }
        });

    }

    void seeSizeList(){
        if (listTask.size() > ZERO){
            txtNotData.setVisibility(View.INVISIBLE);
        } else {
            txtNotData.setVisibility(View.VISIBLE);
        }
    }

    void sortListTask(){
        // сортировка спсика
        sortComparator = getComparatorForSortListTask(setting.getSortLiesTask());
        if (sortComparator != null) {
            Collections.sort(listTask, sortComparator);
            if (setting.isArrowListTask()) Collections.reverse(listTask);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

// Инициализация переменных
        SharedPreferences sp = getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
        int idItem = item.getItemId();

// Обработка выбора
        switch (idItem){
            case R.id.reset_all_filter:
            {
                clearIconSlave();

                imgStatus.setImageResource(ic_filter_off_white_36dp);
                filter.setStatusHead(FILTER_STATUS_OFF);
                setting.setFilterStatusHead(FILTER_STATUS_OFF);
                filterStatus = new Status().getNameStatusFilter(getApplicationContext(), FILTER_STATUS_OFF);

                calendarSrokBegin = Public.getSrokTodayBegin();
                calendarSrokEnd = Public.getSrokTodayEnd();
                calendarSrokBegin.setTimeInMillis(Long.MIN_VALUE);
                calendarSrokEnd.setTimeInMillis(Long.MAX_VALUE);
                setFilterSrokString();
                setting.setFilterSrokBegin(calendarSrokBegin.getTimeInMillis());
                setting.setFilterSrokEnd(calendarSrokEnd.getTimeInMillis());
                setting.setFilterSrokString(filterSrok);
                setFilterString(filterStatus, filterSrok, filterSlave);
                filter.setSrokBegin(setting.getFilterSrokBegin());
                filter.setSrokEnd(setting.getFilterSrokEnd());

                filter.setSlave(FILTER_SLAVE_ALL);
                setting.setFilterSlave(FILTER_SLAVE_ALL);
                filterSlave = getString(R.string.allSlave);
                setting.setFilterSlaveString(filterSlave);

                filter.refresh();
                setFilterString(filterStatus, filterSrok, filterSlave);
                sortListTask();
                adapter.notifyDataSetChanged();
                seeSizeList();

                break;
            }
            case R.id.unlock_today:
            {
                clearIconSlave();

                imgStatus.setImageResource(ic_status_unlock_white_36dp);
                filter.setStatusHead(FILTER_STATUS_UNLOCK);
                setting.setFilterStatusHead(FILTER_STATUS_UNLOCK);
                filterStatus = new Status().getNameStatusFilter(getApplicationContext(), FILTER_STATUS_UNLOCK);

                calendarSrokBegin = Public.getSrokTodayBegin();
                calendarSrokEnd = Public.getSrokTodayEnd();
                filterSrok = getString(R.string.today);
                setting.setFilterSrokBegin(calendarSrokBegin.getTimeInMillis());
                setting.setFilterSrokEnd(calendarSrokEnd.getTimeInMillis());
                setting.setFilterSrokString(filterSrok);
                setFilterString(filterStatus, filterSrok, filterSlave);
                filter.setSrokBegin(setting.getFilterSrokBegin());
                filter.setSrokEnd(setting.getFilterSrokEnd());

                filter.setSlave(FILTER_SLAVE_ALL);
                setting.setFilterSlave(FILTER_SLAVE_ALL);
                filterSlave = getString(R.string.allSlave);
                setting.setFilterSlaveString(filterSlave);

                filter.refresh();
                setFilterString(filterStatus, filterSrok, filterSlave);
                sortListTask();
                adapter.notifyDataSetChanged();
                seeSizeList();

                break;
            }
            case R.id.item_send_my_key: {
                Intent sendKeyIntent = new Intent();
                sendKeyIntent.setAction(Intent.ACTION_SEND);
                String key = sp.getString(KEY_USER_SELF, ZERO_S);
                sendKeyIntent.putExtra(Intent.EXTRA_TEXT, key);
                sendKeyIntent.setType("text/plain");
                startActivity(sendKeyIntent);
                break;
            }
            case R.id.item_send_my_gmail: {
                Intent sendGmailIntent = new Intent();
                sendGmailIntent.setAction(Intent.ACTION_SEND);
                sendGmailIntent.putExtra(Intent.EXTRA_TEXT, user.getEmail());
                sendGmailIntent.setType("text/plain");
                startActivity(sendGmailIntent);
                break;
            }
            case R.id.item_send_url_user_code:{
                Intent sendUrlUserCodeIntent = new Intent();
                String key = sp.getString(KEY_USER_SELF, ZERO_S);
                sendUrlUserCodeIntent.setAction(Intent.ACTION_SEND);
                sendUrlUserCodeIntent.putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.http) + getString(R.string.url_app) +"/" + key);
                sendUrlUserCodeIntent.setType("text/plain");
                startActivity(sendUrlUserCodeIntent);
                break;
            }
            case R.id.item_send_url: {
                Intent sendUrlIntent = new Intent();
                sendUrlIntent.setAction(Intent.ACTION_SEND);
                sendUrlIntent.putExtra(Intent.EXTRA_TEXT,
                        "https://play.google.com/store/apps/details?id=com.babyartsoft.bussinestasks1");
                sendUrlIntent.setType("text/plain");
                startActivity(sendUrlIntent);
                break;
            }
            case R.id.item_activ_device:
                Intent intentDevice = new Intent(this, DeviceActivity.class);
                startActivity(intentDevice);
                break;
            case R.id.item_about_ver:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.item_about_user:
                Intent aboutUserIntent = new Intent(this, AboutUserActivity.class);
                startActivity(aboutUserIntent);
                break;

            default: {
                if (idItem == MENU_SLAVE_ADD) {
                    regAsBoss.chooseDoAtStatBoss();
                    break;
                }
                if (idItem == MENU_SLAVE_EDIT) {
                    regAsBoss.chooseDoAtStatBoss();
                    break;
                }
                if (idItem == MENU_BOSS_EDIT){
                    Intent intent = new Intent(getApplicationContext(), EditBossActivity.class);
                    startActivity(intent);
                    break;
                }
                clearIconSlave();
                if (idItem == MENU_SLAVE_ALL) {
                    filter.setSlave(FILTER_SLAVE_ALL);
                    setting.setFilterSlave(FILTER_SLAVE_ALL);
                    filterSlave = getString(R.string.allSlave);
                    setting.setFilterSlaveString(filterSlave);
                } else {
                    for (ItemSlaves i : listSlave) {
                        if (i.getIdItemMenu() == idItem) {
                            item.setIcon(R.drawable.ic_cheese_item_black_24dp);
                            filter.setSlave(i.getKey());
                            setting.setFilterSlave(i.getKey());
                            filterSlave = i.getDisplayNameSlaveEdit();
                            setting.setFilterSlaveString(filterSlave);
                            break;
                        }
                    }
                }
                filter.refresh();
                setFilterString(filterStatus, filterSrok, filterSlave);
                sortListTask();
                adapter.notifyDataSetChanged();
                seeSizeList();
            }
        }

// Закрыть шторку после выбора
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    // Затираем иконки у всех Slave
    private void clearIconSlave(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu navMenu = navigationView.getMenu();
        MenuItem itemSlave = navMenu.getItem(0);
        SubMenu subMenuSlave = itemSlave.getSubMenu();
        for (int i=1; i<=listSlave.size();i++){
            subMenuSlave.getItem(i).setIcon(R.drawable.hide_icon_menu);
        }
    }

    public boolean isOnline(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
}
