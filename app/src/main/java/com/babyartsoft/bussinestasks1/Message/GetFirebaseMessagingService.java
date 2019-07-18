package com.babyartsoft.bussinestasks1.Message;

import android.content.Context;
import android.content.SharedPreferences;

import com.babyartsoft.bussinestasks1.Interface.Constant;
import com.babyartsoft.bussinestasks1.ListActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static com.babyartsoft.bussinestasks1.Public.abTask;
import static com.babyartsoft.bussinestasks1.Public.idAuthor;
import static com.babyartsoft.bussinestasks1.Public.idTask;
import static com.babyartsoft.bussinestasks1.Public.toLog;

/**
 * Created by User on 08.05.2018.
 */

public class GetFirebaseMessagingService extends FirebaseMessagingService implements Constant {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String, String> ms;
        ms = remoteMessage.getData();

        String sIdTask = "id_task";
        String sIdAuthor = "id_author";
        String sAbTask = "ab_task";

        idTask = ms.get(sIdTask);
        idAuthor = ms.get(sIdAuthor);
        abTask = ms.get(sAbTask);

//        toLog("from onMessageReceived "+ms.get(sIdTask) + " " + ms.get(sIdAuthor)+ " " + ms.get(sAbTask));

        if (idTask == null || idAuthor == null || abTask == null ) return;


        Context context = this;
//        ListActivity.getLoopAndNonify(context, Public.callbackOnDataChange);
        ListActivity.getLoopAndNonify(context, new CallbackOnDataChangeRun().getCallbackOnDataChange());

    }

    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        toLog("New Token = " + refreshedToken);

        SharedPreferences sp = getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(ID_TOKEN_THIS_DEVICE, refreshedToken);
        editor.putBoolean(IT_IS_NEW_TOKEN, true);
        editor.apply();
    }
}
