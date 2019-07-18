package com.babyartsoft.bussinestasks1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.babyartsoft.bussinestasks1.Dialog.DialError;
import com.babyartsoft.bussinestasks1.Dialog.DialogNotConnact;
import com.babyartsoft.bussinestasks1.Dialog.DialogNotPodpiska;
import com.babyartsoft.bussinestasks1.Dialog.DialogStartTreal;
import com.babyartsoft.bussinestasks1.Interface.Constant;

import java.util.Calendar;
import java.util.List;

import static com.babyartsoft.bussinestasks1.Public.dbRefUser;

public class RegAsBoss implements Constant {

    public final static int statNoBoss =    0;  //  Не босс
    public final static int statTreal =     1;  //  Босс на пробном периоде 3 мес
    public final static int statEndTreal =  2;  //  Босс, пробный период закончился, начался пробный по подписке 15 дней
    public final static int statIsPay =     5;  //  Босс, оплачено
    public final static int statNotPay =    6;  //  Босс, неоплачено

    private Context context;
    private Activity activity;
//    private SharedPreferences sp;
    private BillingClient billingClient;
    private boolean isConnection;

    RegAsBoss(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    void checkStatBoss(final Context context) {
        // проверяет активна ли еще подписка
        SharedPreferences sp = context.getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
        int stat = sp.getInt(USER_SELF_STAT_BOSS, statNoBoss);
        if (stat == statIsPay) {
            final BillingClient billingCheck;
            billingCheck = BillingClient.newBuilder(context).setListener(new PurchasesUpdatedListener() {
                @Override
                public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

                }
            }).build();

            billingCheck.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(int billingCode) {
                    if (billingCheck.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS) == BillingClient.BillingResponse.OK) {
                        if (billingCode == BillingClient.BillingResponse.OK) {

                            Purchase.PurchasesResult purchasesResult = billingCheck.queryPurchases(BillingClient.SkuType.SUBS);
                            List<Purchase> pList = purchasesResult.getPurchasesList();
                            String tokenServer = ZERO_S;
                            for (Purchase purchase : pList) {
                                tokenServer = purchase.getPurchaseToken();
                            }
                            SharedPreferences sp = context.getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
                            String tokenHere = sp.getString(USER_SELF_PurchaseToken, ZERO_S);

                            if (!tokenServer.equals(tokenHere)){
                                setStatNotPay();
                            }
                        }
                    }
                }

                @Override
                public void onBillingServiceDisconnected() {

                }
            });
        } else if (stat == statTreal){
            lookDateTreal(true);
        }
    }

    void chooseDoAtStatBoss(){
        if (dbRefUser == null){
            userNoRegToast(context);
            return;
        }

        SharedPreferences sp = context.getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
        int stat = sp.getInt(USER_SELF_STAT_BOSS, statNoBoss);
        switch (stat){
            case statNoBoss:
                showDialogStartTreal();
                break;
            case statTreal:
                lookDateTreal(false);
                break;
            case statEndTreal:
                billingGo();
                break;
            case statNotPay:
                billingGo();
                break;
            case statIsPay:
                startAddActivity();
                break;
        }
    }

    private void userNoRegToast(Context context){
        Toast.makeText(context, R.string.userNotReg, Toast.LENGTH_LONG).show();
    }

    private void lookDateTreal(boolean runOnStart) {
        SharedPreferences sp = context.getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
        long trealTimeStart = sp.getLong(USER_SELF_DATE_BOSS, ZERO_L);
        Calendar dateTrealEnd = Calendar.getInstance();
        dateTrealEnd.setTimeInMillis(trealTimeStart);
//        dateTrealEnd.add(Calendar.MINUTE, 3);
        dateTrealEnd.add(Calendar.MONTH, 3);
        dateTrealEnd.add(Calendar.DATE, 1);
        long trealTimeEnd = dateTrealEnd.getTimeInMillis();
        long currentTime = System.currentTimeMillis();
        if (currentTime <= trealTimeEnd){
            if (!runOnStart) startAddActivity();
        } else {
            setStatEndTreal();
            billingGo();
        }

    }

    private void showDialogStartTreal(){
        Intent intent = new Intent(context, DialogStartTreal.class);
        context.startActivity(intent);
    }

    private void startAddActivity(){
        Intent intent = new Intent(context, AddSlaveActivity.class);
        activity.startActivityForResult(intent, RESULT_ON_EDIT);
    }

    private void setStatEndTreal(){
        dbRefUser.child(User.sStatAsBoss).setValue(statEndTreal);
    }

    private void setStatNotPay(){
        dbRefUser.child(User.sStatAsBoss).setValue(statNotPay);
    }

    private void setStatIsPay(){
        dbRefUser.child(User.sStatAsBoss).setValue(statIsPay);
    }

    private void setPurchaseToken(String token) {
        dbRefUser.child(User.sPurchaseToken).setValue(token);
    }

    private void billingGo(){
        billingBuild();
        if (billingClient != null) billingConnect();
    }

    private void billingBuild(){
        if (!isConnection) {
            billingClient = BillingClient.newBuilder(context)
                    .setListener(new PurchasesUpdatedListener() {
                        @Override
                        public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
                            if (responseCode == BillingClient.BillingResponse.OK) {
                                if (purchases != null) {
                                    for (Purchase purchase : purchases) {
                                        if (purchase != null) {
                                            setStatIsPay();
                                            String token = purchase.getPurchaseToken();
                                            setPurchaseToken(token);
                                            startAddActivity();
                                        } else{
                                            showDialError();
                                        }
                                    }
                                } else{
                                    showDialogNotConnact();
                                }
                            } else if (responseCode == BillingClient.BillingResponse.SERVICE_UNAVAILABLE ){
                                showDialogNotConnact();
                            } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
                                showDialogNotConnact();
                            }
                        }
                    }).build();

        }
    }

    private void billingConnect() {

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int billingCode) {

                if (billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS) == BillingClient.BillingResponse.OK) {
                    if (billingCode == BillingClient.BillingResponse.OK) {

                        isConnection = true;

                        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                .setSku("business_tasks")
                                .setType(BillingClient.SkuType.SUBS)
                                .build();
                        billingClient.launchBillingFlow(activity, flowParams);
                    }
                } else {
                        showDialogNotPodpiska();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                    showDialError();
                isConnection = false;
            }
        });

    }

    private void showDialogNotConnact() {
        Intent intent = new Intent(context, DialogNotConnact.class);
        context.startActivity(intent);
    }

    private void showDialogNotPodpiska(){
        Intent intent = new Intent(context, DialogNotPodpiska.class);
        context.startActivity(intent);
    }

    private void showDialError(){
        Intent intent = new Intent(context, DialError.class);
        context.startActivity(intent);
    }

    void stopConnect(){
        if (billingClient != null) billingClient.endConnection();
    }

}
