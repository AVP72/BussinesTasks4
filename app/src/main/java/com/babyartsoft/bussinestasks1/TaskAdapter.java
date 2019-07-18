package com.babyartsoft.bussinestasks1;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babyartsoft.bussinestasks1.Interface.Constant;

import java.util.ArrayList;

import static com.babyartsoft.bussinestasks1.Public.dbRefTasks;
import static com.babyartsoft.bussinestasks1.Public.listSlave;
import static com.babyartsoft.bussinestasks1.Public.user;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Item> implements Constant {

    private ArrayList<ItemBussinessTask> itemTask ;

    TaskAdapter(ArrayList<ItemBussinessTask> itemTask) {
        this.itemTask = itemTask;
    }

    static class Item extends RecyclerView.ViewHolder implements Constant{

        TextView txtTask, txtDate, txtTime, txtSlave;
        ImageView imgStatus, imgDone;
        Context context;
        LinearLayout ll;

        Item(View itemView, final ArrayList<ItemBussinessTask> arrayList) {
            super(itemView);

            txtTask = itemView.findViewById(R.id.text_item);
            txtDate = itemView.findViewById(R.id.text_date);
            txtTime = itemView.findViewById(R.id.text_time);
            txtSlave = itemView.findViewById(R.id.text_slave);
            imgStatus = itemView.findViewById(R.id.image_status);
            imgDone = itemView.findViewById(R.id.image_done);
            ll = itemView.findViewById(R.id.item_task_all);

            this.context = txtTask.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int k = getAdapterPosition();
                    String keyAuthor = arrayList.get(k).getIdAuthor();
                    if (keyAuthor != null) {
                        Intent intent;
                        if (keyAuthor.equals(Public.user.getUid())) {
                            intent = new Intent(context, EditTaskActivity.class);
                        } else {
                            intent = new Intent(context, EditStatusActivity.class);
                        }
                        intent.putExtra(ADAPTER_POSITION, k);
                        context.startActivity(intent);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    txtTask.setTextColor(context.getResources().getColor(R.color.accentNormal));
                    PopupMenu popupMenu = new PopupMenu(context, imgStatus);
                    popupMenu.inflate(R.menu.popup_context);
                    popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                        @Override
                        public void onDismiss(PopupMenu menu) {
                            txtTask.setTextColor(context.getResources().getColor(R.color.black));
                        }
                    });
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            txtTask.setTextColor(context.getResources().getColor(R.color.black));

                            switch (item.getItemId()){
                                case R.id.popup_context_send: {
                                    int k = getAdapterPosition();
                                    Intent sendTaskIntent = new Intent();
                                    sendTaskIntent.setAction(Intent.ACTION_SEND);
                                    String sendTaskString = txtTask.getText().toString();
                                    sendTaskString += "\n";
                                    sendTaskString += context.getString(R.string.dateSrok) + " "
                                            + txtDate.getText().toString() + " "
                                            + txtTime.getText().toString();
                                    sendTaskString += "\n";
                                    sendTaskString += context.getString(R.string.status) + ": ";
                                    int id_string_status = new Status().getNameFromId(arrayList.get(k).getStatus());
                                    sendTaskString += context.getString(id_string_status);

                                    sendTaskIntent.putExtra(Intent.EXTRA_TEXT, sendTaskString);
                                    sendTaskIntent.setType("text/plain");
                                    context.startActivity(sendTaskIntent);
                                    break;
                                }
                                case R.id.popup_context_copy: {
//                                    int k = getAdapterPosition();
                                    String copyTaskToClip = txtTask.getText().toString();
                                    ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                                    if (clipboard == null) return false;
                                    ClipData clip = ClipData.newPlainText("lable", copyTaskToClip);
                                    clipboard.setPrimaryClip(clip);
                                    break;
                                }
                                case R.id.popup_context_edit: {
                                    int k = getAdapterPosition();
                                    if (k < arrayList.size()) {
                                        String keyAuthor = arrayList.get(k).getIdAuthor();
                                        if (keyAuthor != null) {
                                            Intent intent;
                                            if (keyAuthor.equals(Public.user.getUid())) {
                                                intent = new Intent(context, EditTaskActivity.class);
                                            } else {
                                                intent = new Intent(context, EditStatusActivity.class);
                                            }
                                            intent.putExtra(ADAPTER_POSITION, k);
                                            context.startActivity(intent);
                                        }
                                    }
                                    break;
                                }
                                case R.id.popup_context_delete: {
                                    final int k = getAdapterPosition();
                                    String idAuthor = arrayList.get(k).getIdAuthor();
                                    if (!idAuthor.equals(user.getUid())){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setTitle(R.string.title_message_delete)
                                                .setMessage(context.getString(R.string.message1_delete) +
                                                        context.getString(R.string.message2_delete))
                                                .setNegativeButton(R.string.message_btn_close, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                })
                                                .setCancelable(false);
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();

                                    } else {
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setTitle(R.string.delete_task_title)
                                                .setMessage(R.string.delete_task_message)
                                                .setCancelable(false)
                                                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        String idSlave = arrayList.get(k).getIdSlave();
                                                        String key = arrayList.get(k).getKey();
                                                        dbRefTasks.child(key).removeValue();
                                                    }
                                                })
                                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                    }
                                    break;
                                }
                                default:

                            }
                            return false;
                        }
                    });

                    popupMenu.show();

                    return true;
                }
            });

        }
    }

    @NonNull
    @Override
    public TaskAdapter.Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new Item(view, itemTask);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskAdapter.Item item, int position) {

        Context context = item.txtTask.getContext();

        ItemBussinessTask ibtp = itemTask.get(position);

        item.txtTask.setText(ibtp.getTask());
        long date = ibtp.getDateSrok();
        int status = ibtp.getStatus();
        item.txtDate.setText(Public.setTxtDate(date));
        item.txtTime.setText(Public.setTxtTime(date));
        if (status == Status.id_pause || status == Status.id_pusto || status == Status.id_go){
            long currentTime = System.currentTimeMillis();
            long minus = date - currentTime;
            if (minus <= ZERO_L){
                item.txtDate.setTextColor(context.getResources().getColor(R.color.accentNormal));
                item.txtTime.setTextColor(context.getResources().getColor(R.color.accentNormal));
            } else if (minus <= time30minut){
                item.txtDate.setTextColor(context.getResources().getColor(R.color.accentDark));
                item.txtTime.setTextColor(context.getResources().getColor(R.color.accentDark));
            } else if (minus <= time60minut){
                item.txtDate.setTextColor(context.getResources().getColor(R.color.accentSuperDark));
                item.txtTime.setTextColor(context.getResources().getColor(R.color.accentSuperDark));
            } else if (minus <= time2hour){
                item.txtDate.setTextColor(context.getResources().getColor(R.color.primaryNormal));
                item.txtTime.setTextColor(context.getResources().getColor(R.color.primaryNormal));
            } else if (minus <= time3hour){
                item.txtDate.setTextColor(context.getResources().getColor(R.color.primaryDark));
                item.txtTime.setTextColor(context.getResources().getColor(R.color.primaryDark));
            } else if (minus <= time4hour){
                item.txtDate.setTextColor(context.getResources().getColor(R.color.primarySuperDark));
                item.txtTime.setTextColor(context.getResources().getColor(R.color.primarySuperDark));
            } else {
                item.txtDate.setTextColor(context.getResources().getColor(R.color.gray50));
                item.txtTime.setTextColor(context.getResources().getColor(R.color.gray50));
            }
        } else {
            item.txtDate.setTextColor(context.getResources().getColor(R.color.gray50));
            item.txtTime.setTextColor(context.getResources().getColor(R.color.gray50));
        }
        item.imgStatus.setImageResource(new Status().getDrawableFromId(status));
        int done = ibtp.getDone();
        item.imgDone.setImageResource(new Status().getDrawableFromIdDone(done));

        // Красим не прочитаные в желтый, а прочитаные в белый, для не своих заданий
        String uidSelf = user.getUid();
        String uidAuthor = ibtp.getIdAuthor();

//        Public.toLog("task= "+ ibtp.getTask() + " user=" + uidSelf + " author= " + uidAuthor + " equals= " + (uidSelf.equals(uidAuthor))) ;
        if (!(uidSelf.equals(uidAuthor)) ){

            if (ibtp.getDateRead() == ZERO_L) {
                item.ll.setBackgroundColor(context.getResources().getColor(R.color.liteYellow));
            } else {
                item.ll.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        } else {
            item.ll.setBackgroundColor(context.getResources().getColor(R.color.white));
        }


        String keySlave = ibtp.getIdSlave();
        final String userThisId = user.getUid();
        boolean isInListSlave;
        int flagNormal = 1281;              // это флаг по умолчанию, что бы избежать не верных зачеркиваний
        if (keySlave.equals(userThisId)){
            item.txtSlave.setText(R.string.setForMy);
            item.txtSlave.setTextColor(context.getResources().getColor(R.color.accentDark));
            item.txtSlave.setPaintFlags(flagNormal);
        } else if (keySlave.equals(NOT_SLAVE_KEY)){
            item.txtSlave.setText(context.getString(R.string.setNobody));
            item.txtSlave.setTextColor(context.getResources().getColor(R.color.primaryDark));
            item.txtSlave.setPaintFlags(flagNormal);
        } else {
            isInListSlave = false;
            for (int i=0; i<listSlave.size();i++) {
                String keyFind = listSlave.get(i).getKey();
                if (keySlave.equals(keyFind)) {
                    item.txtSlave.setText(listSlave.get(i).getDisplayNameSlaveEdit());
                    item.txtSlave.setTextColor(context.getResources().getColor(R.color.gray50));
                    item.txtSlave.setPaintFlags(flagNormal);
                    isInListSlave = true;
                    break;
                }
            }
            if (!isInListSlave){
                item.txtSlave.setText(keySlave);
                item.txtSlave.setTextColor(context.getResources().getColor(R.color.primaryDark));
                item.txtSlave.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);       // зачерктутый текст
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemTask.size();
    }
}
