package com.example.user.demo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.user.demo.R;
import com.example.user.demo.database.DatabaseHelper;
import com.example.user.demo.utils.Settings;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {

//    @Bind(R.id.login)
//    EditText loginT;
    @Bind(R.id.password)
    EditText passwordT;
//    @Bind(R.id.loginBtn)
//    Button loginBtn;

//    @Bind(R.id.toggle1)
//    ToggleButton toggle1;
//    @Bind(R.id.toggle2)
//    ToggleButton toggle2;
//    @Bind(R.id.toggle3)
//    ToggleButton toggle3;
//    @Bind(R.id.toggle4)
//    ToggleButton toggle4;

//    final ToggleButton arrayToggle[] = {toggle1,toggle2,toggle3,toggle4};

    ToggleButton arrToggle[];
    Button arrBtn[];

    String pwPattern=""; // 누적 패스워드 패턴


    DatabaseHelper databaseHelper;
    int attempt_counter = 3;

    @Inject
    Settings settings;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Log.e("check", "11111111111111");


        databaseHelper = new DatabaseHelper(this);


        if (!settings.getAuth()) {
            Intent Sign = new Intent(getApplicationContext(), SignActivity.class);
            startActivity(Sign);
            Log.e("check", "aaaaaaaaaaaaaa");
        } else {
            setContentView(R.layout.activity_login);


            arrToggle = new ToggleButton[]{(ToggleButton) findViewById(R.id.toggle1),
                    (ToggleButton) findViewById(R.id.toggle2),
                    (ToggleButton) findViewById(R.id.toggle3),
                    (ToggleButton) findViewById(R.id.toggle4)};

            arrBtn = new Button[]{(Button) findViewById(R.id.btn0),
                    (Button) findViewById(R.id.btn1),
                    (Button) findViewById(R.id.btn2),
                    (Button) findViewById(R.id.btn3),
                    (Button) findViewById(R.id.btn4),
                    (Button) findViewById(R.id.btn5),
                    (Button) findViewById(R.id.btn6),
                    (Button) findViewById(R.id.btn7),
                    (Button) findViewById(R.id.btn8),
                    (Button) findViewById(R.id.btn9)};

            for(int i=0; i<arrBtn.length; i++){
                arrBtn[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        arrToggle[pwPattern.length()].setChecked(true);
                        pwPattern += ((Button)v).getText();

                        passwordT.setText(pwPattern);

                        if(pwPattern.length() == 4){
                            Toast.makeText(getApplicationContext(), pwPattern, Toast.LENGTH_SHORT).show();
//                            for(int j=0;j<arrToggle.length;j++)
//                                arrToggle[j].setChecked(false);
                            login();
                        }
                    }
                });
            }

//            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

//           list = new ArrayList<String>();
//            for (BluetoothDevice db : pairedDevices){
//                s.add(db.getAddress());
//            }

        }

    }

//    @OnClick(R.id.loginBtn)
    void login() {

        final ProgressDialog progressDialog = ProgressDialog.show(this, null, "Creating....");

//        String userName = loginT.getText().toString();          //사용자 입력한 아이디
        String userName = "aaa";          //사용자 입력한 아이디
        final String password = passwordT.getText().toString(); //사용자가 입력한 패스워드
        passwordT.setText("");



        // fetch the Password form database for respective user name
        final String storedPassword = databaseHelper.getSingleUser(userName);


        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (password.equals(storedPassword)) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Congrats: LoginActivity Successfull", Toast.LENGTH_LONG).show();
                    Intent Sign = new Intent(getApplicationContext(), BluetoothListActivity.class);
                    pwPattern="";
                    for(int j=0;j<arrToggle.length;j++)
                        arrToggle[j].setChecked(false);

                    startActivity(Sign);

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
//                    passwordT.setText("");
                    pwPattern="";
//                    toggleOff(5);
                    for(int j=0;j<arrToggle.length;j++)
                        arrToggle[j].setChecked(false);
                }
            }
        }, 3000);
        // check if the Stored password matches with  Password entered by user

    }

    @OnClick(R.id.btndel)
    void pressdel(){


        if(pwPattern.length() > 0)
        {
            arrToggle[pwPattern.length() - 1].setChecked(false);
            pwPattern = pwPattern.substring(0, pwPattern.length() - 1);
        }


        passwordT.setText(pwPattern);

    }



