package com.babyartsoft.bussinestasks1.Interface;

import android.content.Context;

public interface CallbackOnDataChange {

    void onCallback(Context context, String idTask, String title, String task, boolean whatGroup,
                    String IdUserEditStatus, boolean IamAuthor, boolean deleteNotify);

//    void onCallbackDelete(Context context, String key);

    void onCallbackBad(Context context);
}
