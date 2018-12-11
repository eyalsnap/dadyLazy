package com.es.dadylazy;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter adapter;
    private WifiManager wifiManager;
    private AudioManager audioManager;

    private CheckBox btOnCheckBox,
                    btOffCheckBox,
                    gpsOnCheckBox,
                    gpsOffCheckBox,
                    wifiOnCheckBox,
                    wifiOffCheckBox,
                    soundOnCheckBox,
                    soundOffCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCheckBoxes();

        initAdapters();
        
        initObjects();

        initButtons();

    }

    private void initAdapters() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

    }

    private void initCheckBoxes() {
        btOnCheckBox = (CheckBox) findViewById(R.id.btOnCheckbox);
        btOffCheckBox = (CheckBox) findViewById(R.id.btOffCheckbox);
        gpsOnCheckBox = (CheckBox) findViewById(R.id.gpsOnCheckBox);
        gpsOffCheckBox = (CheckBox) findViewById(R.id.gpsOffCheckBox);
        wifiOnCheckBox = (CheckBox) findViewById(R.id.wifiOnCheckBox);
        wifiOffCheckBox = (CheckBox) findViewById(R.id.wifiOffCheckBox);
        soundOnCheckBox = (CheckBox) findViewById(R.id.soundOnCheckBox);
        soundOffCheckBox = (CheckBox) findViewById(R.id.soundOffCheckBox);
    }

    private void initObjects() {
        try{
            Thread.sleep(200);
        }
        catch (InterruptedException e) {}

        btOnCheckBox.setChecked(adapter.isEnabled());
        btOffCheckBox.setChecked(!adapter.isEnabled());
        wifiOnCheckBox.setChecked(wifiManager.isWifiEnabled());
        wifiOffCheckBox.setChecked(!wifiManager.isWifiEnabled());
        soundOnCheckBox.setChecked(audioManager.isSpeakerphoneOn());
        soundOffCheckBox.setChecked(!audioManager.isSpeakerphoneOn());
    }

    private void initButtons() {
        Button onButton = (Button) findViewById(R.id.setOnButton);
        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!adapter.isEnabled())
//                    adapter.enable();
//                if (!wifiManager.isWifiEnabled())
//                    wifiManager.setWifiEnabled(true);
                if (audioManager.isSpeakerphoneOn())
                    audioManager.setSpeakerphoneOn(false);
                turnGPSOn();
                initObjects();
            }
        });

        Button offButton = (Button) findViewById(R.id.setOfButton);
        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.isEnabled())
                    adapter.disable();
                if (wifiManager.isWifiEnabled())
                    wifiManager.setWifiEnabled(false);
                if (audioManager.isSpeakerphoneOn())
                    audioManager.setSpeakerphoneOn(true);
                turnGPSOff();
                initObjects();
            }
        });
    }

    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }

    }

    private void turnGPSOff(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }


}
