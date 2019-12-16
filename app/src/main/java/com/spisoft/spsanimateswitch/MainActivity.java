package com.spisoft.spsanimateswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.spisoft.spanimswitch.SpaSwitch;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpaSwitch SPA = findViewById(R.id.spa);
        List<SpaSwitch.SpaSwitchVal> sVal = new ArrayList<>();
        sVal.add(new SpaSwitch.SpaSwitchVal("A",getResources().getDrawable(R.drawable.ic_3d_rotation_black_24dp)));
        sVal.add(new SpaSwitch.SpaSwitchVal("d",getResources().getDrawable(R.drawable.ic_account_box_black_24dp)));
        SPA.setListValues(sVal);
        SPA.setIcon(null);
    }
}
