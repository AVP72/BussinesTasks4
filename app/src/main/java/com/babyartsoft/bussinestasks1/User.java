package com.babyartsoft.bussinestasks1;

public class User {

    private String uid;
    private String gmail;
    private String displayName;
    private String phoneNamber;
//    private String deviceToken;
    private long dateReg;
    private long dateRegAsBossTreal;      // дата регистрации как босса
    private int statAsBoss;               // статус регистрации
    private String purchaseToken;
    public static final String sDateRegAsBossTreal = "dateRegAsBossTreal";
    public static final String sStatAsBoss = "statAsBoss";
    public static final String sPurchaseToken = "purchaseToken";

    public User() {}

    public User(String uid, String gmail, String displayName, String phoneNamber, long dateReg) {
        this.uid = uid;
        this.gmail = gmail;
        this.displayName = displayName;
        this.phoneNamber = phoneNamber;
//        this.deviceToken = deviceToken;
        this.dateReg = dateReg;
        this.dateRegAsBossTreal = 0L;
        this.statAsBoss = 0;
    }

    public User(String uid, String displayName) {
        this.uid = uid;
        this.displayName = displayName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNamber() {
        return phoneNamber;
    }

    public void setPhoneNamber(String phoneNamber) {
        this.phoneNamber = phoneNamber;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

/*
    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
*/

    public long getDateReg() {
        return dateReg;
    }

    public void setDateReg(long dateReg) {
        this.dateReg = dateReg;
    }

    public long getDateRegAsBossTreal() {
        return dateRegAsBossTreal;
    }

    public void setDateRegAsBossTreal(long dateRegAsBossTreal) {
        this.dateRegAsBossTreal = dateRegAsBossTreal;
    }

    public int getStatAsBoss() {
        return statAsBoss;
    }

    public void setStatAsBoss(int statAsBoss) {
        this.statAsBoss = statAsBoss;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }
}
