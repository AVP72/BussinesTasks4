package com.babyartsoft.bussinestasks1;

public class ItemBussinessTask extends BussinessTask {

    private String key;

    public ItemBussinessTask(String task, long dateInto, long dateSrok, String idSlave, String key) {
        super(task, dateInto, dateSrok, idSlave);
        this.key = key;
    }

    ItemBussinessTask(String key, BussinessTask bt) {
        super(bt.getTask(), bt.getTaskForSort(), bt.getDateInto(), bt.getDateLastEdit(), bt.getDateSrok(),
                bt.getStatus(), bt.getDateEditStatus(), bt.getIdUserEditStatus(), bt.getIdAuthor(), bt.getIdSlave(),
                bt.getDateRead(), bt.getDateDelivery(), bt.getDone(), bt.getNoteSlave());
        this.key = key;

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    BussinessTask getBussinessTask() {
        return new BussinessTask(getTask(), getTaskForSort(), getDateInto(), getDateLastEdit(), getDateSrok(),
                getStatus(), getDateEditStatus(), getIdUserEditStatus(), getIdAuthor(), getIdSlave(),
                getDateRead(), getDateDelivery(), getDone(), getNoteSlave());
    }
}
