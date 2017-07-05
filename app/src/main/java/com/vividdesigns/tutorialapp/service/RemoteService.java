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

public class RemoteService extends Service {

    private static final String LOG_TAG = RemoteService.class.getSimpleName();

    // codes for the client-server communication (IPC)

    public static final int STRIP_STRING_REQUEST = 13;
    public static final int STRIP_STRING_RESPONSE = 17;

    private final Messenger myMessenger = new Messenger(new IncomingHandler());


    private static class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STRIP_STRING_REQUEST: {
                    try {
                        Bundle data = msg.getData();

                        // Get the string from the client and strip all non-alpha characters
                        // convert the whole string to lowercase
                        String obtainedString = data.getString("random");
                        String responseString = stripNonAlpha(obtainedString);

                        // Send the stripped string back to client
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

    /**
     * Helper method which strips all non-alpha characters from the string
     * and converts the whole string to lowercase.
     */
    private static String stripNonAlpha(String s) {
        return s.replaceAll("[^A-Za-z]", "").toLowerCase();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myMessenger.getBinder();
    }
}
