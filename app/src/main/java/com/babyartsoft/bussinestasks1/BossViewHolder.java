package com.babyartsoft.bussinestasks1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.babyartsoft.bussinestasks1.Interface.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class BossViewHolder extends RecyclerView.ViewHolder implements Constant {

    TextView displayNameTxt, profTxt, phoneTxt;
    Context context;

    public BossViewHolder(View itemView) {
        super(itemView);

        displayNameTxt = itemView.findViewById(R.id.item_boss_txt_name);
        profTxt = itemView.findViewById(R.id.item_boss_txt_prof);
        phoneTxt = itemView.findViewById(R.id.item_boss_txt_phone);

        ImageView editImg, deleteImg;
        editImg = itemView.findViewById(R.id.item_slave_img_edit);
        deleteImg = itemView.findViewById(R.id.item_slave_img_delete);

        this.context = displayNameTxt.getContext();

        editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int k = getAdapterPosition();

                Public.adapterBoss.getRef(k).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Slaves boss = dataSnapshot.getValue(Slaves.class);
                        if (boss == null) return;
                        String keyBoss = dataSnapshot.getKey();
                        if (keyBoss == null) return;
                        Intent editBossItem = new Intent(context, EditBossItemActivity.class);
                        String dName = boss.getDisplayNameSlave();
                        if (dName == null) dName = ZERO_S;
                        String dNameEdit = boss.getDisplayNameSlaveEdit();
                        if (dNameEdit == null) dNameEdit = ZERO_S;
                        String prof = boss.getProf();
                        if (prof == null) prof = ZERO_S;
                        String phone = boss.getPhoneNumber();
                        if (phone == null) phone = ZERO_S;

                        editBossItem.putExtra(SLAVE_DISPLAY_NAME, dName );
                        editBossItem.putExtra(SLAVE_DISPLAY_NAME_EDIT,  dNameEdit);
                        editBossItem.putExtra(SLAVE_PROF, prof);
                        editBossItem.putExtra(SLAVE_PHONE, phone);
                        editBossItem.putExtra(SLAVE_KEY, keyBoss);
                        context.startActivity(editBossItem);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Public.toLog("==== deleteImg ==");
                int k = getAdapterPosition();
                Public.adapterBoss.getRef(k).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Slaves boss = dataSnapshot.getValue(Slaves.class);
                        if (boss == null) return;
                        final String keyBoss = dataSnapshot.getKey();
                        if (keyBoss == null) return;

//                        final String nameBoss = boss.getDisplayNameSlaveEdit();

                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        String s = context.getString(R.string.delete_item);
                        String name;
                        if (boss.getDisplayNameSlaveEdit() == null) name = "";
                        else name = boss.getDisplayNameSlaveEdit();
                        String prof;
                        if (boss.getProf() == null) prof = "";
                        else prof = boss.getProf();
                        s = s + " " + name + " " + prof ;

                        dialog.setTitle(s);
                        dialog.setMessage(R.string.and_del_task);
                        dialog.setCancelable(false);

                        dialog.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Public.dbRefTasks.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                                            String keyDelete = ds.getKey();
                                            if (keyDelete == null) return;
                                            BussinessTask bt = ds.getValue(BussinessTask.class);
                                            if (bt != null){
                                                String keyBossDelete = bt.getIdAuthor();
                                                if (keyBossDelete == null) return;
                                                if (keyBoss.equals(keyBossDelete)){
                                                    Public.dbRefTasks.child(keyDelete).removeValue();
                                                    // Slave в _not.slave.key устанавливает сервер
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                Public.dbRefBoss.child(keyBoss).removeValue();
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
