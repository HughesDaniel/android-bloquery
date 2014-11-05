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
public class QuestionsAdapter extends ParseQueryAdapter {

    private static final String PARSE_QUESTION = "theQuestion";
    private static final String PARSE_NUM_ANSWERS = "numberOfAnswers";

    public QuestionsAdapter(Context context, String className) {
        super(context, className);
    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        super.getItemView(object, v, parent);

        View cView = v;

        if (cView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            cView = inflater.inflate(R.layout.question_in_lv, parent, false);
        }


        TextView questionTextView = (TextView) cView.findViewById(R.id.tv_question_body);
        questionTextView.setText(object.getString(PARSE_QUESTION));

        TextView numberOfAnswersTextView = (TextView) cView.findViewById(R.id.tv_number_answers);
        numberOfAnswersTextView.setText(String.valueOf(object.getInt(PARSE_NUM_ANSWERS)));


        return cView;

    }

}
