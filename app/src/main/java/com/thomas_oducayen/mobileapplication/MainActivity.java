package com.thomas_oducayen.mobileapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    Button send = (Button)findViewById(R.id.send_button);
    EditText send_name = (EditText)findViewById(R.id.name);
    EditText send_email = (EditText)findViewById(R.id.email);
    EditText send_password = (EditText)findViewById(R.id.password);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Button tl = findViewById(R.id.tlButton);
        tl.setOnClickListener(this);
        Button tr = findViewById(R.id.trButton);
        tr.setOnClickListener(this);
        Button bl = findViewById(R.id.blButton);
        bl.setOnClickListener(this);
        Button br = findViewById(R.id.brButton);
        br.setOnClickListener(this);

        send.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about_action, menu);
        return true;
    }


    private boolean validate() {

        String username = send_name.getText().toString();
        String password = send_password.getText().toString();

        final EditText emailValidate = findViewById(R.id.email);

        final TextView textView = findViewById(R.id.textView);

        String email = emailValidate.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if (email.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(), "valid email address", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (username.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Please Enter User", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Please Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void signIn() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword("tcoducayen@gmail.com","papaya")
                .addOnCompleteListener(this, new
                        OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                Log.d("FIREBASE", "signIn:onComplete:" +
                                        task.isSuccessful());
                                if (task.isSuccessful()) {
                                    // update profile
                                    FirebaseUser user =
                                            FirebaseAuth.getInstance().getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new
                                            UserProfileChangeRequest.Builder()
                                            .setDisplayName("value")
                                            .build();
                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull
                                                                               Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("FIREBASE", "User profile updated.");
                                                        // Go to FirebaseActivity
                                                        startActivity(new
                                                                Intent(MainActivity.this, TeamActivity.class));
                                                    }
                                                }
                                            });
                                } else {
                                    Log.d("FIREBASE", "sign-in failed");
                                    Toast.makeText(MainActivity.this, "Sign In Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tlButton:
                startZombies();
                break;
            case R.id.trButton:
                startCameras();
                break;
            case R.id.blButton:
                startMap();
                break;
            case R.id.brButton:
                displayToast("Bottom right");
                break;
            default:
                break;
        }
    }

    public void startZombies() {
        Intent intent = new Intent(this, ZombieList.class);
        startActivity(intent);
    }

    public void startCameras() {
        Intent intent = new Intent(this, TrafficCamActivity.class);
        startActivity(intent);
    }

    public void startMap() {
        Intent intent = new Intent(this, MapsActivity2.class);
        startActivity(intent);
    }

    public void displayToast(String direction) {
        Toast.makeText(getApplicationContext(),
                direction + " button was clicked", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_about:
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
