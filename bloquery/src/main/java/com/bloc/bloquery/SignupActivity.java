package com.bloc.bloquery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;

/**
 * Created by Daniel on 11/17/2014.
 */
public class SignupActivity extends Activity {

    private static final String TAG = ".SignupActivity.java";

    private static final String PARSE_USER_DESCRIPTION = "description";
    private static final String PARSE_USER_AVATAR = "avatar";

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPass;
    private EditText mDescriptionEditText;
    private Button mUpload;
    private Button mCreateAccnt;

    private String mUsername;
    private String mPassword;
    private String mDescription;
    private byte[] mBytes;
    private ParseFile mAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mUsernameEditText = (EditText) findViewById(R.id.et_enter_username);
        mPasswordEditText = (EditText) findViewById(R.id.et_enter_pass);
        mConfirmPass = (EditText) findViewById(R.id.et_confirm_pass);
        mDescriptionEditText = (EditText) findViewById(R.id.et_enter_description);

        mUpload = (Button) findViewById(R.id.btn_avatar_upload);
        //TODO upload avatar click listener

        mCreateAccnt = (Button) findViewById(R.id.btn_create_account);
        mCreateAccnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogPass();
                getDescription();
                getAvatar();
                if (mUsername == "" || mPassword == "") {
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_signup),
                            Toast.LENGTH_LONG).show();
                } else {
                    ParseUser user = new ParseUser();
                    user.setUsername(mUsername);
                    user.setPassword(mPassword);
                    user.put(PARSE_USER_DESCRIPTION, mDescription);
                    user.put(PARSE_USER_AVATAR, mAvatar);
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getApplicationContext(), getString(R.string.successful_signup),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.signup_error),
                                        Toast.LENGTH_LONG).show();
                                Log.d(TAG, e.toString());
                            }
                        }
                    });
                }
            }
        });
    }

    private void getLogPass() {
        if (passwordsAreEqual()) {
            mUsername = mUsernameEditText.getText().toString();
            mPassword = mConfirmPass.getText().toString();
        } else {
            Toast.makeText(this, "Passwords are not equal", Toast.LENGTH_LONG).show();
        }
    }

    // checks to make sure the password fields match
    private boolean passwordsAreEqual() {
        String pass1 = mPasswordEditText.getText().toString();
        String pass2 = mConfirmPass.getText().toString();

        return pass1.equals(pass2);
    }

    private void getDescription() {
        mDescription = mDescriptionEditText.getText().toString();
    }

    // if user hasnt uploaded an avatar it gets the default avatar
    private void getAvatar() {
        if (mBytes == null) {
            mBytes = getDefaultAvatar();

            mAvatar = new ParseFile("default.png", mBytes);
            try {
                mAvatar.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    // converts the generic avatar drawable into a byte array
    private byte[] getDefaultAvatar() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_generic_small);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }

}
