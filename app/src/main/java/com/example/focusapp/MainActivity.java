package com.example.focusapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    private static final String server = "https://shrivseshan.com/focus";
    private static final String TAG = "FOCUS DEBUG";
    private EditText editUserID;
    private TextView tbox;
    private TextView netOutput;
    private String token;

    public void uiUpdate(String output) {
        // implement uiUpdate action that occurs after AsyncTask
        Log.d(TAG, "Entered Async Post Execute Method");
        this.netOutput.setText(output);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbox = (TextView) findViewById(R.id.tbox);
        editUserID = (EditText) findViewById(R.id.edit_userid);
        netOutput = (TextView) findViewById(R.id.net_output);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("focus app", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("focus app", msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void registerClicked(View view) {
        // Send content entered to the server
        Log.d(TAG, "Entered On Click Method");
        tbox.setText(token);
        new NetworkAsyncTask(this).execute(server, this.editUserID.getText().toString(), this.token);
    }
}
