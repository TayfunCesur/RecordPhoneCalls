package com.xionces.StoreCallRecords;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Tayfun CESUR on 24.01.2016.
 */
public class CallDetectService extends Service {
    private CallHelper callHelper;

    public CallDetectService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        callHelper = new CallHelper(this);

        int res = super.onStartCommand(intent, flags, startId);
        callHelper.start();
        return res;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        callHelper.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
