package com.bloc.bloquery.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bloc.bloquery.R;
import com.bloc.bloquery.adapters.QuestionsAdapter;

/**
 * Created by Daniel on 10/29/2014.
 */
//public class QuestionsFragment extends Fragment implements QuestionModelCenter.onParseUpdate {
public class QuestionsFragment extends Fragment {

    public static interface QuestionFragmentCallback {
        void onQuestionItemSelected(String id, String question);
    }

    private static final String TAG = ".QuestionsFragment.java";

    private static final String PARSE_CLASS = "Question";
    private static final String PARSE_QUESTION = "theQuestion";

    private ListView mListView;
    private QuestionsAdapter mAdapter;

    private QuestionFragmentCallback mCallBack;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallBack = (QuestionFragmentCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.questions_listview,
                container, false);

        mListView = (ListView) rootView.findViewById(R.id.lv_questions);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        mAdapter = new QuestionsAdapter(getActivity(), PARSE_CLASS);
        mListView.setAdapter(mAdapter);

        return rootView;
    }

    private void selectItem(int position) {
        // the unique ID of the question that was selected
        String id = mAdapter.getItem(position).getObjectId();
        String question = mAdapter.getItem(position).getString(PARSE_QUESTION);
        mCallBack.onQuestionItemSelected(id, question);
    }
}
