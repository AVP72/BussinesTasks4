package com.babyartsoft.bussinestasks1;

public class Slaves {

    private String uidSvale;
    private String displayNameSlave;
    private String displayNameSlaveEdit;
    private String phoneNumber;
    private String prof;

    public Slaves() {}

    public Slaves(String displayNameSlave, String phoneNumber) {
        this.displayNameSlave = displayNameSlave;
        this.phoneNumber = phoneNumber;
    }

    public Slaves(String displayNameSlave, String displayNameSlaveEdit, String phoneNumber, String prof) {
        this.displayNameSlave = displayNameSlave;
        this.displayNameSlaveEdit = displayNameSlaveEdit;
        this.phoneNumber = phoneNumber;
        this.prof = prof;
    }

    public String getUidSvale() {
        return uidSvale;
    }

    public void setUidSvale(String uidSvale) {
        this.uidSvale = uidSvale;
    }

    public String getDisplayNameSlave() {
        return displayNameSlave;
    }

    public void setDisplayNameSlave(String displayNameSlave) {
        this.displayNameSlave = displayNameSlave;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getDisplayNameSlaveEdit() {
        return displayNameSlaveEdit;
    }

    public void setDisplayNameSlaveEdit(String displayNameSlaveEdit) {
        this.displayNameSlaveEdit = displayNameSlaveEdit;
    }

    public static String queryByName(){
        return "displayNameSlave";
    }

    public static String getPoleName_displayNameSlaveEdit(){
        return "displayNameSlaveEdit";
    }
}
