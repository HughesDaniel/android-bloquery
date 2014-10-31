package com.bloc.bloquery.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bloc.bloquery.R;
import com.bloc.bloquery.adapters.QuestionsBaseAdapter;
import com.bloc.bloquery.models.QuestionModel;
import com.bloc.bloquery.models.QuestionModelCenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 10/29/2014.
 */
public class QuestionsFragment extends Fragment implements QuestionModelCenter.onParseUpdate {

    private static final String TAG = ".QuestionsFragment.java";

    private ListView mListView;
    private QuestionsBaseAdapter mArrayAdapter;

    // Will hold all the Questions for the adapter
    private List<QuestionModel> mQuestions = new ArrayList<QuestionModel>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.questions_listview,
                container, false);

        mListView = (ListView) rootView.findViewById(R.id.lv_questions);

        // pass ourselves in so we can get callbacks
        QuestionModelCenter modelCenter = new QuestionModelCenter(this);
        modelCenter.getQuestions();

        mArrayAdapter = new QuestionsBaseAdapter(mQuestions);
        mListView.setAdapter(mArrayAdapter);

        return rootView;
    }

    @Override
    public void onUpdate(List<QuestionModel> questionList) {
        mQuestions = questionList;
        mArrayAdapter = new QuestionsBaseAdapter(questionList); // pass the original cuz it is still being updated asynchronously
        mListView.setAdapter(mArrayAdapter);
    }

    @Override
    public void onModelUpdate() {
        mArrayAdapter.notifyDataSetChanged();
    }
}
