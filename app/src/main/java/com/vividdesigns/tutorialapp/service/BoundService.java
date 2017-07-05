package com.vividdesigns.tutorialapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Ashutosh Purushottam on 04/07/17.
 */

public class BoundService extends Service {

    private final IBinder myBinder = new MyLocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public int getRandomNumber() {
        return (int)(Math.random() * 1000);
    }

    public class MyLocalBinder extends Binder {
        public BoundService getService() {
            return BoundService.this;
        }
    }

}
