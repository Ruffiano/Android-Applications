package com.example.user.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class AlertDialogActivity extends Activity {

    private String notiMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Bundle bun = getIntent().getExtras();
        notiMessage = bun.getString("notiMessage");


        setContentView(R.layout.activity_alert_dialog);

        TextView adMessage = (TextView)findViewById(R.id.message);
        adMessage.setText(notiMessage);

        Button adButton = (Button)findViewById(R.id.submit);

        adButton.setOnClickListener(new SubmitOnClickListener());


    }
    private class SubmitOnClickListener implements View.OnClickListener {

        public void onClick(View v) {
            finish();

        }
    }
}