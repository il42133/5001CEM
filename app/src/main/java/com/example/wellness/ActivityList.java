package com.example.wellness;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.List;

public class ActivityList extends AppCompatActivity {

    List<String> activityList, getActivityListTime;
    String username;
    Button buttonAddActivity;
    TextInputEditText textInputEditTextActivity, textInputEditTextTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        buttonAddActivity = findViewById(R.id.addActivity);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        buttonAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addActivityDialog();
            }
        });




    }

    void addActivityDialog() {
        final Dialog dialog = new Dialog(ActivityList.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_list);

        EditText activityName = dialog.findViewById(R.id.activityName);
        EditText activityTime = dialog.findViewById(R.id.activityTime);
        Button confirmActivityButton = dialog.findViewById(R.id.confirm_activity_button);

        confirmActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activityN = activityName.getText().toString();
                String activityT = activityTime.getText().toString();
                Integer activityTI = Integer.parseInt(activityT);
                if(!activityN.equals("") && !activityT.equals("")) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[2];
                            field[0] = "activity";
                            field[1] = "time";
                            String[] data = new String[2];
                            data[0] = activityN;
                            data[1] = activityT;
                            PutData putData = new PutData("http://192.168.100.25/5001CEM/activity.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Activity Successfully added")){
                                        PutData putData2 = new PutData("http://192.168.100.25/5001CEM/updatePoints.php", "POST", field, data);
                                        if (putData2.startPut()) {
                                            if (putData2.onComplete()) {
                                                String result2 = putData2.getResult();
                                                Toast.makeText(getApplicationContext(), result2, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}