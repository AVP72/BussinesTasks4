package com.babyartsoft.bussinestasks1.Message;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.babyartsoft.bussinestasks1.ListActivity;
import com.babyartsoft.bussinestasks1.Public;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;

import static com.babyartsoft.bussinestasks1.Interface.Constant.FILE_SETTING;
import static com.babyartsoft.bussinestasks1.Interface.Constant.TASKS;

/**
 * Created by User on 10.05.2018.
 */

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
//        Public.toLog("==== Получил ...WakefulBroadcastReceiver");

/*
        SharedPreferences sp = context.getSharedPreferences("tmp", Context.MODE_PRIVATE);
        int k = sp.getInt("count", 0);
        k++;
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("count", k);
        editor.apply();
*/


//        if (intent.getExtras() == null) return;

//        ListActivity.getLoopAndNonify(context, Public.callbackOnDataChange);


    }
}
