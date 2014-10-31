package com.bloc.bloquery.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bloc.bloquery.R;
import com.bloc.bloquery.adapters.QuestionsBaseAdapter;
import com.bloc.bloquery.models.QuestionModel;
import com.bloc.bloquery.models.QuestionModelCenter;

import java.util.List;

/**
 * Created by Daniel on 10/29/2014.
 */
public class QuestionsFragment extends Fragment {

    private static final String TAG = ".QuestionsFragment.java";

    private ListView mListView;
    private BaseAdapter mArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, "onCreateView()");

        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.questions_listview,
                container, false);

        mListView = (ListView) rootView.findViewById(R.id.lv_questions);

        QuestionModelCenter modelCenter = new QuestionModelCenter();
        List<QuestionModel> questions = modelCenter.getQuestions();

        mArrayAdapter = new QuestionsBaseAdapter(questions);

        mListView.setAdapter(mArrayAdapter);

        return rootView;

    }
}
