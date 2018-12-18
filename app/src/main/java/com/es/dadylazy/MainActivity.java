package com.es.dadylazy;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
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

    private EditText followNumberEditText,
            followSecondsEditText;
    private boolean editableMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editableMode = false;

        initCheckBoxes();

        initAdapters();

        initButtons();

        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
    }

    private void initAdapters() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

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

        followNumberEditText = (EditText) findViewById(R.id.numberFollowEditText);
        followSecondsEditText = (EditText) findViewById(R.id.secondsFollowEditText);
    }

    private void initButtons() {
        Button onButton = (Button) findViewById(R.id.setOnButton);
        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bluetoothAdapter.isEnabled() && btOnCheckBox.isChecked())
                    bluetoothAdapter.enable();
                if (!wifiManager.isWifiEnabled() && wifiOnCheckBox.isChecked())
                    wifiManager.setWifiEnabled(true);
                if (audioManager.isSpeakerphoneOn())
                    //TODO the next command should make the phone noisy - dont work
                    audioManager.setSpeakerphoneOn(false);
                if (gpsOnCheckBox.isChecked())
                    turnGPSOn();
            }
        });

        Button offButton = (Button) findViewById(R.id.setOfButton);
        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothAdapter.isEnabled() && btOffCheckBox.isChecked())
                    bluetoothAdapter.disable();
                if (wifiManager.isWifiEnabled() && wifiOffCheckBox.isChecked())
                    wifiManager.setWifiEnabled(false);
                if (audioManager.isSpeakerphoneOn())
                    //TODO the next command should make the phone on silence - dont work
                    audioManager.setSpeakerphoneOn(true);
                if (gpsOffCheckBox.isChecked())
                    turnGPSOff();
            }
        });

        TextView followTextView = (TextView) findViewById(R.id.followTextView);
        followTextView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                if (editableMode) {
                    followNumberEditText.setInputType(InputType.TYPE_NULL);
                    followSecondsEditText.setInputType(InputType.TYPE_NULL);
                    Toast.makeText(getApplicationContext(), "not editable", Toast.LENGTH_SHORT).show();
                } else {
                    followNumberEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                    followSecondsEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                    Toast.makeText(getApplicationContext(), "editable", Toast.LENGTH_SHORT).show();
                }
                editableMode = !editableMode;

                return true;
            }
        });
    }

    private void turnGPSOn(){

        if (isGpsInMode(true))
            return;

        //TODO: here should be the code that turn on the location
    }

    private void turnGPSOff(){

        if (isGpsInMode(false))
            return;

        //TODO: here should be the code that turn off the location
    }

    private boolean isGpsInMode(boolean mode){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        return mode == provider.contains("gps");
    }

}
