package com.babyartsoft.bussinestasks1.Message;

import android.content.Context;

import com.babyartsoft.bussinestasks1.Interface.CallbackOnDataChange;

public class CallbackOnDataChangeRun {

    private CallbackOnDataChange callbackOnDataChange;

    public CallbackOnDataChangeRun() {

        callbackOnDataChange = new CallbackOnDataChange() {
            @Override
            public void onCallback(Context context, String idTask, String title, String task,
                                   boolean whatGroup, String IdUserEditStatus, boolean IamAuthor,
                                   boolean deleteNotify
            ) {
                new SendNotificatinon(context, idTask, title, task, whatGroup, IdUserEditStatus, IamAuthor, deleteNotify);
            }

            @Override
            public void onCallbackBad(Context context) {

            }
        };
    }

    public CallbackOnDataChange getCallbackOnDataChange() {
        return callbackOnDataChange;
    }
}
