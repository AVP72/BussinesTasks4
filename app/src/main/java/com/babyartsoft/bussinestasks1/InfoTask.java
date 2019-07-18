package com.babyartsoft.bussinestasks1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.babyartsoft.bussinestasks1.Interface.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class InfoTask extends AppCompatActivity implements View.OnClickListener, Constant {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_task);

        Intent intent = getIntent();
        long dateInto = intent.getLongExtra(INFO_DATA_INTO, ZERO_L);
        long dateLastEdit = intent.getLongExtra(IFNO_DATA_LAST_EDIT, ZERO_L);
        long dateDelivery = intent.getLongExtra(INFO_DATE_DELIVERY, ZERO_L);
        long dateRead = intent.getLongExtra(INFO_DATE_READ, ZERO_L);
        long dateEditStatus = intent.getLongExtra(INFO_DATE_EDIT_SATUS, ZERO_L);
        String userEditStatus = intent.getStringExtra(INFO_USER_EDIT_SATATUS);
        String idUser = intent.getStringExtra(INFO_USER_ID);

        TextView txtDateInto = findViewById(R.id.text_info_into_date);
        TextView txtDateLastEdit = findViewById(R.id.text_info_last_edit_date);
        TextView txtDateDelivery = findViewById(R.id.text_info_delivery_date);
        TextView txtDateRead = findViewById(R.id.text_info_read_date);
        TextView txtDateEditStatus = findViewById(R.id.text_info_edit_status_date);
        final TextView txtUserEditStatus = findViewById(R.id.text_info_user_edit_status);

        txtDateInto.setText(Public.setTxtDateTime(dateInto));
        txtDateLastEdit.setText(Public.setTxtDateTime(dateLastEdit));
        if (dateDelivery == ZERO_L) txtDateDelivery.setText(R.string.not_delivery);
            else txtDateDelivery.setText(Public.setTxtDateTime(dateDelivery));
        if (dateRead == ZERO_L) txtDateRead.setText(R.string.not_read);
            else txtDateRead.setText(Public.setTxtDateTime(dateRead));
        txtDateEditStatus.setText(Public.setTxtDateTime(dateEditStatus));

        if (userEditStatus == null){
            txtUserEditStatus.setText(ZERO_S);
        } else {
            if (idUser != null) if (userEditStatus.equals(idUser)) {
                txtUserEditStatus.setText(R.string.author_my);
            } else {
                Public.dbRefSlaves.child(userEditStatus).child(Slaves.getPoleName_displayNameSlaveEdit())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                txtUserEditStatus.setText(dataSnapshot.getValue(String.class));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                txtUserEditStatus.setText(ZERO_S);
                            }
                        });
            }
        }

        Button ok = findViewById(R.id.info_task_button_ok);
        ok.setOnClickListener(this);
 }

    @Override
    public void onClick(View v) {
        finish();
    }
}