//    @OnClick(R.id.btn1)
//    void press1(){
//        pwPattern += "1";
//        passwordT.setText(pwPattern);
//
//        int len = pwPattern.length();
//        toggleOn(len);
//
//        if(len == 4){
//            login();
//        }
//
//    }
//    @OnClick(R.id.btn2)
//    void press2(){
//        pwPattern += "2";
//        passwordT.setText(pwPattern);
//
//        int len = pwPattern.length();
//        toggleOn(len);
//
//        if(len == 4){
//            login();
//        }
//    }
//    @OnClick(R.id.btn3)
//    void press3(){
//        pwPattern += "3";
//        passwordT.setText(pwPattern);
//
//        int len = pwPattern.length();
//        toggleOn(len);
//
//        if(len == 4){
//            login();
//        }
//    }
//    @OnClick(R.id.btn4)
//    void press4(){
//        pwPattern += "4";
//        passwordT.setText(pwPattern);
//
//        int len = pwPattern.length();
//        toggleOn(len);
//
//        if(len == 4){
//            login();
//        }
//    }
//    @OnClick(R.id.btn5)
//    void press5(){
//        pwPattern += "5";
//        passwordT.setText(pwPattern);
//
//        int len = pwPattern.length();
//        toggleOn(len);
//
//        if(len == 4){
//            login();
//        }
//    }
//    @OnClick(R.id.btn6)
//    void press6(){
//        pwPattern += "6";
//        passwordT.setText(pwPattern);
//
//        int len = pwPattern.length();
//        toggleOn(len);
//
//        if(len == 4){
//            login();
//        }
//    }
//    @OnClick(R.id.btn7)
//    void press7(){
//        pwPattern += "7";
//        passwordT.setText(pwPattern);
//
//        int len = pwPattern.length();
//        toggleOn(len);
//
//        if(len == 4){
//            login();
//        }
//    }
//    @OnClick(R.id.btn8)
//    void press8(){
//        pwPattern += "8";
//        passwordT.setText(pwPattern);
//
//        int len = pwPattern.length();
//        toggleOn(len);
//
//        if(len == 4){
//            login();
//        }
//    }
//    @OnClick(R.id.btn9)
//    void press9(){
//        pwPattern += "9";
//        passwordT.setText(pwPattern);
//
//        int len = pwPattern.length();
//        toggleOn(len);
//
//        if(len == 4){
//            login();
//        }
//    }
//
//    @OnClick(R.id.btn0)
//    void press0(){
//        pwPattern += "0";
//        passwordT.setText(pwPattern);
//
//        int len = pwPattern.length();
//        toggleOn(len);
//
//        if(len == 4){
//            login();
//        }
//    }
//
//    @OnClick(R.id.btndel)
//    void pressdel(){
//        int len = pwPattern.length();
//        if(!(len < 1)) {
//            pwPattern = pwPattern.substring(0, len - 1);
//            toggleOff(len);
//
//        }
//
//        passwordT.setText(pwPattern);
//
//    }
//
//    void toggleOn(int len){
//
//        switch (len){
//            case 1:
//                toggle1.setChecked(true);
//                break;
//            case 2:
//                toggle2.setChecked(true);
//                break;
//            case 3:
//                toggle3.setChecked(true);
//                break;
//            case 4:
//                toggle4.setChecked(true);
//                break;
//        }
//    }
//
//    void toggleOff(int len){
//        switch (len){
//            case 1:
//                arrayToggle[0].setChecked(false);
//                break;
//            case 2:
//                toggle2.setChecked(false);
//                break;
//            case 3:
//                toggle3.setChecked(false);
//                break;
//            case 4:
//                toggle4.setChecked(false);
//                break;
//            case 5:
//                toggle1.setChecked(false);
//                toggle2.setChecked(false);
//                toggle3.setChecked(false);
//                toggle4.setChecked(false);
//                break;
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
    }

}
