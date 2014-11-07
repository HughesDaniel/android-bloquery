package com.bloc.bloquery.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bloc.bloquery.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by Daniel on 11/6/2014.
 */
public class LoginDialog extends DialogFragment {

    private static final String TAG = ".loginDialog.java";

    private Button mLoginButton;
    private Button mSignUpButton;
    private EditText mPasswordEditText;
    private EditText mUserNameEditText;
    private String mUsername;
    private String mPassword;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        getDialog().setTitle("Login to BloQuery");

        View view = inflater.inflate(R.layout.dialog_login, container, false);

        mUserNameEditText = (EditText) view.findViewById(R.id.et_username);
        mPasswordEditText = (EditText) view.findViewById(R.id.et_password);

        mLoginButton = (Button) view.findViewById(R.id.btn_login);
        mSignUpButton = (Button) view.findViewById(R.id.btn_signup);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogPass();

                ParseUser.logInInBackground(mUsername, mPassword,
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                if (parseUser != null) { // user exists
                                    Toast.makeText(getActivity(),
                                            getString(R.string.successful_login),
                                            Toast.LENGTH_LONG).show();
                                    closeDialog();
                                }
                                else { // user doesn't exist
                                    Toast.makeText(getActivity(),
                                            getString(R.string.no_such_user),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogPass();
                if (mUsername.equals("") || mPassword.equals("")) {
                    Toast.makeText(getActivity(), getString(R.string.invalid_signup),
                            Toast.LENGTH_LONG).show();
                } else {
                    ParseUser user = new ParseUser();
                    user.setUsername(mUsername);
                    user.setPassword(mPassword);
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), getString(R.string.successful_signup),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.signup_error),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });


        return view;
    }

    private void getLogPass() {
        mUsername = mUserNameEditText.getText().toString();
        mPassword = mPasswordEditText.getText().toString();
    }

    private void closeDialog() {
        this.dismiss();
    }
}
