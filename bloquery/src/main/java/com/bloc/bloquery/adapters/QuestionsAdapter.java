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
import android.widget.ImageView;
import android.widget.TextView;

import com.bloc.bloquery.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

/**
 * Created by Daniel on 11/4/2014.
 */
public class QuestionsAdapter extends ParseQueryAdapter {

    private static final String TAG = ".QuestionsAdapter.java";

    private static final String PARSE_QUESTION = "theQuestion";
    private static final String PARSE_NUM_ANSWERS = "numberOfAnswers";
    private static final String PARSE_ASKER_AVATAR = "askerAvatar";

    private Context mContext;

    public QuestionsAdapter(Context context, QueryFactory queryFactory) {
        super(context, queryFactory);
        mContext = context;
    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
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

        ImageView askerAvatarImageView = (ImageView) cView.findViewById(R.id.iv_avatar_question_asker);
        ParseFile avatar = object.getParseFile(PARSE_ASKER_AVATAR);
        try {
            byte[] avatarByte = avatar.getData();
            askerAvatarImageView.setImageBitmap(decodeBitmap(avatarByte));
        } catch (ParseException e) {
            e.printStackTrace();
        }



        return cView;

    }

    // Sets the image to display based upon the number of answers to the question
    private void setInterestIndicator(ImageView interestIndicator, int numberAnswers) {
        int currentVersion = Build.VERSION.SDK_INT;
        if (currentVersion >= 16 && numberAnswers > 2) {
            interestIndicator.setBackground(getBolts(numberAnswers));
        } else if (currentVersion == 15 && numberAnswers > 2) {
            interestIndicator.setBackgroundDrawable(getBolts(numberAnswers));
        }
    }

    // returns the correct graphic (lightning bolts) to display in indicator
    // only called if there are at least 3 answers
    private Drawable getBolts(int numberAnswers) {
        if (numberAnswers > 9) {
            return mContext.getResources().getDrawable(R.drawable.triple_bolt);
        } else if (numberAnswers > 5) {
            return mContext.getResources().getDrawable(R.drawable.double_bolt);
        } else {
            return mContext.getResources().getDrawable(R.drawable.single_bolt);
        }
    }

    private Bitmap decodeBitmap(byte[] file) {
        int length = file.length;
        Bitmap bitmap = BitmapFactory.decodeByteArray(file, 0,length);

        return bitmap;
    }

}
