package com.babyartsoft.bussinestasks1;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceViewHolder extends RecyclerView.ViewHolder {

    TextView dateReg, nameDevice;
    ImageView deleteImg;

    public DeviceViewHolder(View itemView) {
        super(itemView);
        dateReg = itemView.findViewById(R.id.item_device_date_reg);
        nameDevice = itemView.findViewById(R.id.item_device_name);
        deleteImg = itemView.findViewById(R.id.item_device_delete);
    }

}
