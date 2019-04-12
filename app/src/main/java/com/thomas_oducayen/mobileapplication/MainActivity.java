package com.thomas_oducayen.mobileapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button tl = (Button) findViewById(R.id.tlButton);
        tl.setOnClickListener(this);
        Button tr = (Button) findViewById(R.id.trButton);
        tr.setOnClickListener(this);
        Button bl = (Button) findViewById(R.id.blButton);
        bl.setOnClickListener(this);
        Button br = (Button) findViewById(R.id.brButton);
        br.setOnClickListener(this);
        Button send = (Button) findViewById(R.id.send_button);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tlButton:
                Toast.makeText(getApplicationContext(),
                        "Top left button was clicked", Toast.LENGTH_LONG).show();
                break;

            case R.id.trButton:
                Toast.makeText(getApplicationContext(),
                        "Top right button was clicked", Toast.LENGTH_LONG).show();
                break;

            case R.id.blButton:
                Toast.makeText(getApplicationContext(),
                        "Bottom left button was clicked", Toast.LENGTH_LONG).show();
                break;

            case R.id.brButton:
                Toast.makeText(getApplicationContext(),
                        "Bottom right button was clicked", Toast.LENGTH_LONG).show();
                break;
            case R.id.send_button:
                Toast.makeText(getApplicationContext(),
                        "Message sent! (Not really, just mock up)", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}
