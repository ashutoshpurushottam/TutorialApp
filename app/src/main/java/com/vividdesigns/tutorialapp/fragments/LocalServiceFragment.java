package com.vividdesigns.tutorialapp.fragments;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vividdesigns.tutorialapp.R;
import com.vividdesigns.tutorialapp.service.BoundService;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalServiceFragment extends Fragment {

    // instance of the bound service used
    BoundService myService;
    boolean isBound = false;

    // ui elements
    private TextView randomTextView;
    private Button randomButton;

    // Create ServiceConnection object
    private ServiceConnection myServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BoundService.MyLocalBinder binder = (BoundService.MyLocalBinder) service;
            myService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };


    public LocalServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        // bind to the service when fragment is active
        Intent intent = new Intent(getActivity(), BoundService.class);
        getActivity().bindService(intent, myServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_local, container, false);
        randomTextView = (TextView) rootView.findViewById(R.id.random_tv);
        randomButton = (Button) rootView.findViewById(R.id.random_btn);
        setListeners();
        return rootView;
    }

    private void setListeners() {
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomNumber();
            }
        });
    }

    /**
     * Get random number from the service and display it
     */
    private void  getRandomNumber() {
        if(isBound) {
            int randomNumber = myService.getRandomNumber();
            randomTextView.setText(String.valueOf(randomNumber));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // unbind from the service when fragment pauses
        getActivity().unbindService(myServiceConnection);
    }


}
