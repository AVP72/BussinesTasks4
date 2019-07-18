package com.babyartsoft.bussinestasks1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.babyartsoft.bussinestasks1.Interface.Constant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class Public implements Constant {

    // region Объявление переменных

    public static FirebaseAuth mAuth;
    public static DatabaseReference dbRefTasks;
    public static DatabaseReference dbRefUsers;
    public static DatabaseReference dbRefUser;
    public static DatabaseReference dbRefSlaves;
    public static DatabaseReference dbRefBoss;
    public static FirebaseUser user;

    public static FirebaseRecyclerAdapter adapterSlave;
    public static FirebaseRecyclerAdapter adapterBoss;

    public static ArrayList<ItemBussinessTask> listTask;
    public static ArrayList<ItemBussinessTask> listTaskNotFilter;
    public static ArrayList<ItemSlaves> listSlave;          // хранится список slave
    public static RecyclerView.Adapter adapter;

    public static ChildEventListener childEventListener;
    public static RegAsBoss regAsBoss;


    public static int nTasksForMy = ZERO;
    // endregion

    public static String idTask;
    public static String idAuthor;
    public static String abTask;


    public static void toLog(String s){
        Log.d("avp", s);
    }
    public static void toLog(long s){
        Log.d("avp", ""+s);
    }

    // region Функции для работы с датой
    public static String setTxtDate(Calendar d){

/*
        String s;
        final SimpleDateFormat dataFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        s = dataFormat.format(d.getTime());
        return s;
*/
        String format;
        String country = Locale.getDefault().getCountry();
        switch (country){
            case "US":
                format = "M/d/yy";
                break;
            case "RU":
                format = "dd.MM.yy";
                break;
            case "GB":
            case "AU":
            case "IN":
                format = "dd/MM/yy";
                break;
            default:
                format = "dd-MM-yy";
        }
        SimpleDateFormat dataFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dataFormat.format(d.getTime());
    }
    public static String setTxtDate(long date){
        Calendar d = Calendar.getInstance();
        d.setTimeInMillis(date);
        return setTxtDate(d);
    }

    public static String setTxtDateYYYY(Calendar d){
        String s;
        final SimpleDateFormat dataFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        s = dataFormat.format(d.getTime());
        return s;
    }
    public static String setTxtTime(Calendar d){
        String s;
        final SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        s = dataFormat.format(d.getTime());
        return s;
    }
    public static String setTxtTime(long time){
        Calendar d = Calendar.getInstance();
        d.setTimeInMillis(time);
        return setTxtTime(d);
    }
    public static String setTxtDateTime(Calendar d){
        String s;
        final SimpleDateFormat dataFormat = new SimpleDateFormat("dd.MM.yy в HH:mm", Locale.getDefault());
        s = dataFormat.format(d.getTime());
        return s;
    }
    public static String setTxtDateTime(long time){
        Calendar d = Calendar.getInstance();
        d.setTimeInMillis(time);
        return setTxtDateTime(d);
    }
    public static String setTxtDateTimeSecond(Calendar d){
        String s;
        final SimpleDateFormat dataFormat = new SimpleDateFormat("dd.MM.yy в HH:mm:ss", Locale.getDefault());
        s = dataFormat.format(d.getTime());
        return s;
    }
    public static String setTxtDateTimeSecond(long t){
        Calendar d = Calendar.getInstance();
        d.setTimeInMillis(t);
        return setTxtDateTimeSecond(d);
    }
    // endregion

    public static void hideSoftKeyboard(View v, Context context){    // Спрятать софтовую клавиатуру
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static String installTaskForSortFromTask(String task){
        // Обрезаем из задания 10 символов и делаем их прописныи для сортировки в дальнейшем поле: taskForSort
        String taskForSort;
        int maxN = 10;
        int n = task.length();
        int nDel;
        if (n < maxN) nDel = n;
        else nDel = maxN;
        taskForSort = task.substring(ZERO, nDel);
        taskForSort = taskForSort.toLowerCase();
        return taskForSort;
    }

    public static Calendar getSrokTodayBegin(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, ZERO);
        calendar.set(Calendar.SECOND, ZERO);
        calendar.set(Calendar.MINUTE, ZERO);
        calendar.set(Calendar.HOUR_OF_DAY, ZERO);
        return calendar;
    }

    public static Calendar getSrokTodayEnd(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        return calendar;
    }

}
