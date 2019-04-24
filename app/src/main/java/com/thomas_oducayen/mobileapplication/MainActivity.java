package com.thomas_oducayen.mobileapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button tl = findViewById(R.id.tlButton);
        tl.setOnClickListener(this);
        Button tr = findViewById(R.id.trButton);
        tr.setOnClickListener(this);
        Button bl = findViewById(R.id.blButton);
        bl.setOnClickListener(this);
        Button br = findViewById(R.id.brButton);
        br.setOnClickListener(this);
        Button send = findViewById(R.id.send_button);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tlButton:
                startZombies();
                break;
            case R.id.trButton:
                displayToast("Top right");
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

    public void displayToast(String direction) {
        Toast.makeText(getApplicationContext(),
                direction + " button was clicked", Toast.LENGTH_LONG).show();
    }

}
