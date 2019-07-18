package com.babyartsoft.bussinestasks1.Message;

import android.content.Context;
import android.content.SharedPreferences;

import com.babyartsoft.bussinestasks1.Interface.Constant;
import com.babyartsoft.bussinestasks1.Public;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements Constant {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

/*
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Public.toLog("Токен обновленный = "+refreshedToken);

        SharedPreferences sp = getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
        String key = sp.getString(ID_TOKEN_THIS_DEVICE_KEY, ZERO_S);
        String idUser = sp.getString(KEY_USER_SELF, ZERO_S);
        SharedPreferences.Editor editor = sp.edit();
        // Бывает токен обновляется когда пользователь уже заренистрирован
        // например после долгого отсутсвия
        // в этом случае просто перезаписываем токен по тому ключу, что был получен ранее
*/
/*
        if (!key.equals(ZERO_S)){
            Public.dbRefMassage.child(idUser).child(key).child("id").setValue(refreshedToken);
        }
*/
/*

        editor.putString(ID_TOKEN_THIS_DEVICE, refreshedToken);
        editor.apply();
*/
    }
}
