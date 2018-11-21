package com.example.user.demo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.demo.R;
import com.example.user.demo.database.DatabaseHelper;
import com.example.user.demo.utils.Settings;

import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by User on 01.03.2016.
 */
public class SignActivity extends BaseActivity {

//    @Bind(R.id.login)
//    EditText loginT;
    @Bind(R.id.password)
    EditText passwordT;
    @Bind(R.id.repeatPassword)
    EditText repeatPasswordT;

    @Bind(R.id.signBtn)
    Button signBtn;
    @Inject
    Settings settings;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

//        loginT.requestFocus();
        passwordT.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(passwordT, InputMethodManager.SHOW_IMPLICIT);
        databaseHelper = new DatabaseHelper(this);
    }

    @OnClick(R.id.signBtn)
    void sign() {

        final ProgressDialog progressDialog = ProgressDialog.show(this, null, "Creating....");

//        final String userName = loginT.getText().toString();
        final String userName = "aaa";
        final String password = passwordT.getText().toString();
        String confirmPassword = repeatPasswordT.getText().toString();

        // check if any of the fields are vaccant
        if (userName.equals("") || password.equals("") || confirmPassword.equals("")) {
            Toast.makeText(getApplicationContext(), "Field", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }
        // check if both password matches
        if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        } else {

            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    settings.saveAuth(true);
                    databaseHelper.saveUser(userName, password);
                    Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
                    String dirPath = getFilesDir().getAbsolutePath() + File.separator + "check";
                    File projDir = new File(dirPath);
                    if (!projDir.exists())
                        projDir.mkdirs();

                    Intent SignIn = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(SignIn);
                    finish();
                }
            }, 3000);


        }

    }

}
