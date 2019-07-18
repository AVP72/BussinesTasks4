package com.babyartsoft.bussinestasks1;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import static com.babyartsoft.bussinestasks1.Public.toLog;

public class LogAnalytic {

    private FirebaseAnalytics firebaseAnalytics;
    Bundle bundle;
    private String uid;

    public LogAnalytic(Context context, String uid) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, uid);

        this.uid = uid;
    }

    void setLogReg(){
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "reg_on_enter");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "reg_on_enter");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

//        toLog("reg_on_enter " + uid);
    }

    void setLogNotReg(){
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "not_reg_on_enter");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "not_reg_on_enter");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

//        toLog("not_reg_on_enter " + uid);
    }

    public void setLogSetAsBoss(){
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "set_as_boss");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "set_as_boss");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

//        toLog("set_as_boss " + uid );
    }

    public void setLogCancelAsBoss(){
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "set_cancel_as_boss");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "set_cancel_as_boss");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

//        toLog("set_cancel_as_boss " + uid );
    }

    void setLogErorr(){
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "error");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "error");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

//        toLog("error " + uid );
    }

}
