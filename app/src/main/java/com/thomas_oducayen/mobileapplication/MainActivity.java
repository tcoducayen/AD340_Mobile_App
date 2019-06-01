package com.thomas_oducayen.mobileapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        Button tl = findViewById(R.id.tlButton);
        tl.setOnClickListener(this);
        Button tr = findViewById(R.id.trButton);
        tr.setOnClickListener(this);
        Button bl = findViewById(R.id.blButton);
        bl.setOnClickListener(this);
        Button br = findViewById(R.id.brButton);
        br.setOnClickListener(this);
//        Button send = findViewById(R.id.send_button);
//        send.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about_action, menu);
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tlButton:
                startZombies();
                break;
            case R.id.trButton:
                startCamera();
                break;
            case R.id.blButton:
                displayToast("Bottom left");
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

    public void startCamera() {
        Intent intent = new Intent(this, CameraList.class);
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
