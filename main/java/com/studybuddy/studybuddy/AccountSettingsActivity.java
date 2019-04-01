package com.studybuddy.studybuddy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AccountSettingsActivity extends AppCompatActivity {

    private static final String TAG = "AccountSettings";

    public void backPressed(View view) {
        finish();
    }

    public void changeEmailPressed(View view) {

        this.changeEmail();
    }

    public void changePasswordPressed(View view) {

        this.changePassword();
    }

    /*
    public void deleteAccountPressed(View view) {

        this.deleteAccount();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
    }


    public void changeEmail() {
        Intent intent = new Intent(getApplicationContext(), ChangeEmailActivity.class);

        startActivity(intent);
    }

    public void changePassword() {
        Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);

        startActivity(intent);
    }

    /*
    public void deleteAccount() {
        Intent intent = new Intent(getApplicationContext(), DeleteAccountActivity.class);

        startActivity(intent);
    }*/


}
