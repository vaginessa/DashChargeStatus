package org.out386.dashchargestats;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public class DataRefreshService extends Service {
    private Handler handler;
    private static Runnable runnable;
    private static Shell.Interactive rootSession;
    private LocalBroadcastManager broadcastManager;
    private Intent uiUpdateIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        startRoot();
        broadcastManager = LocalBroadcastManager.getInstance(this);
        uiUpdateIntent = new Intent(Constants.UI_UPDATE_INTENT);

        handler = new Handler();
         runnable = new Runnable() {
            @Override
            public void run() {
                gatherData();
                handler.postDelayed(runnable, 1000);
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
    }

    private void startRoot() {
        if (rootSession == null) {
            rootSession = new Shell.Builder()
                    .useSU()
                    .setWatchdogTimeout(1)
                    .open(new Shell.OnCommandResultListener() {
                        @Override
                        public void onCommandResult(int commandCode, int exitCode, List<String> output) {
                            if (exitCode != Shell.OnCommandResultListener.SHELL_RUNNING) {
                                Log.i("DASH", "Root permission denied");
                                onDestroy();
                            }
                        }
                    });
        }
    }

    private void gatherData() {
        for (int i = 0; i < Constants.PATHS.length; i++)
            rootSession.addCommand(new String[] { "cat " + Constants.PATHS[i]}, i,
                    new Shell.OnCommandResultListener() {
                        public void onCommandResult(int commandCode, int exitCode, List<String> output) {
                            if (exitCode < 0) {
                                Log.i("DASH", "Failed to execute command");
                            } else {
                                sendData(output, commandCode);
                            }
                        }
                    });
    }

    private void sendData(List<String> data, int code) {
        if (data == null || data.size() == 0)
            return;

        //Log.i("DASH", "run: " + data.get(0));

        uiUpdateIntent.putExtra(Constants.UI_UPDATE_MESSAGE, data.get(0));
        uiUpdateIntent.putExtra(Constants.UI_UPDATE_MESSAGE_CODE, code);
        broadcastManager.sendBroadcast(uiUpdateIntent);
    }
}
