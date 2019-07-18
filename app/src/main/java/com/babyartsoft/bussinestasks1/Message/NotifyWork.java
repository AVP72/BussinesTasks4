package com.babyartsoft.bussinestasks1.Message;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.service.notification.StatusBarNotification;

import com.babyartsoft.bussinestasks1.Interface.Constant;

public class NotifyWork implements Constant {

    private Context context;
    private String keyPosition;
    public NotifyWork(Context context, String keyPosition){
        this.context = context;
        this.keyPosition = keyPosition;
    }

    public void delNotify(){
        // стирает оповещение, если оно еще болтается

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) return;

        String thisGroup = ZERO_S;
        // Находим группу оповещения по ключу, что записан в ticker
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarNotification[] statusBarNotifications;
            statusBarNotifications = notificationManager.getActiveNotifications();

            // Находим в какой группе выбранное оповещение
            for (StatusBarNotification sbn: statusBarNotifications) {
                if (sbn.getNotification().tickerText.equals(keyPosition)){
                    thisGroup = sbn.getGroupKey();
                    break;
                }
            }

            // считаем сколько в ней элементов
            int i = ZERO;   // будем считать количество оповещений в баре
            for (StatusBarNotification sbn: statusBarNotifications) {
                String gr = sbn.getGroupKey();
                if (gr.equals(thisGroup)){
                    i++;
                }
            }

            // Если оповещений в группе 2, то это текущее и группа - удаляем оба
            if (i != ZERO) {
                if (i <= 2) {
                    statusBarNotifications = notificationManager.getActiveNotifications();
                    for (StatusBarNotification sbn : statusBarNotifications) {
                        String gr = sbn.getGroupKey();
                        if (gr.equals(thisGroup)) {
                            notificationManager.cancel(sbn.getId());
                        }
                    }
                } else {    // Иначе удаляем только оповещение
                    for (StatusBarNotification sbn : statusBarNotifications) {
                        if (sbn.getNotification().tickerText.equals(keyPosition)) {
                            notificationManager.cancel(sbn.getId());
                            break;
                        }
                    }
                }
            }
        }
    }

}
