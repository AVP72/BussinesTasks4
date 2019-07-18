package com.babyartsoft.bussinestasks1.Message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.babyartsoft.bussinestasks1.ListActivity;
import com.babyartsoft.bussinestasks1.Public;

// Когда меняется состояние интеренет, то запускается этот метод, даже при незапущеном приложении

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
//            ListActivity.getLoopAndNonify(context, Public.callbackOnDataChange);
            ListActivity.getLoopAndNonify(context, new CallbackOnDataChangeRun().getCallbackOnDataChange());
        }
    }
}
