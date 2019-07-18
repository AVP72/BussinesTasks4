package com.babyartsoft.bussinestasks1;

import com.babyartsoft.bussinestasks1.Interface.Constant;
import static com.babyartsoft.bussinestasks1.Public.listTask;
import static com.babyartsoft.bussinestasks1.Public.listTaskNotFilter;

public class Filter implements Constant {

    private int statusHead;
    private long srokBegin, srokEnd;
    private int period;
    private String slave;
    private String task;

    public Filter() {
        task = ZERO_S;
    }


    public void addToList(ItemBussinessTask ibt){
        int iStatus = ibt.getStatus();
        long iSrok = ibt.getDateSrok();
        String iSlave = ibt.getIdSlave();

        if (belongsStatusStatusHead(iStatus)
                && srokBegin <= iSrok && iSrok <= srokEnd
                && slaveAllOrNot(iSlave)
                ) {
            listTask.add(ibt);
        }
    }

    public void setToList(ItemBussinessTask ibt){

        if (listTask == null || listTaskNotFilter == null) return;

        String keyFind = ibt.getKey();
            boolean isInList=false;
            for (int i = 0; i < listTask.size(); i++) {
                if (keyFind.equals(listTask.get(i).getKey())) {
                    isInList=true;
                    int iStatus = ibt.getStatus();
                    long iSrok = ibt.getDateSrok();
                    String iSlave = ibt.getIdSlave();
                    if (belongsStatusStatusHead(iStatus)
                            && srokBegin <= iSrok && iSrok <= srokEnd
                            && slaveAllOrNot(iSlave)
                            ) {
                        listTask.set(i, ibt);
                    } else {
                        listTask.remove(i);
                    }
                }
            }

            if (!isInList){                         // если в списке нет, но условию удовлетноряет, то добавить в список
                int iStatus = ibt.getStatus();
                long iSrok = ibt.getDateSrok();
                String iSlave = ibt.getIdSlave();
                if (belongsStatusStatusHead(iStatus)
                        && srokBegin <= iSrok && iSrok <= srokEnd
                        && slaveAllOrNot(iSlave)
                        ) {
                    listTask.add(ibt);
                }
            }
    }
    public void removeFromList(String key){

        if (listTask == null || listTaskNotFilter == null) return;
        for (ItemBussinessTask i: listTask){
            if (key.equals(i.getKey())){
                listTask.remove(i);
                return;
            }
        }
    }

    public void refresh(){

        if (listTask == null || listTaskNotFilter == null) return;

        listTask.clear();
        int j=0;
        for (ItemBussinessTask i: listTaskNotFilter){
            int iStatus = i.getStatus();
            long iSrok = i.getDateSrok();
            String iSlave = i.getIdSlave();
            String iTask = i.getTask();
            if (belongsStatusStatusHead(iStatus)
                    && srokBegin <= iSrok && iSrok <= srokEnd
                    && slaveAllOrNot(iSlave)
                    && containTask(iTask, task)
            ) {
                listTask.add(i);
            }
        }
    }

    private boolean belongsStatusStatusHead(int status){
        switch (statusHead){
            case FILTER_STATUS_OFF:
                return true;
            case FILTER_STATUS_UNLOCK:
                return status == Status.id_go || status == Status.id_pusto || status == Status.id_pause;
            case FILTER_STATUS_LOCK:
                return status == Status.id_galka || status == Status.id_cancel;
            case FILTER_STATUS_GO:
                return status == Status.id_go;
            case FILTER_STATUS_GALKA:
                return status == Status.id_galka;
            case FILTER_STATUS_PUSTO:
                return status == Status.id_pusto;
            case FILTER_STATUS_PAUSE:
                return status == Status.id_pause;
            case FILTER_STATUS_CANCEL:
                return status == Status.id_cancel;
            default:
                return false;
        }
    }

    private boolean slaveAllOrNot(String iSlave) {
        return slave.equals(FILTER_SLAVE_ALL) || slave.equals(iSlave);
    }

    private boolean containTask(String text, String into){
        return text.toLowerCase().contains(into.toLowerCase());
    }

    public int getStatusHead() {
        return statusHead;
    }

    public void setStatusHead(int statusHead) {
        this.statusHead = statusHead;
    }

    public long getSrokBegin() {
        return srokBegin;
    }

    public void setSrokBegin(long srokBegin) {
        this.srokBegin = srokBegin;
    }

    public long getSrokEnd() {
        return srokEnd;
    }

    public void setSrokEnd(long srokEnd) {
        this.srokEnd = srokEnd;
    }

    public String getSlave() {
        return slave;
    }

    public void setSlave(String slave) {
        this.slave = slave;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
