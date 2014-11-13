package com.bloc.bloquery.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bloc.bloquery.R;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

/**
 * Created by Daniel on 11/4/2014.
 */
public class QuestionsAdapter extends ParseQueryAdapter {

    private static final String PARSE_QUESTION = "theQuestion";
    private static final String PARSE_NUM_ANSWERS = "numberOfAnswers";

    private Context mContext;
    private int mNumberAnswers;
    private ImageView mInterestIndicator;

    public QuestionsAdapter(Context context, QueryFactory queryFactory) {
        super(context, queryFactory);
        mContext = context;
    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        super.getItemView(object, v, parent);

        mNumberAnswers = object.getInt(PARSE_NUM_ANSWERS);

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
        numberOfAnswersTextView.setText(mNumberAnswers + " "
                            + mContext.getString(R.string.answers));

        mInterestIndicator = (ImageView) cView.findViewById(R.id.iv_interest_indicator);
        setInterestIndicator();



        return cView;

    }

    // Sets the image to display based upon the number of answers to the question
    private void setInterestIndicator() {
        int currentVersion = Build.VERSION.SDK_INT;
        if (currentVersion >= 16 && mNumberAnswers > 2) {
            mInterestIndicator.setBackground(getBolts());
        } else if (currentVersion == 15 && mNumberAnswers > 2) {
            mInterestIndicator.setBackgroundDrawable(getBolts());
        }
    }

    // returns the correct graphic (lightning bolts) to display in indicator
    // only called if there are at least 3 answers
    private Drawable getBolts() {
        if (mNumberAnswers > 9) {
            return mContext.getResources().getDrawable(R.drawable.triple_bolt);
        } else if (mNumberAnswers > 5) {
            return mContext.getResources().getDrawable(R.drawable.double_bolt);
        } else {
            return mContext.getResources().getDrawable(R.drawable.single_bolt);
        }
    }

}
