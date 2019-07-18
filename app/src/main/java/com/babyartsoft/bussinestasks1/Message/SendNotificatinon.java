package com.babyartsoft.bussinestasks1.Message;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.babyartsoft.bussinestasks1.EditStatusActivity;
import com.babyartsoft.bussinestasks1.EditTaskActivity;
import com.babyartsoft.bussinestasks1.Interface.Constant;
import com.babyartsoft.bussinestasks1.ListActivity;
import com.babyartsoft.bussinestasks1.R;

/**
 * Created by User on 18.04.2018.
 */

public class SendNotificatinon implements Constant {

    private NotificationCompat.Builder nc;
    private NotificationCompat.Builder groupNotify;
    private String msgroup;
    private String title, task, idTask;
    private Context context;
    private boolean IamAuthor;
    private boolean whatGroup;
    private NotificationManagerCompat notificationManagerCompat;
    private NotificationManager notificationManager;
    private int idNotify;
    private int idGroup;
    private String nameGroup;
    private SharedPreferences sp;
    private final int startIdNofity = 100;
    private final int idGroupForMy = 99;
    private final int idGroupOrder = 98;
    boolean deleteNotify;
//    private String nameSlave;

    public SendNotificatinon(Context context, String idTask, String title, String task,
                             boolean whatGroup, String IdUserEditStatus, boolean IamAuthor,
                             boolean deleteNotify
    ) {
        this.context = context;
        this.title = title;
        this.task = task;
        this.idTask = idTask;
        this.IamAuthor = IamAuthor;
        this.whatGroup = whatGroup;
        this.deleteNotify = deleteNotify;

        sp = context.getSharedPreferences(FILE_SETTING, Context.MODE_PRIVATE);
        idNotify = sp.getInt(ID_NOTIFY, startIdNofity);

        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) return;
        notificationManagerCompat = NotificationManagerCompat.from(context);

        this.msgroup = context.getPackageName() + ".massage.";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){    // с 24 есть группы, до этого не было
            setChannel();
            createGroup(whatGroup);
            sendNofify_N();
        } else {
            sendNofify_Old();
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(ID_NOTIFY, idNotify);
        editor.apply();

    }

    private void setChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { // для 8 андроида и выше
            NotificationChannel channel = new NotificationChannel(
                    context.getString(R.string.channel_id),
                    context.getString(R.string.name_channel_general),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createGroup(boolean whatGroup) {
        groupNotify = new NotificationCompat.Builder(context, context.getString(R.string.channel_id));
        groupNotify.setSmallIcon(R.drawable.ic_boss_fill_circle);

        if (whatGroup){
            nameGroup = context.getString(R.string.notif_order);
            msgroup += NOTIFY_ORDER;
            idGroup = idGroupOrder;
        } else {
            nameGroup = context.getString(R.string.notif_set_for_my);
            msgroup += NOTIFY_FOR_MY;
            idGroup = idGroupForMy;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            groupNotify.setSubText(nameGroup);
        } else {
            groupNotify.setContentInfo(nameGroup);
        }
        groupNotify.setGroup(msgroup);
        groupNotify.setGroupSummary(true);
        groupNotify.setTicker(ZERO_S);
        groupNotify.setAutoCancel(true);
    }

    private void sendNofify_N() {

        Intent listIntent = new Intent(context, ListActivity.class);
        Intent thisIntent;
        if (IamAuthor){
            thisIntent = new Intent(context, EditTaskActivity.class);
//            toLog("I am Author");
        }
            else{
            thisIntent = new Intent(context, EditStatusActivity.class);
        }
        thisIntent.putExtra(FROM_NOTIFY, FROM_NOTIFY_YES);
        thisIntent.putExtra(FROM_NOTIFY_ID_TASK, idTask);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntent(listIntent);
        taskStackBuilder.addNextIntent(thisIntent);

        nc = new NotificationCompat.Builder(context, context.getString(R.string.channel_id));
        nc.setSmallIcon(R.drawable.ic_boss_fill_circle);
        nc.setContentTitle(title);
        nc.setContentText(task);
        nc.setGroup(msgroup);
        nc.setTicker(idTask);
        nc.setSubText(nameGroup);
        nc.setStyle(new NotificationCompat.BigTextStyle().bigText(task));
        nc.setDefaults(Notification.DEFAULT_ALL);
        nc.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_massage_large_round));

        notificationManagerCompat.notify(idGroup, groupNotify.build());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarNotification[] statusBarNotifications;
            statusBarNotifications = notificationManager.getActiveNotifications();
            boolean isToo = false;
            for (StatusBarNotification sbn: statusBarNotifications) {
                if (sbn.getNotification().tickerText.equals(idTask)){
                    if (deleteNotify){
                        PendingIntent listPendingIntent = PendingIntent.getActivity(context, sbn.getId(), listIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        nc.setContentIntent(listPendingIntent);
                        nc.setAutoCancel(true);
                    } else {
                        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(sbn.getId(), PendingIntent.FLAG_UPDATE_CURRENT);
                        nc.setContentIntent(pendingIntent);
                    }
                    notificationManagerCompat.notify(sbn.getId(), nc.build());
                    isToo = true;
                    break;
                }
            }
            if (!isToo){
                idNotify++;
                if (idNotify == Integer.MAX_VALUE) idNotify = startIdNofity;
                if (deleteNotify){
                    PendingIntent listPendingIntent = PendingIntent.getActivity(context, idNotify, listIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    nc.setContentIntent(listPendingIntent);
                    nc.setAutoCancel(true);
                } else {
                    PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(idNotify, PendingIntent.FLAG_UPDATE_CURRENT);
                    nc.setContentIntent(pendingIntent);
                }
                notificationManagerCompat.notify(idNotify, nc.build());
            }
        }

    }

    private void sendNofify_Old() {

        Intent listIntent = new Intent(context, ListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, ZERO, listIntent, ZERO);

        nc = new NotificationCompat.Builder(context, context.getString(R.string.channel_id));
        nc.setSmallIcon(R.drawable.ic_boss_fill_circle);
        nc.setContentTitle(title);
        nc.setContentText(task);
        nc.setContentIntent(pendingIntent);
        nc.setAutoCancel(true);
        String info;
        if (whatGroup){
            info = context.getString(R.string.notif_order);
        } else {
            info = context.getString(R.string.notif_set_for_my);
        }
        nc.setContentInfo(info);
        nc.setStyle(new NotificationCompat.BigTextStyle().bigText(task));
        nc.setDefaults(Notification.DEFAULT_ALL);
        nc.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_massage_large_round));


    }
}
