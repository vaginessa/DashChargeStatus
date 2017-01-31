package org.out386.dashchargestats;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import android.support.v4.content.LocalBroadcastManager;

public class MainActivity extends Activity {

    private TextView startingTv;
    private TextView statusTv;
    private TextView limitTv;
    private TextView maxTv;
    private TextView currentTv;
    private TextView battStatusTv;
    private LocalBroadcastManager broadcastManager;

    private BroadcastReceiver uiReceiverStarting = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String tv1 = intent.getStringExtra(Constants.UI_UPDATE_MESSAGE);
            int code = intent.getIntExtra(Constants.UI_UPDATE_MESSAGE_CODE, -1);
            switch (code) {
                case 0:
                    if (tv1.equals("1"))
                        startingTv.setText("Dash charging");
                    else
                        startingTv.setText("Slow Charging");
                    break;
                case 1:
                    if (tv1.equals("1"))
                        statusTv.setText("Dash charger detected");
                    else
                        statusTv.setText("Dash charger not detected");
                    break;
                case 2:
                    if (tv1.equals("1"))
                        limitTv.setText("Current limited");
                    else
                        limitTv.setText("Current not limited");
                    break;
                case 3:
                    maxTv.setText("Max current in microamps: " + tv1);
                    break;
                case 4:
                    currentTv.setText("Current in microamps: " + tv1);
                    break;
                case 5:
                    battStatusTv.setText("Battery status: " + tv1);
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startingTv = (TextView) findViewById(R.id.startingTv);
        statusTv = (TextView) findViewById(R.id.statusTv);
        limitTv = (TextView) findViewById(R.id.limitTv);
        maxTv = (TextView) findViewById(R.id.maxCurrentTv);
        currentTv = (TextView) findViewById(R.id.nowCurrentTv);
        battStatusTv = (TextView) findViewById(R.id.batteryStatusTv);
        broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onResume() {
        broadcastManager.registerReceiver((uiReceiverStarting),
                new IntentFilter(Constants.UI_UPDATE_INTENT));
        startService(new Intent(this, DataRefreshService.class));
        super.onResume();
    }

    @Override
    protected void onPause() {
        broadcastManager.unregisterReceiver(uiReceiverStarting);
        stopService(new Intent(this, DataRefreshService.class));
        super.onPause();
    }
}
