package com.babyartsoft.bussinestasks1.Message;

public class MassegeReg {

    private String id;
    private long dateReg;
    private String nameDevice;
    private String body;

    public static final String sId = "id";
    public static final String sDateReg = "dateReg";
    public static final String sNameDevice = "nameDevice";
    public static final String sBody = "body";

    public static final String bodyNew = "m1";
    public static final String bodyEditTask = "m2";
    public static final String bodyEditStatus = "m3";
    public static final String bodyEditSrok = "m4";
    public static final String bodyEditAll = "m5";
    public static final String bodyRemove = "m6";

    public static final int bodyNewInt = 101;
    public static final int bodyEditTaskInt = 102;
    public static final int bodyEditStatusInt = 103;
    public static final int bodyEditSrokInt = 104;
    public static final int bodyEditAllInt = 105;
    public static final int bodyRemoveInt = 106;

    public MassegeReg(String id, long dateReg, String nameDevice) {
        this.id = id;
        this.dateReg = dateReg;
        this.nameDevice = nameDevice;
        this.body = "body";
    }

    public MassegeReg(String id) {
        this.id = id;
    }

    public MassegeReg() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDateReg() {
        return dateReg;
    }

    public void setDateReg(long dateReg) {
        this.dateReg = dateReg;
    }

    public String getNameDevice() {
        return nameDevice;
    }

    public void setNameDevice(String nameDevice) {
        this.nameDevice = nameDevice;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
