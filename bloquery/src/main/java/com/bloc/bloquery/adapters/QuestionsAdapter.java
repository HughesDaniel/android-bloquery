package com.bloc.bloquery.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bloc.bloquery.ProfileActivity;
import com.bloc.bloquery.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/**
 * Created by Daniel on 11/4/2014.
 */
public class QuestionsAdapter extends ParseQueryAdapter {

    private static final String TAG = ".QuestionsAdapter.java";

    private static final String PARSE_QUESTION = "theQuestion";
    private static final String PARSE_NUM_ANSWERS = "numberOfAnswers";
    private static final String PARSE_ASKER_AVATAR = "askerAvatar";
    private static final String PARSE_QUESTION_ASKER = "questionAsker";
    private static final String PARSE_USER_DESCRIPTION = "description";
    private static final String PARSE_USER_AVATAR = "avatar";

    // constants for question interest indicator
    private static final int HIGH_INTEREST = 10;
    private static final int MEDIUM_INTEREST = 6;
    private static final int LOW_INTEREST = 3;

    private Context mContext;

    public QuestionsAdapter(Context context, QueryFactory queryFactory) {
        super(context, queryFactory);
        mContext = context;
    }

    @Override
    public View getItemView(final ParseObject object, View v, ViewGroup parent) {
        super.getItemView(object, v, parent);

        int numberAnswers = object.getInt(PARSE_NUM_ANSWERS);

        View cView = v;

        if (cView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            cView = inflater.inflate(R.layout.question_in_lv, parent, false);
        }

        // Text View that displays question
        TextView questionTextView = (TextView) cView.findViewById(R.id.tv_question_body);
        questionTextView.setText(object.getString(PARSE_QUESTION));

        // Displays the number of answers
        TextView numberOfAnswersTextView = (TextView) cView.findViewById(R.id.tv_number_answers);
        numberOfAnswersTextView.setText(numberAnswers + " "
                            + mContext.getString(R.string.answers));

        ImageView interestIndicator = (ImageView) cView.findViewById(R.id.iv_interest_indicator);
        setInterestIndicator(interestIndicator, numberAnswers);

        // displays avatar of the asker
        ImageButton askerAvatarImageButton = (ImageButton) cView.findViewById(R.id.ib_avatar_question_asker);
        ParseFile avatar = object.getParseFile(PARSE_ASKER_AVATAR);
        try {
            byte[] avatarByte = avatar.getData();
            askerAvatarImageButton.setImageBitmap(decodeBitmap(avatarByte));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //load profile page for that user
        askerAvatarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ProfileActivity.class);
                ParseUser user = object.getParseUser(PARSE_QUESTION_ASKER);
                try {
                    user.fetch();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                i.putExtra("username", user.getUsername());
                i.putExtra("description", user.getString(PARSE_USER_DESCRIPTION));
                try {
                    i.putExtra("avatar", user.getParseFile(PARSE_USER_AVATAR).getData());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mContext.startActivity(i);
            }
        });



        return cView;

    }

    // Sets the image to display based upon the number of answers to the question
    private void setInterestIndicator(ImageView interestIndicator, int numberAnswers) {
        int currentVersion = Build.VERSION.SDK_INT;
        if (currentVersion >= 16) {
            interestIndicator.setBackground(getBolts(numberAnswers));
        } else {
            interestIndicator.setBackgroundDrawable(getBolts(numberAnswers));
        }
    }

    // returns the correct graphic (lightning bolts) to display in indicator
    // only called if there are at least 3 answers
    private Drawable getBolts(int numberAnswers) {
        if (numberAnswers >= HIGH_INTEREST) {
            return mContext.getResources().getDrawable(R.drawable.triple_bolt);
        } else if (numberAnswers >= MEDIUM_INTEREST) {
            return mContext.getResources().getDrawable(R.drawable.double_bolt);
        } else if (numberAnswers >= LOW_INTEREST) {
            return mContext.getResources().getDrawable(R.drawable.single_bolt);
        } else {
            return null;
        }
    }

    private Bitmap decodeBitmap(byte[] file) {
        int length = file.length;
        Bitmap bitmap = BitmapFactory.decodeByteArray(file, 0,length);

        return bitmap;
    }

}
