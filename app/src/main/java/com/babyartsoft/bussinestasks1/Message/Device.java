package com.babyartsoft.bussinestasks1.Message;

public class Device {

    private long dateRegDevice; // дата регистрации устройства (когда на нем регистрируется User)
    private String name;        // Имя устройства
    private String token;       // текущий токен
    private int version;        // версия программы
    final public static String DEVICES = "Devices"; // Имя ветки в FireBase

    public Device() {   // для FireBase
    }

    public Device(long dateRegDevice, String name, String token, int version) {
        this.dateRegDevice = dateRegDevice;
        this.name = name;
        this.token = token;
        this.version = version;
    }

    public long getDateRegDevice() {
        return dateRegDevice;
    }

    public void setDateRegDevice(long dateRegDevice) {
        this.dateRegDevice = dateRegDevice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public static String queryByDate(){
        return "dateRegDevice";
    }

}
