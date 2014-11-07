package com.bloc.bloquery;

import android.app.Application;

import com.parse.Parse;

public class BloQueryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Parse
        Parse.initialize(getApplicationContext(), getString(R.string.parse_app_id),
                getString(R.string.parse_client_key));

    }
}
