package com.bloc.bloquery;

import android.app.Activity;
import android.os.Bundle;

import com.bloc.bloquery.fragments.ProfileFragment;

/**
 * Created by Daniel on 11/17/2014.
 */
public class ProfileActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();

        setContentView(R.layout.activity_profile);

        String username = extras.getString("username");
        String description = extras.getString("description");
        byte[] avatar = extras.getByteArray("avatar");

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container2, ProfileFragment.newInstance(username, description, avatar))
                    .commit();
        }

    }
}
