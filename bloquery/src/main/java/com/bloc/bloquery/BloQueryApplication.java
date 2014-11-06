package com.bloc.bloquery;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseUser;

public class BloQueryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Parse
        Parse.initialize(getApplicationContext(), getString(R.string.parse_app_id),
                getString(R.string.parse_client_key));

        Log.d("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE", ParseUser.getCurrentUser().getUsername().toString());
        ParseUser.getCurrentUser().logOut();
    }
}
