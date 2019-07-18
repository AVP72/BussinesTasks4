package com.babyartsoft.bussinestasks1;

public class ItemSlaves extends Slaves {

    private String key;
    private int idItemMenu;

    ItemSlaves(String key, String displayNameSlave) {
        super(displayNameSlave, "");
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    int getIdItemMenu() {
        return idItemMenu;
    }

    void setIdItemMenu(int idItemMenu) {
        this.idItemMenu = idItemMenu;
    }
}
