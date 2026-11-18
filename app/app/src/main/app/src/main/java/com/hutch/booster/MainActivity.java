package com.hutch.booster;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.view.Gravity;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Hutch Stabilizer Ready!\n\nPull down your Notification Panel,\nclick the Edit (Pencil) icon,\nand add the 'Hutch Boost' tile to turn it ON/OFF.");
        tv.setTextSize(18);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(40, 40, 40, 40);
        setContentView(tv);
    }
}
