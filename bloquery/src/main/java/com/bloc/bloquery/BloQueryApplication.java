package com.bloc.bloquery;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

public class BloQueryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Parse
        Parse.initialize(getApplicationContext(), getString(R.string.parse_app_id),
                getString(R.string.parse_client_key));

        // temporary so I can add questions
        //TODO: implement an actual login process
        try {
            ParseUser.logIn("Alobar", "shazam"); // Don't get excited, this is the only place I use this log:pass on the internet :) :)
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
