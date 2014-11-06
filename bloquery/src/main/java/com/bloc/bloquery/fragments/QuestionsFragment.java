package com.bloc.bloquery.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bloc.bloquery.R;
import com.bloc.bloquery.adapters.QuestionsAdapter;
import com.parse.ParseUser;

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
            throw new ClassCastException("Activity must implement QuestionFragmentCallback");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Entering onResume()");
        mAdapter.notifyDataSetChanged();

    }

    private void selectItem(int position) {
        // the unique ID of the question that was selected
        String id = mAdapter.getItem(position).getObjectId();
        String question = mAdapter.getItem(position).getString(PARSE_QUESTION);
        mCallBack.onQuestionItemSelected(id, question);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.blo_query, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id == R.id.new_bloquery) {
            if (isLoggedIn()) {
                AskQuestionDialog askQuestion = new AskQuestionDialog();
                askQuestion.show(getFragmentManager(), "ask_question_dialog_fragment");
            } else {
                LoginDialog login = new LoginDialog();
                login.show(getFragmentManager(), "login_dialog_fragment");
            }
            return true;
        }
        return false;
    }

    private boolean isLoggedIn() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        return (currentUser != null);
    }
}
