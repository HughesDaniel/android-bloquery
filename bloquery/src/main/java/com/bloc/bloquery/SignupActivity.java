package com.bloc.bloquery;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

    // Parse constants
    private static final String PARSE_USER_DESCRIPTION = "description";
    private static final String PARSE_USER_AVATAR = "avatar";

    // used for activity on result code
    private static final int SELECT_PICTURE = 1;

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPass;
    private EditText mDescriptionEditText;
    private ImageView mAvatarImageView;
    private Button mUpload;
    private Button mCreateAccnt;

    private String mUsername;
    private String mPassword;
    private String mDescription;
    private boolean mAvatarSelected = false;
    // we need a byte array to create a parse file
    private byte[] mBytes;
    private ParseFile mAvatar;
    // where we will store the path of user selected image
    private String mSelectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mUsernameEditText = (EditText) findViewById(R.id.et_enter_username);
        mPasswordEditText = (EditText) findViewById(R.id.et_enter_pass);
        mConfirmPass = (EditText) findViewById(R.id.et_confirm_pass);
        mDescriptionEditText = (EditText) findViewById(R.id.et_enter_description);
        mAvatarImageView = (ImageView) findViewById(R.id.iv_signup_avatar);

        mUpload = (Button) findViewById(R.id.btn_avatar_upload);
        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchImagePicker();
            }
        });

        mCreateAccnt = (Button) findViewById(R.id.btn_create_account);
        mCreateAccnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get all the data so that we can create a Parse user account
                getLogPass();
                getDescription();
                getAvatar();
                if (mUsername == "" || mPassword == "") {
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_signup),
                            Toast.LENGTH_LONG).show();
                } else {
                    createUser();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                mAvatarSelected = true; // set to true so we know not to load default later
                Uri selectedImageUri = data.getData();
                mSelectedImagePath = getPath(selectedImageUri);

                if(mSelectedImagePath == null){
                    loadPicasaImageFromGallery(selectedImageUri);
                }
                convertPathToByteArray();
            }
        }
    }

    // helper function to take the image user selected and turn it into a byte array
    private void convertPathToByteArray() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(mSelectedImagePath, options);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        mBytes = stream.toByteArray();
        // displays the avatar user slected, shouldnt have it in this function, kind of sloppy
        mAvatarImageView.setImageBitmap(scaledBitmap);
    }

    // creates and launches the intent that allows user to select an image
    private void launchImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        } else {
            Toast.makeText(this, "No image viewer present", Toast.LENGTH_LONG).show();
        }
    }

    // makes sure password fields match and then sets user name and password member variables
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

    // sets member variable to the description user entered
    private void getDescription() {
        mDescription = mDescriptionEditText.getText().toString();
    }

    // if user hasnt uploaded an avatar it gets the default avatar
    private void getAvatar() {
        if (mAvatarSelected) { // flag set if user has uploaded an avatar
            mAvatar = new ParseFile(mUsername + ".png", mBytes);
        } else {
            mBytes = getDefaultAvatar();
            mAvatar = new ParseFile("default.png", mBytes);
        }
        try {
            mAvatar.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // converts the generic avatar drawable into a byte array
    private byte[] getDefaultAvatar() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_generic_small);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }

    // creates a user account on Parse with the info user entered
    private void createUser() {
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
                    // we are done with this activity, close it for the user
                    closeActivity();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.signup_error),
                            Toast.LENGTH_LONG).show();
                    Log.d(TAG, e.toString());
                }
            }
        });
    }

    private void closeActivity() {
        this.finish();
    }

    // helper method for getting the path to the image the user selected
    private String getPath(Uri uri) {
        String[] projection = {  MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        else
            return uri.getPath();               // FOR OI/ASTRO/Dropbox etc
    }

    // this prolly wont even work, lol
    private void loadPicasaImageFromGallery(final Uri uri) {
        String[] projection = {  MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
            if (columnIndex != -1) {
                new Thread(new Runnable() {
                    // NEW THREAD BECAUSE NETWORK REQUEST WILL BE MADE THAT WILL BE A LONG PROCESS & BLOCK UI
                    // IF CALLED IN UI THREAD
                    public void run() {
                        try {
                            Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            // THIS IS THE BITMAP IMAGE WE ARE LOOKING FOR.
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        }
        cursor.close();
    }

}
