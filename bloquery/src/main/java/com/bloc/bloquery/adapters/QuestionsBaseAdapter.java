package com.bloc.bloquery.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bloc.bloquery.R;
import com.bloc.bloquery.models.QuestionModel;

import java.util.List;

/**
 * Created by Daniel on 10/30/2014.
 */
public class QuestionsBaseAdapter extends BaseAdapter {

    private static final String TAG = ".QuestionsBaseAdapter.java";

    List<QuestionModel> mQuestions;

    public QuestionsBaseAdapter(List<QuestionModel> list) {
        mQuestions = list;
    }

    @Override
    public int getCount() {
        return mQuestions.size();
    }

    @Override
    public Object getItem(int position) {
        return mQuestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cView = convertView;

        if (cView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            cView = inflater.inflate(R.layout.question_in_lv, parent, false);
        }

        QuestionModel q = (QuestionModel) getItem(position);

        TextView questionTextView = (TextView) cView.findViewById(R.id.tv_question_body);
        questionTextView.setText(q.getQuestion());

        TextView numberOfAnswersTextView = (TextView) cView.findViewById(R.id.tv_number_answers);
        numberOfAnswersTextView.setText(String.valueOf(q.getNumberOfAnswers()));


        return cView;
    }
}
