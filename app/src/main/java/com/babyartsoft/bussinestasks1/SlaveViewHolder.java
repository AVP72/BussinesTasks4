package com.babyartsoft.bussinestasks1;

import android.app.Activity;
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
import com.babyartsoft.bussinestasks1.Interface.IsendAddSlaveActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SlaveViewHolder  extends RecyclerView.ViewHolder implements Constant, IsendAddSlaveActivity {

    TextView displayNameTxt, profTxt, phoneTxt;
    private Context context;
    private AddSlaveActivity addSlaveActivity;

    public SlaveViewHolder(final View itemView) {
        super(itemView);

        displayNameTxt = itemView.findViewById(R.id.item_slave_txt_name);
        profTxt = itemView.findViewById(R.id.item_slave_txt_prof);
        phoneTxt = itemView.findViewById(R.id.item_slave_txt_phone);

        ImageView editImg = itemView.findViewById(R.id.item_slave_img_edit);
        ImageView deleteImg = itemView.findViewById(R.id.item_slave_img_delete);

        this.context = displayNameTxt.getContext();

        itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int k = getAdapterPosition();
                    Public.adapterSlave.getRef(k).
                            addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String key = dataSnapshot.getKey();
                            final Intent intent = new Intent();
                            intent.putExtra(SLAVE_KEY, key);
                            addSlaveActivity.setResult(Activity.RESULT_OK, intent);
                            addSlaveActivity.finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
            });

        editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int k = getAdapterPosition();
                Public.adapterSlave.getRef(k).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Slaves slaves = dataSnapshot.getValue(Slaves.class);
                        if (slaves == null) return;
                        String keySlave = dataSnapshot.getKey();
                        if (keySlave == null) return;
                        Intent intentEditSlave = new Intent(context, EditSlaveActivity.class);
                        intentEditSlave.putExtra(SLAVE_DISPLAY_NAME,slaves.getDisplayNameSlave() );
                        intentEditSlave.putExtra(SLAVE_DISPLAY_NAME_EDIT,slaves.getDisplayNameSlaveEdit() );
                        intentEditSlave.putExtra(SLAVE_PROF, slaves.getProf());
                        intentEditSlave.putExtra(SLAVE_PHONE, slaves.getPhoneNumber());
                        intentEditSlave.putExtra(SLAVE_KEY, keySlave);
                        context.startActivity(intentEditSlave);
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
                int k = getAdapterPosition();
                Public.adapterSlave.getRef(k).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Slaves slaves = dataSnapshot.getValue(Slaves.class);
                        final String keySlave = dataSnapshot.getKey();
                        if (slaves == null || keySlave == null) return;

                        final String nameSlave = IS_DEL + slaves.getDisplayNameSlaveEdit();

                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        String s = context.getString(R.string.delete_item);
                        String name;
                        if (slaves.getDisplayNameSlaveEdit() == null) name = "";
                            else name = slaves.getDisplayNameSlaveEdit();
                        String prof;
                        if (slaves.getProf() == null) prof = "";
                            else prof = slaves.getProf();
                        s = s + " " + name + " " + prof ;


                        dialog.setTitle(s);
                        dialog.setCancelable(false);

                        dialog.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    Public.dbRefTasks.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot ds: dataSnapshot.getChildren()){
                                                String keyDelete = ds.getKey();
                                                if (keyDelete == null) break;
                                                BussinessTask bt = ds.getValue(BussinessTask.class);
                                                if (bt != null){
                                                    String keySlaveDelete = bt.getIdSlave();
                                                    if (keySlave.equals(keySlaveDelete)){
                                                        bt.setIdSlave(nameSlave);
                                                        bt.setDone(Status.newDone);
                                                        Public.dbRefTasks.child(keyDelete).setValue(bt);
                                                        // у Slave удаляет сервер
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                Public.dbRefSlaves.child(keySlave).removeValue();
                                // у партнера удаляется сервером
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

    @Override
    public void send(AddSlaveActivity addSlaveActivity) {
        this.addSlaveActivity = addSlaveActivity;
    }
}
