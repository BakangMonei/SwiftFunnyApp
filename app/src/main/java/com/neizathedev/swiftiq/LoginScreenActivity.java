package com.neizathedev.swiftiq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import android.Manifest;

public class LoginScreenActivity extends AppCompatActivity {

    // My Widgets
    private TextView signin, others;
    private ProgressBar progressBar;
    private EditText username, password;
    private Button loginbtn, forgotpass, signUp;
    private ImageView googlesignup, fbsignup, twittersignup, instagramicoon;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        // TextViews
        signin = (TextView) findViewById(R.id.signin);
        others = (TextView) findViewById(R.id.others);

        // Progress Bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // EditTexts
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        // Buttons
        loginbtn = (Button) findViewById(R.id.loginbtn);
        forgotpass = (Button) findViewById(R.id.forgotpass);
        signUp = (Button) findViewById(R.id.signUp);

        // ImageViews
        googlesignup = (ImageView) findViewById(R.id.googlesignup);
        fbsignup = (ImageView) findViewById(R.id.fbsignup);
        twittersignup = (ImageView) findViewById(R.id.twittersignup);
        instagramicoon = (ImageView) findViewById(R.id.instasignup);

        // Firebase initialization [For Login]
        mAuth = FirebaseAuth.getInstance();

        // Login Functionality on the button
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fetch input data from Edit text views
                String email = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                // Validating the email & password
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(LoginScreenActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            FirebaseUser user = mAuth.getCurrentUser();
                            finish();
                            goToDashboard();
                        } else if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            FirebaseUser admin = mAuth.getCurrentUser();
                            finish();
                            goToAdmin();
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginScreenActivity.this, "Sign in failed." + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Creating an user account
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegisterActivity();
            }
        });

        // Google SignUp
        googlesignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToGoogle();
            }
        });
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginScreenActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this).setTitle("Permission Request").setMessage("This App requires your location to work. Activate Your Location please.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(LoginScreenActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                }).create().show();
            } else {
                ActivityCompat.requestPermissions(LoginScreenActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    if (ContextCompat.checkSelfPermission(LoginScreenActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    }
                } else {
                    Toast.makeText(LoginScreenActivity.this, "Permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void goToAdmin() {
        // Intent intent = new Intent(MainActivity, AdministratorActivity.class);
        // startActivity(intent);
    }

    public void goToRegisterActivity() {
        Intent intent = new Intent(LoginScreenActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void goToDashboard() {
        Intent intent = new Intent(LoginScreenActivity.this, DashBoard.class);
        startActivity(intent);
    }

    public void goToGoogle() {
        //Intent intent = new Intent(LoginScreenActivity.this, GoogleSignInActivity.class);
        //startActivity(intent);
    }
}