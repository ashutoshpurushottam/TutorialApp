package com.vividdesigns.tutorialapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class RemoteService extends Service {

    private static final String LOG_TAG = RemoteService.class.getSimpleName();
    public static final int STRIP_STRING = 13;
    public static final int STRIP_STRING_RESPONSE = 17;

    private final Messenger myMessenger = new Messenger(new IncomingHandler());


    static class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STRIP_STRING: {
                    try {
                        Bundle data = msg.getData();

                        String obtainedString = data.getString("random");

                        String responseString = stripNonAlpha(obtainedString);
                        Message resp = Message.obtain(null, STRIP_STRING_RESPONSE);
                        Bundle bundle = new Bundle();
                        bundle.putString("strippedString", responseString);
                        resp.setData(bundle);
                        msg.replyTo.send(resp);
                    } catch (Exception e) {
                        Log.i(LOG_TAG, e.getMessage());
                    }
                }
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private static String stripNonAlpha(String s) {
        return s.replaceAll("[^A-Za-z]", "").toLowerCase();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myMessenger.getBinder();
    }
}
