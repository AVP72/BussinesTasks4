package com.babyartsoft.bussinestasks1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.babyartsoft.bussinestasks1.Interface.Constant;
import com.babyartsoft.bussinestasks1.Message.Device;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.babyartsoft.bussinestasks1.Public.user;

public class DeviceActivity extends AppCompatActivity implements Constant {

    RecyclerView listDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        listDevice = findViewById(R.id.list_device);


        final DatabaseReference dbRefDevices = FirebaseDatabase.getInstance().getReference(DEVICES).child(user.getUid());
        Query query = dbRefDevices.orderByChild(Device.queryByDate());

        final FirebaseRecyclerAdapter adapterDevices = new FirebaseRecyclerAdapter<Device, DeviceViewHolder>
                (Device.class, R.layout.item_device, DeviceViewHolder.class, query) {
            @Override
            protected void populateViewHolder(DeviceViewHolder viewHolder, Device model, int position) {
                String txtDateReg = getString(R.string.date_reg_device)+" " +Public.setTxtDate(model.getDateRegDevice());
                final Context context = viewHolder.dateReg.getContext();
                viewHolder.dateReg.setText(txtDateReg);
                viewHolder.nameDevice.setText(model.getName());
                SharedPreferences sp = getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
                final String keyThis = sp.getString(KEY_DEVICE, ZERO_S);
                final String key = getRef(position).getKey();
                if (key != null) if (!key.equals(keyThis)){
                    viewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                            dialog.setTitle(R.string.off_device);
                            dialog.setMessage(R.string.off_device_msg);
                            dialog.setCancelable(false);

                            dialog.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbRefDevices.child(key).removeValue();
                                }
                            });

                            dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Просто закрывается диалог
                                }
                            });

                            dialog.show();

                        }
                    });
                } else {
                    viewHolder.dateReg.setTextColor(getResources().getColor(R.color.primaryDark));
                    viewHolder.nameDevice.setTextColor(getResources().getColor(R.color.primaryDark));

                    viewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                            dialog.setTitle(R.string.it_is_current);
                            dialog.setMessage(R.string.current_no_delete);
                            dialog.setCancelable(false);

                            dialog.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Просто закрывается диалог
                                }
                            });

                            dialog.show();
                        }
                    });
                }
            }
        };

        listDevice.setLayoutManager(new LinearLayoutManager(this));
        listDevice.setAdapter(adapterDevices);
    }
}
