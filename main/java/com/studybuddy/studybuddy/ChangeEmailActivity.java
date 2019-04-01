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

public class ChangeEmailActivity extends AppCompatActivity{

    private static final String TAG = "com.studybuddy.studybuddy.ChangeEmail.User";

    public void backPressed(View view) {
        finish();
    }

    public void newEmailPressed(View view) {

        TextView password = findViewById(R.id.passwordText);
        TextView email = findViewById(R.id.emailText);

        String pass = password.getText().toString();
        String mail = email.getText().toString();

        this.newEmail(mail, pass);
    }

    public void newEmail(String email, String password) {

        Toast.makeText(ChangeEmailActivity.this,
                "Changed Email", Toast.LENGTH_SHORT).show();

        goHome();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
    }

    public void goHome() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

        startActivity(intent);
    }

}
