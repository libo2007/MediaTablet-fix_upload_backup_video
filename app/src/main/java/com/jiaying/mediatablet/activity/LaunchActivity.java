package com.jiaying.mediatablet.activity;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jiaying.mediatablet.R;
import com.jiaying.mediatablet.utils.MyLog;
import com.jiaying.mediatablet.utils.WifiAdmin;

public class LaunchActivity extends AppCompatActivity {
    private boolean wifiIsOk = false;
    //wifi自动连接begin
    private WifiAdmin wifiAdmin = null;
    private static final String SSID = "JiaYing_ZXDC";
    private static final String PWD = "jyzxdcarm";
    private static final int TYPE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        wifiAdmin = new WifiAdmin(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        autoWifiConnect();
    }

    //自动连接wifi
    private void autoWifiConnect() {
        wifiIsOk = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (wifiAdmin.checkState() == WifiManager.WIFI_STATE_ENABLED) {

                        wifiIsOk = wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(SSID, PWD, TYPE));

                        if (wifiIsOk) {
                            //wifi连接上
                            MyLog.e("ERROR", "指定wifi已连接上");
                            LaunchActivity.this.startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                            break;
                        }
                    } else {
                        wifiAdmin.openWifi();
                    }
                    try {
                        Thread.sleep(3000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}


