package com.babyartsoft.bussinestasks1;

import com.babyartsoft.bussinestasks1.Interface.Constant;

public class BussinessTask implements Constant {

    private String  task;
    private String  taskForSort;
    private String  noteSlave;
    private long    dateInto;
    private long    dateLastEdit;
    private long    dateSrok;
    private int     status;
    private long    dateEditStatus;
    private String  idUserEditStatus;
    private String  idAuthor;
    private String  idSlave;
    private long    dateRead;
    private long    dateDelivery;
    private int     done;

    public BussinessTask() {}

    BussinessTask(String task, long dateInto, long dateSrok, String idSlave) {
        this.task = task;
        taskForSort = Public.installTaskForSortFromTask(task);
        this.dateInto = dateInto;
        dateLastEdit = dateEditStatus = System.currentTimeMillis();
        this.dateSrok = dateSrok;
        status = Status.id_pusto;
        idUserEditStatus = idAuthor = Public.user.getUid();
        this.idSlave = idSlave;
        dateRead = dateDelivery = ZERO_L;
        done = Status.newDone;
        noteSlave = ZERO_S;
    }

    BussinessTask(String task, String taskForSort, long dateInto, long dateLastEdit, long dateSrok,
                  int status, long dateEditStatus, String idUserEditStatus, String idAuthor, String idSlave,
                  long dateRead, long dateDelivery, int done, String noteSlave) {
        this.task = task;
        this.taskForSort = taskForSort;
        this.dateInto = dateInto;
        this.dateLastEdit = dateLastEdit;
        this.dateSrok = dateSrok;
        this.status = status;
        this.dateEditStatus = dateEditStatus;
        this.idUserEditStatus = idUserEditStatus;
        this.idAuthor = idAuthor;
        this.idSlave = idSlave;
        this.dateRead = dateRead;
        this.dateDelivery = dateDelivery;
        this.done = done;
        this.noteSlave = noteSlave;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public long getDateInto() {
        return dateInto;
    }

    public void setDateInto(long dateInto) {
        this.dateInto = dateInto;
    }

    public long getDateLastEdit() {
        return dateLastEdit;
    }

    public void setDateLastEdit(long dateLastEdit) {
        this.dateLastEdit = dateLastEdit;
    }

    public long getDateSrok() {
        return dateSrok;
    }

    public void setDateSrok(long dateSrok) {
        this.dateSrok = dateSrok;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getDateEditStatus() {
        return dateEditStatus;
    }

    public void setDateEditStatus(long dateEditStatus) {
        this.dateEditStatus = dateEditStatus;
    }

    public String getIdUserEditStatus() {
        return idUserEditStatus;
    }

    public void setIdUserEditStatus(String idUserEditStatus) {
        this.idUserEditStatus = idUserEditStatus;
    }

    public String getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(String idAuthor) {
        this.idAuthor = idAuthor;
    }

    public String getIdSlave() {
        return idSlave;
    }

    public void setIdSlave(String idSlave) {
        this.idSlave = idSlave;
    }

    public long getDateRead() {
        return dateRead;
    }

    public void setDateRead(long dateRead) {
        this.dateRead = dateRead;
    }

    public long getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(long dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public String getTaskForSort() {
        return taskForSort;
    }

    public void setTaskForSort(String taskForSort) {
        this.taskForSort = taskForSort;
    }

    public String getNoteSlave() {
        return noteSlave;
    }

    public void setNoteSlave(String noteSlave) {
        this.noteSlave = noteSlave;
    }

    public static String getNamePole_IdSlave(){
        return "idSlave";
    }

    public static String getPoleAuthor(){ return  "idAuthor";}

    public static String getPoleSortForTask(){ return "taskForSort"; }
    public static String getPoleSortForDateInto(){return "dateInto";}
    public static String getPoleSortForDateSrok(){return "dateSrok";}
    public static String getPoleSortForSlave(){return "idSlave";}
    public static String getPoleSortForStatus(){return "status";}

    public static String getPoleForDateRead(){return "dateRead";}
    public static String getPoleForDateDelivery(){return "dateDelivery";}
    public static String getPoleForDone(){return "done";}

}
