package com.babyartsoft.bussinestasks1;

import android.content.Context;
import com.babyartsoft.bussinestasks1.Interface.Constant;

public class Status implements Constant {

    public static int newDone   = 0;

    public static final int id_go     = 1;
    public static final int id_galka  = 2;
    public static final int id_pusto  = 3;
    public static final int id_pause  = 4;
    public static final int id_cancel = 5;

    private final int drawable_pusto    = R.drawable.ic_status_pusto_black_36dp;
    private final int drawable_go       = R.drawable.ic_status_go_black_36dp;
    private final int drawable_galka    = R.drawable.ic_status_galka_black_36dp;
    private final int drawable_pause    = R.drawable.ic_status_pause_black_36dp;
    private final int drawable_cancel   = R.drawable.ic_status_cancel_black_36dp;

    private final int name_pusto        = R.string.status_pusto;
    private final int name_go           = R.string.status_go;
    private final int name_galka        = R.string.status_galka;
    private final int name_pause        = R.string.status_pause;
    private final int name_cancel       = R.string.status_cancel;

    public int getDrawableFromId(int id){
        switch (id){
            case id_pusto:  return drawable_pusto;
            case id_go:     return drawable_go;
            case id_galka:  return drawable_galka;
            case id_pause:  return drawable_pause;
            case id_cancel: return drawable_cancel;
        }
        return ZERO;
    }

    int getNameFromId(int id){
        switch (id){
            case id_pusto:  return name_pusto;
            case id_go:     return name_go;
            case id_galka:  return name_galka;
            case id_pause:  return name_pause;
            case id_cancel: return name_cancel;
        }
        return ZERO;
    }

    String getNameStatusFilter(Context context, int id){
        switch (id){
            case FILTER_STATUS_UNLOCK:
                return context.getString(R.string.unlock);
            case FILTER_STATUS_LOCK:
                return context.getString(R.string.lock);
            case FILTER_STATUS_GO:
                return context.getString(R.string.status_go);
            case FILTER_STATUS_GALKA:
                return context.getString(R.string.status_galka);
            case FILTER_STATUS_PUSTO:
                return context.getString(R.string.status_pusto);
            case FILTER_STATUS_PAUSE:
                return context.getString(R.string.status_pause);
            case FILTER_STATUS_CANCEL:
                return context.getString(R.string.status_cancel);
            default:
                return context.getString(R.string.all_status);
        }
    }

    public static final int id_done_10        = 10;

    public static final int id_done_no        = 0;
    public static final int id_done_yes       = 1;
    public static final int id_done_delivery  = 2;
    public static final int id_done_read      = 3;

    private final int drawable_done_no      = R.drawable.ic_done_not_clock_black_24dp;
    private final int drawable_done_yes     = R.drawable.ic_done_black_24dp;
    private final int drawable_done_delivery= R.drawable.ic_done_all_black_24dp;
    private final int drawable_done_raed    = R.drawable.ic_done_all_blue_24dp;

    public int getDrawableFromIdDone(int id){
        switch (id){
            case id_done_no:    return drawable_done_no;
            case id_done_yes:   return drawable_done_yes;
            case id_done_delivery:return drawable_done_delivery;
            case id_done_read:  return drawable_done_raed;
        }
        return ZERO;
    }

}
