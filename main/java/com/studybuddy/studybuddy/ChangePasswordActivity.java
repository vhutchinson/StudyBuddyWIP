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

public class ChangePasswordActivity extends AppCompatActivity{

    private static final String TAG = "com.studybuddy.studybuddy.ChangPassword.User";
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Back button
        public void backPressed(View view) {
            finish();
        }

        //Submit new password button
        public void newPasswordPressed(View view) {

            TextView password = findViewById(R.id.passwordText);
            TextView newPassword = findViewById(R.id.newPasswordText);
            TextView verPassword = findViewById(R.id.verifyPasswordText);

            String pass = password.getText().toString();
            String newPass = newPassword.getText().toString();
            String verPass = verPassword.getText().toString();

            this.newPassword(pass, newPass, verPass);
        }

        //Authentication of current password and saving new password
        public void newPassword(String password, String newPassword, String verPassword) {


            @Override
            public void onComplete(@NonNull Task<Void> task) {

                //If current password is verified
                if (task.isSuccessful()) {

                    //If new password matches confirmation new password
                    if (newPassword == verPassword) {

                        //Update in Firebase
                        user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {

                            if (task.isSuccessful()) {
                                Log.d(TAG, "changePassword:success");

                                //Notify user that password change was successful
                                Toast.makeText(ChangePasswordActivity.this,
                                        "Changed Password", Toast.LENGTH_SHORT).show();
                            }

                            else {
                                Log.d(TAG, "Error password not updated");
                            }
                        });
                        goHome();
                    }

                    else {

                        Log.d(TAG, "New password match:failed");

                        //Notify user that their new password needs to match their confirmation new password
                        Toast.makeText(ChangePasswordActivity.this,
                                "New Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                    }
                }

                else {

                    Log.d(TAG, "Authentication:failed");

                    //Notify user that their current password could not be verified
                    Toast.makeText(ChangePasswordActivity.this,
                            "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }

        public void goHome() {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

            startActivity(intent);
        }

    }