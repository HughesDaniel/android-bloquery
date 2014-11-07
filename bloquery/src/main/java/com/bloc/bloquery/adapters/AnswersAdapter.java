package com.bloc.bloquery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bloc.bloquery.R;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

/**
 * Created by Daniel on 11/4/2014.
 */
public class AnswersAdapter extends ParseQueryAdapter{

    private static final String TAG = ".AnswersAdapter.java";
    private static final String PARSE_ANSWER = "theAnswer";
    private static final String PARSE_NUM_UPVOTES = "numberOfUpVotes";

    private Context mContext;

    public AnswersAdapter(Context context, QueryFactory queryFactory) {
        super(context, queryFactory);
        mContext = context;
    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        super.getItemView(object, v, parent);

        View cView = v;

        if (cView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            cView = inflater.inflate(R.layout.answer_in_lv, parent, false);
        }

        TextView answerTextView = (TextView) cView.findViewById(R.id.tv_answer_body);
        answerTextView.setText(object.getString(PARSE_ANSWER));

        TextView votesTextView = (TextView) cView.findViewById(R.id.tv_number_up_votes);
        votesTextView.setText(object.getInt(PARSE_NUM_UPVOTES) + " " +
                mContext.getString(R.string.votes));

        return cView;
    }
}
