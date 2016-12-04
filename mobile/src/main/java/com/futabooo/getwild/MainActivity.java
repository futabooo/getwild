package com.futabooo.getwild;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.CompoundButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    private SwitchCompat serviceSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceSwitch = (SwitchCompat) findViewById(R.id.service_witch);
        serviceSwitch.setChecked(isServiceRunning("com.futabooo.getwild.GetWildListenerService"));
        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Service止められてないときの保険
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if (isChecked) {
                    sp.edit().putBoolean("isEnable", true).commit();
                    startService(new Intent(MainActivity.this, GetWildListenerService.class));
                } else {
                    sp.edit().putBoolean("isEnable", false).commit();
                    stopService(new Intent(MainActivity.this, GetWildListenerService.class));
                }
                buttonView.setChecked(isChecked);
            }
        });
    }

    private boolean isServiceRunning(String className) {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0, max = services.size(); i < max; i++) {
            Log.d(TAG, services.get(i).service.getClassName());
            if (services.get(i).service.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }
}
