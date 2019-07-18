package com.babyartsoft.bussinestasks1.Help;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.babyartsoft.bussinestasks1.Public;
import com.babyartsoft.bussinestasks1.R;
import com.babyartsoft.bussinestasks1.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static com.babyartsoft.bussinestasks1.Interface.Constant.FILE_USER_SELF;
import static com.babyartsoft.bussinestasks1.Interface.Constant.KEY_USER_SELF;
import static com.babyartsoft.bussinestasks1.Interface.Constant.USERS;
import static com.babyartsoft.bussinestasks1.Interface.Constant.ZERO_L;
import static com.babyartsoft.bussinestasks1.Interface.Constant.ZERO_S;
import static com.babyartsoft.bussinestasks1.RegAsBoss.statEndTreal;
import static com.babyartsoft.bussinestasks1.RegAsBoss.statIsPay;
import static com.babyartsoft.bussinestasks1.RegAsBoss.statNoBoss;
import static com.babyartsoft.bussinestasks1.RegAsBoss.statNotPay;
import static com.babyartsoft.bussinestasks1.RegAsBoss.statTreal;

public class AboutUserActivity extends AppCompatActivity {

    TextView nameTxt, emailTxt, dateRegTxt, infoBossTxt, dateTrealBossTxt, dateTrealBossEndTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_user);

        nameTxt = findViewById(R.id.about_user_name_data);
        emailTxt = findViewById(R.id.about_user_email_data);
        dateRegTxt = findViewById(R.id.about_user_date_reg_data);
        infoBossTxt = findViewById(R.id.about_user_stat_boss_data);
        dateTrealBossTxt = findViewById(R.id.about_user_stat_boss_date_data);
        dateTrealBossEndTxt = findViewById(R.id.about_user_stat_boss_date_end_data);

        final SharedPreferences sp = getSharedPreferences(FILE_USER_SELF, Context.MODE_PRIVATE);
        String key = sp.getString(KEY_USER_SELF, ZERO_S);
        if (!key.equals(ZERO_S)) {
            DatabaseReference dbRefUser = FirebaseDatabase.getInstance().getReference(USERS).child(key);
            dbRefUser.keepSynced(true);

            dbRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null){
                        setPoleUser(user);
                    } else {
                        setPoleUserAtError();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    setPoleUserAtError();
                }
            });
        }
    }

    private void setPoleUser(User user) {

        String name = user.getDisplayName();
        String email = user.getGmail();
        long dateReg = user.getDateReg();
        int stat = user.getStatAsBoss();
        long dateTreal = user.getDateRegAsBossTreal();

        if (name == null){
            nameTxt.setText(R.string.error_read_data);
        } else {
            nameTxt.setText(name);
        }

        if (email == null){
            emailTxt.setText(R.string.error_read_data);
        } else {
            emailTxt.setText(email);
        }

        if (dateReg == ZERO_L){
            dateRegTxt.setText(R.string.error_read_data);
        } else {
            dateRegTxt.setText(Public.setTxtDate(dateReg));
        }

        if (dateTreal == ZERO_L){
            dateTrealBossTxt.setText(R.string.not_activ);
            dateTrealBossEndTxt.setText(R.string.not_activ);
        } else {
            dateTrealBossTxt.setText(Public.setTxtDate(dateTreal));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dateTreal);
            calendar.add(Calendar.MONTH, 3);
            calendar.add(Calendar.DATE, 1);
            dateTrealBossEndTxt.setText(Public.setTxtDate(calendar));
        }

        switch (stat){
            case statNoBoss:
                infoBossTxt.setText(R.string.not_activ);
                break;
            case statTreal:
                infoBossTxt.setText(R.string.treal);
                break;
            case statEndTreal:
                infoBossTxt.setText(R.string.treal_end);
                break;
            case statIsPay:
                infoBossTxt.setText(R.string.is_pay);
                break;
            case statNotPay:
                infoBossTxt.setText(R.string.is_not_pay);
                break;
            default:
                infoBossTxt.setText(R.string.error_read_data);
        }
    }

    private void setPoleUserAtError() {
        nameTxt.setText(R.string.error_read_data);
        emailTxt.setText(R.string.error_read_data);
        dateRegTxt.setText(R.string.error_read_data);
        dateTrealBossTxt.setText(R.string.error_read_data);
        infoBossTxt.setText(R.string.error_read_data);
        dateTrealBossEndTxt.setText(R.string.error_read_data);
    }
}
