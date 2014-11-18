package com.bloc.bloquery.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bloc.bloquery.R;

/**
 * Created by Daniel on 11/17/2014.
 */
public class ProfileFragment extends Fragment{

    private static final String KEY_USERNAME =
            "com.bloc.bloquery.fragments.ProfileFragment.key_username";
    private static final String KEY_DESCRIPTION =
            "com.bloc.bloquery.fragments.ProfileFragment.key_description";
    private static final String KEY_AVATAR =
            "com.bloc.blowquery.fragments.ProfileFragment.key_avatar";

    private String mUsername;
    private String mDescription;
    private byte[] mAvatar;

    private ImageView mAvatarImageView;
    private TextView mDescriptionTextView;

    public static ProfileFragment newInstance(String username, String description, byte[] avatar) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(KEY_USERNAME, username);
        args.putString(KEY_DESCRIPTION, description);
        args.putByteArray(KEY_AVATAR, avatar);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUsername = getArguments().getString(KEY_USERNAME);
        mDescription = getArguments().getString(KEY_DESCRIPTION);
        mAvatar = getArguments().getByteArray(KEY_AVATAR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // set the action bar title to the username
        getActivity().setTitle(mUsername);

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mDescriptionTextView = (TextView) view.findViewById(R.id.tv_profile_description);
        mDescriptionTextView.setText(mDescription);

        mAvatarImageView = (ImageView) view.findViewById(R.id.iv_profile_avatar);
        mAvatarImageView.setImageBitmap(convertArrayToBitmap(mAvatar));

        return view;
    }

    private Bitmap convertArrayToBitmap(byte[] array) {
        int length = array.length;
        Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, length);

        return bitmap;
    }
}
