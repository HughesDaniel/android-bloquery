package com.bloc.bloquery.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bloc.bloquery.R;
import com.bloc.bloquery.adapters.AnswersAdapter;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by Daniel on 11/4/2014.
 */
public class AnswersFragment extends Fragment {

    private static final String TAG = ".AnswersFragment.java";

    // key to set and retrieve the question ID we store in the args for this fragment
    private static final String KEY_QUESTION_ID =
            "com.bloc.bloquery.fragments.AnswersFragment.key_question_id";
    // key to set and retrieve the question we store in the args for this fragment
    private static final String KEY_QUESTION =
            "com.bloc.bloquery.fragments.AnswersFragment.key_question";

    // The unique ID of the Question that we will display the answers to
    private String mQuestionId;
    private String mQuestion;

    private ListView mListView;
    private AnswersAdapter mAdapter;

    public static AnswersFragment newInstance(String questionId, String question) {
        AnswersFragment fragment = new AnswersFragment();
        Bundle args = new Bundle();
        args.putString(KEY_QUESTION_ID, questionId);
        args.putString(KEY_QUESTION, question);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mQuestionId = getArguments().getString(KEY_QUESTION_ID);
        mQuestion = getArguments().getString(KEY_QUESTION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.answers_listview,
                container, false);
        TextView questionTextView = (TextView) rootView.findViewById(R.id.tv_answer_in_lv_footer);
        questionTextView.setText(mQuestion);

        mListView = (ListView) rootView.findViewById(R.id.lv_answers);

        ParseQueryAdapter.QueryFactory<ParseObject> factory =
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery create() {
                        ParseQuery query = new ParseQuery("Answer");
                        query.whereEqualTo("parent", mQuestionId);
                        return query;
                    }
                };

        mAdapter = new AnswersAdapter(getActivity(), factory);
        mListView.setAdapter(mAdapter);

        return rootView;
    }
}
