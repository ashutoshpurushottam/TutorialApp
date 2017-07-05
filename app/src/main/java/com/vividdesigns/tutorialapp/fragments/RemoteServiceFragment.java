package com.vividdesigns.tutorialapp.fragments;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vividdesigns.tutorialapp.R;
import com.vividdesigns.tutorialapp.service.RemoteService;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemoteServiceFragment extends Fragment {

    private static final String LOG_TAG  = RemoteServiceFragment.class.getSimpleName();

    Messenger myService = null;
    boolean isBound;

    private TextView mRemoteTextView;

    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBound = true;
            myService = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            myService = null;
        }
    };


    public RemoteServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_remote, container, false);
        mRemoteTextView = (TextView)rootView.findViewById(R.id.remote_string_tv);

        Button messageButton = (Button) rootView.findViewById(R.id.message_btn);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Bind to the remote service for IPC
        Intent intent = new Intent();
        intent.setAction("com.vividdesigns.RemoteService");
        intent.setPackage(getActivity().getPackageName());
        getActivity().bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
    }

    private void sendMessage() {
        if(!isBound) return;

        String randomString = getRandomString();

        displayToast(randomString);

        // Create an instance of the Handler here
        ResponseHandler.Listener listener = createListener();

        Message msg = Message.obtain(null, RemoteService.STRIP_STRING_REQUEST);
        msg.replyTo = new Messenger(new ResponseHandler(listener));
        Bundle bundle = new Bundle();

        bundle.putString("random", randomString);
        msg.setData(bundle);

        try {
            // send message to the service
            myService.send(msg);
        } catch (RemoteException e) {
            Log.i(LOG_TAG, e.getMessage());
        }

    }

    private void displayToast(String randomString) {
        Toast toast = Toast.makeText(getActivity(), "To Service: " + randomString, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private ResponseHandler.Listener createListener() {
        return new ResponseHandler.Listener() {
            @Override
            public void onSuccess(String message) {
                // on successful response display the text
                mRemoteTextView.setText(message);
            }
        };
    }

    private static class ResponseHandler extends Handler {

        // Listener created to avoid tight coupling between the
        // handler and the activity
        private ResponseHandler(Listener listener) {
            this.listener = listener;
        }

        private Listener listener;

        @Override
        public void handleMessage(Message msg) {
            int code = msg.what;
            switch (code) {
                case RemoteService.STRIP_STRING_RESPONSE: {
                    String strippedString = msg.getData().getString("strippedString");
                    // call the listener method to set the text
                    listener.onSuccess(strippedString);
                }
            }
        }

        interface Listener {
           void onSuccess(String message);
        }
    }


    /**
     * Helper method to create a random string
     */
    protected String getRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    /**
     * Unbind from the service when fragment is onPaused state
     */
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unbindService(myConnection);
    }
}
