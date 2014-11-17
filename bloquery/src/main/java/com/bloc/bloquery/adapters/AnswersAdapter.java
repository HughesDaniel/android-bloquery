package com.bloc.bloquery.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bloc.bloquery.R;
import com.bloc.bloquery.models.AnswerModelCenter;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Daniel on 11/4/2014.
 */
public class AnswersAdapter extends ParseQueryAdapter{

    private static final String TAG = ".AnswersAdapter.java";
    private static final String PARSE_ANSWER = "theAnswer";
    private static final String PARSE_NUM_UPVOTES = "numberOfUpVotes";
    private static final String PARSE_UP_VOTERS = "upVoters";
    private static final String PARSE_ANSWERER_AVATAR = "answererAvatar";

    private Context mContext;
    private String mUserName;
    // the back half of the string for concatenation in our textview
    private String mVotesString;
    private AnswerModelCenter mAMC;
    private Drawable mThumbsUpImg;
    private Drawable mCheckMarkImg;



    public AnswersAdapter(Context context, QueryFactory queryFactory) {
        super(context, queryFactory);
        mContext = context;
        mUserName = getUserName();
        mVotesString = " " +  mContext.getString(R.string.votes);
        mAMC = new AnswerModelCenter(mContext);
        mThumbsUpImg = mContext.getResources().getDrawable(R.drawable.thumbs_up);
        mCheckMarkImg = mContext.getResources().getDrawable(R.drawable.check_mark);
    }

    @Override
    public View getItemView(final ParseObject object, View v, ViewGroup parent) {
        super.getItemView(object, v, parent);

        // list of usernames for everyone that has upvoted the answer
        final List<String> upVoters = object.getList(PARSE_UP_VOTERS);
        final int numUpVotes = object.getInt(PARSE_NUM_UPVOTES);
        final ImageButton upVoteButton;

        View cView = v;

        if (cView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            cView = inflater.inflate(R.layout.answer_in_lv, parent, false);
        }

        // Displays the answer
        TextView answerTextView = (TextView) cView.findViewById(R.id.tv_answer_body);
        answerTextView.setText(object.getString(PARSE_ANSWER));

        ImageView answererAvatar = (ImageView) cView.findViewById(R.id.iv_answerer_avatar_in_lv);
        try {
            Bitmap avatar = decodeBitmap(object.getParseFile(PARSE_ANSWERER_AVATAR).getData());
            answererAvatar.setImageBitmap(avatar);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // Displays number of upvotes
        final TextView votesTextView = (TextView) cView.findViewById(R.id.tv_number_up_votes);
        votesTextView.setText(numUpVotes + mVotesString);

        upVoteButton = (ImageButton) cView.findViewById(R.id.ib_is_upvoted);
        // sets image to checkmark if user has voted
        if (mUserName != null) { // if user isnt logged in, we want to show default image of thumb
            setToCheckMark(upVoteButton, upVoters.contains(mUserName));
        }
        upVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserName != null) {
                    if (isCheckMarkDisplayed(upVoteButton)) {
                        downVote(object, upVoteButton, votesTextView, numUpVotes);
                    } else {
                        upVote(object, upVoteButton, votesTextView, numUpVotes);
                    }
                } else { // user not logged in, so cant vote
                    Toast.makeText(mContext, mContext.getString(R.string.login_vote),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        return cView;
    }

    private String getUserName() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUsername();
        }
        return null;
    }

    // Sets to checkmark image if boolean is set to 'true', thumbsup if 'false'
    private void setToCheckMark(ImageButton upVoteButton, boolean hasUpVoted) {
        int currentVersion = Build.VERSION.SDK_INT;
        if (currentVersion < 16) {
            if (hasUpVoted) {
                upVoteButton.setBackgroundDrawable(mCheckMarkImg);
            } else {
                upVoteButton.setBackgroundDrawable(mThumbsUpImg);
            }
        } else if (hasUpVoted) {
            upVoteButton.setBackground(mCheckMarkImg);
        } else if (!hasUpVoted) {
            upVoteButton.setBackground(mThumbsUpImg);
        }
    }

    private boolean isCheckMarkDisplayed(ImageButton upVoteButton) {
        if (upVoteButton.getBackground() == mCheckMarkImg) {
            return true;
        }
        return false;
    }

    // handles the process of upvoting and calling method that saves data to Parse
    private void upVote(ParseObject Answer, ImageButton upVoteButton, TextView tv, int numVotes) {
        setToCheckMark(upVoteButton, true);
        tv.setText((numVotes + 1) + mVotesString);
        mAMC.addUpVoterToAnswer(Answer, mUserName);
    }

    // handles the process of removing the user from the answers list of upvoters on Parse
    private void downVote(ParseObject Answer, ImageButton upVoteButton, TextView tv, int numUpVotes) {
        setToCheckMark(upVoteButton, false);
        tv.setText((numUpVotes - 1) + mVotesString);
        mAMC.removeUpVoterFromAnswer(Answer, mUserName);
    }

    private Bitmap decodeBitmap(byte[] file) {
        int length = file.length;
        Bitmap bitmap = BitmapFactory.decodeByteArray(file, 0, length);

        return bitmap;
    }

}
